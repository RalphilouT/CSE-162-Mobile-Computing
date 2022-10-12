package com.ralphiloutatoy.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class activity_play extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.sendString);
        //System.out.println(message);

        Uri vidFile = Uri.parse(message);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(vidFile);
        videoView.setMediaController(new MediaController(this));

        videoView.start();
//        vid.setMediaController(m);
//        String path = message;
//        Uri u = Uri.parse(path);
//        vid.setVideoPath(path);
//        vid.start();
    }
}