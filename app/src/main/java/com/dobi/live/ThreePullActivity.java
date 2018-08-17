package com.dobi.live;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

public class ThreePullActivity extends AppCompatActivity {

    IjkVideoView ijkplayer0;
    IjkVideoView ijkplayer1;
    IjkVideoView ijkplayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_pull);
        ijkplayer0 = (IjkVideoView) findViewById(R.id.ijkplayer0);
        ijkplayer1 = (IjkVideoView) findViewById(R.id.ijkplayer1);
        ijkplayer2 = (IjkVideoView) findViewById(R.id.ijkplayer2);


        ijkplayer0.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        ijkplayer1.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        ijkplayer2.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        ijkplayer1.setVideoPath("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        ijkplayer2.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        ijkplayer0.start();
        ijkplayer1.start();
        ijkplayer2.start();

        ijkplayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
