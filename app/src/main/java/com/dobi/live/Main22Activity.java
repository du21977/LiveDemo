package com.dobi.live;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dobi.live.jni.PushNative;
import com.dobi.live.listener.LiveStateChangeListener;
import com.dobi.live.pusher.LivePusher;
import com.dobi.live.video.EmptyControlVideo;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 在jason基础上自己修改
 */
public class Main22Activity extends AppCompatActivity implements LiveStateChangeListener {
    private static final String TAG = "Main1Activity-Video";
    static  String PUSH = "rtmp://119.131.176.169/live/test2";

    static  String PULL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private LivePusher live;

    EmptyControlVideo gsyplayer;
    //IjkVideoView ijkplayer;
    ProgressBar progressbar;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PushNative.CONNECT_FAILED:
                    Toast.makeText(Main22Activity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    //Log.d("jason", "连接失败..");
                    break;
                case PushNative.INIT_FAILED:
                    Toast.makeText(Main22Activity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_22);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        //DragSurfaceView surfaceView = (DragSurfaceView) findViewById(R.id.surface);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        //ijkplayer = (IjkVideoView) findViewById(R.id.ijkplayer);
        //System.out.println("isInMainThread()=" + ThreadUtils.isInMainThread());//在这里调用一下方法
        gsyplayer = (EmptyControlVideo) findViewById(R.id.gsyplayer);
        //相机图像的预览
        live = new LivePusher(surfaceView.getHolder());

        PULL = SetupActivity.pull__;
        PUSH = SetupActivity.push__;
//        gsyplayer.setUp(PULL, true, "");
//        gsyplayer.startPlayLogic();
    }

    /**
     * 开始直播
     * @param
     */
    public void mStartLive(View view) {
        Button btn = (Button)view;
        if(btn.getText().equals("开始推流")){
            live.startPush(PUSH,this);
            btn.setText("停止推流");
            gsyplayer.setUp(PULL, true, "");
            gsyplayer.startPlayLogic();
        }else{
            live.stopPush();
            btn.setText("开始推流");
            gsyplayer.setStandardVideoAllCallBack(null);
        }
    }

    /**
     * 切换摄像头
     * @param btn
     */
    public void mSwitchCamera(View btn) {
        live.switchCamera();
    }

    //改方法执行仍然在子线程中，发送消息到UI主线程
    @Override
    public void onError(int code) {
        handler.sendEmptyMessage(code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放所有
        gsyplayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
    }
}
