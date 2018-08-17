package com.dobi.live;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dobi.live.view.MyIjkVideoView;
import com.dobi.live.view.OnDoubleClickListener;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

public class ThreePullActivity extends AppCompatActivity {

    MyIjkVideoView ijkplayer0;
    MyIjkVideoView ijkplayer1;
    MyIjkVideoView ijkplayer2;

    private String[] videoUrl = {"rtmp://192.168.0.122/live/jason","rtmp://119.131.176.169/live/test2","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4","rtmp://video-center.evideo.tv/gtyx/","rtmp://live.hkstv.hk.lxdns.com/live/hks","rtmp://media3.sinovision.net:1935/live/livestream"
            ,"rtmp://media3.scctv.net/live/scctv_800","rtmp://ns8.indexforce.com/home/mystream","rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp"
            ,"rtmp://202.69.69.180:443/webcast/bshdlive-pc","rtmp://116.199.115.228/live/gztv_jingji"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_pull);
        ijkplayer0 = (MyIjkVideoView) findViewById(R.id.ijkplayer0);
        ijkplayer1 = (MyIjkVideoView) findViewById(R.id.ijkplayer1);
        ijkplayer2 = (MyIjkVideoView) findViewById(R.id.ijkplayer2);


        ijkplayer0.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        ijkplayer1.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        ijkplayer2.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        ijkplayer1.setVideoPath("rtmp://202.69.69.180:443/webcast/bshdlive-pc");
        ijkplayer2.setVideoPath("rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp");

        ijkplayer0.start();
        ijkplayer1.start();
        ijkplayer2.start();


        ijkplayer1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                //双击事件
              //  Toast.makeText(ThreePullActivity.this,"ijkplayer1双击拉",Toast.LENGTH_LONG).show();
                String path0 = ijkplayer0.getVideoPath();
                String path1 = ijkplayer1.getVideoPath();
                Log.e("path0--",path0);
                Log.e("path1--",path1);
                ijkplayer0.stopPlayback();
                ijkplayer1.stopPlayback();
                ijkplayer0.setVideoPath(path1);
                ijkplayer1.setVideoPath(path0);
                ijkplayer0.start();
                ijkplayer1.start();
            }
        }));

        ijkplayer2.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                //双击事件
                //Toast.makeText(ThreePullActivity.this,"ijkplayer2双击拉",Toast.LENGTH_LONG).show();
                String path0 = ijkplayer0.getVideoPath();
                String path2 = ijkplayer2.getVideoPath();
                Log.e("path0--",path0);
                Log.e("path2--",path2);
                ijkplayer0.stopPlayback();
                ijkplayer2.stopPlayback();
                ijkplayer0.setVideoPath(path2);
                ijkplayer2.setVideoPath(path0);
                ijkplayer0.start();
                ijkplayer2.start();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ijkplayer0.stopPlayback();
        ijkplayer1.stopPlayback();
        ijkplayer2.stopPlayback();

    }



}
