package com.dobi.live;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dobi.live.jni.PushNative;
import com.dobi.live.listener.LiveStateChangeListener;
import com.dobi.live.pusher.LivePusher;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 在jason基础上自己修改
 */
public class Main1Activity extends AppCompatActivity implements LiveStateChangeListener {
    private static final String TAG = "Main1Activity-Video";
    static  String PUSH = "rtmp://119.131.176.169/live/test2";

    static  String PULL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private LivePusher live;


    IjkVideoView ijkplayer;
    ProgressBar progressbar;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PushNative.CONNECT_FAILED:
                    Toast.makeText(Main1Activity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    //Log.d("jason", "连接失败..");
                    break;
                case PushNative.INIT_FAILED:
                    Toast.makeText(Main1Activity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        //DragSurfaceView surfaceView = (DragSurfaceView) findViewById(R.id.surface);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ijkplayer = (IjkVideoView) findViewById(R.id.ijkplayer);
        //System.out.println("isInMainThread()=" + ThreadUtils.isInMainThread());//在这里调用一下方法

        //相机图像的预览
        live = new LivePusher(surfaceView.getHolder());

        PULL = SetupActivity.pull__;
        PUSH = SetupActivity.push__;
        ijkplayer.setVideoPath(PULL);
        ijkplayer.start();

        ijkplayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Toast.makeText(Main1Activity.this,"播放出错啦",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        ijkplayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int arg1, int arg2) {
                Log.e("haha--onInfo", arg1 + "");
                switch (arg1) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        Log.e(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        Log.e(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                        progressbar.setVisibility(View.GONE);
                        ijkplayer.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:    //开始了缓冲，卡了
                        Log.e(TAG, "MEDIA_INFO_BUFFERING_START:");
                        progressbar.setVisibility(View.VISIBLE);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:     //缓冲结束，卡结束
                        Log.e(TAG, "MEDIA_INFO_BUFFERING_END:");
                        progressbar.setVisibility(View.GONE);
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        Log.e(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        Log.e(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        Log.e(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        Log.e(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                        Log.e(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                        Log.e(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:

                        Log.e(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);

                        break;
                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        Log.e(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                        break;
                }
                return false;
            }
        });
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
        }else{
            live.stopPush();
            btn.setText("开始推流");
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
        ijkplayer.stopPlayback();
    }
}
