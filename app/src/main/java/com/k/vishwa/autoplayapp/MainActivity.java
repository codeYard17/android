package com.k.vishwa.autoplayapp;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    int i = 0;
    VideoView vidView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);


        vidView = (VideoView)findViewById(R.id.videoView);
        final MediaController mediaController;

        if (shouldAskPermission()) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);
        }


        mediaController= new MediaController(this);
        mediaController.setAnchorView(vidView);
        File dir=new File("/storage/emulated/0");
        final ArrayList<String> list=new ArrayList<>();
        findVideos(dir, list);
//        final ArrayList<String> list=getAllMedia();
        System.out.println("Size of list "+list.size());

        System.out.println("hello");
        final File[] file = {new File(list.get(0))};
        Uri uri=Uri.fromFile(file[0]);
        System.out.println("xkjhkjdskfjsdlkfjsdlfjsklfj " + uri.getPath());
        vidView.setVideoURI(uri);
        vidView.requestFocus();
        vidView.start();

        vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                i =(i + 1)%list.size();
                vidView.setVideoPath(list.get(i));
                vidView.requestFocus();
                vidView.start();
            }
        });
    }

    void findVideos(File dir, ArrayList<String> list){
        for (File file : dir.listFiles()) {
            if(file.getAbsolutePath().contains(".mp4"))
                list.add(file.getAbsolutePath());
        }
    }

//    public ArrayList<String> getAllMedia() {
//        HashSet<String> videoItemHashSet = new HashSet<>();
//        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
//        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
//        try {
//            cursor.moveToFirst();
//            do{
//                videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
//            }while(cursor.moveToNext());
//
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ArrayList<String> downloadedList = new ArrayList<>(videoItemHashSet);
//        return downloadedList;
//    }

//for getting the dialog of permission

    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    System.out.println("pass");

                } else {
                    System.out.println("fail");
                    finish();
                }

//                boolean writeAccepted = (grantResults[0]==PackageManager.PERMISSION_GRANTED);

                break;

        }

    }

}
