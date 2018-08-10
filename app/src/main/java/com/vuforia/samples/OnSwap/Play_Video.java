package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;

import com.vuforia.samples.VuforiaSamples.R;

public class Play_Video extends AppCompatActivity {

    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    Integer select_video;
    static int[] video = new int[]{
            R.raw.locate_chassis,
            R.raw.power,
    };
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();

        // set the main layout of the activity
        setContentView(R.layout.video_view);
        select_video = (Integer)getIntent().getSerializableExtra("selected_video");
        if(select_video == 0)
            w.setTitle("Demo to Scan the Portion of the Switch");
        else
            w.setTitle("Demo to replace the hardware");
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.video_play);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(this);
        // set a title for the progress bar
        progressDialog.setTitle("Onswap Video Tutorial");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +  video[select_video]));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });
    }


}