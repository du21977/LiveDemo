package com.dobi.live;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blueberry.media.Config;
import com.blueberry.media.MediaPublisher;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 用的那个没延时的
 */
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback2 {

    static  String PUSH = "rtmp://119.131.176.169/live/test2";
    static  String PULL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

    private static final String TAG = "MainActivity";

    //开始推送按钮
    private Button btnToggle;
    //显示视频
    private SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;

    private boolean isPublished;

    //音视频推送类
    private MediaPublisher mMediaPublisher;

    IjkVideoView ijkplayer;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Log.i(TAG, "onCreate: ");
        initView();
        //音视频参数初始化参数
        mMediaPublisher = MediaPublisher
                .newInstance(new Config.Builder()
                        .setFps(25) // fps
                        .setMaxWidth(320) //视频的最大宽度
                        .setMinWidth(240) //视频的最小宽度
                        .setUrl(PUSH)//推送的url
                        .build());
        //初始化视频采集器，音频采集器，视频编码器，音频编码器
        mMediaPublisher.init();

        initData();
    }

    /**
     * 播放拉流地址
     */
    private void initData() {
        PULL = SetupActivity.pull__;
        PUSH = SetupActivity.push__;
//        ijkplayer.setVideoPath(PULL);
//        ijkplayer.start();
        ijkplayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Toast.makeText(MainActivity.this,"播放出错啦",Toast.LENGTH_LONG).show();
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

    private void initView() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ijkplayer = (IjkVideoView) findViewById(R.id.ijkplayer);
        btnToggle = (Button) findViewById(R.id.btn_push);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPublish();
            }
        });
    }

    private void switchPublish() {
        if (isPublished) {
            stop();
            ijkplayer.stopPlayback();
        } else {
            start();
            ijkplayer.setVideoPath(PULL);
            ijkplayer.start();
        }
        btnToggle.setText(isPublished ? "停止推流" : "开始推流");
    }

    private void start() {
        //初始化声音采集
        mMediaPublisher.initAudioGatherer();
        //初始化编码器
        mMediaPublisher.initEncoders();
        //开始采集
        mMediaPublisher.startGather();
        //开始编码
        mMediaPublisher.startEncoder();
        //开始推送
        mMediaPublisher.starPublish();
        isPublished = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        mMediaPublisher.initVideoGatherer(this, mSurfaceHolder);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        stop();
    }

    private void stop() {
        mMediaPublisher.stopPublish();
        mMediaPublisher.stopGather();
        mMediaPublisher.stopEncoder();
        isPublished = false;
        ijkplayer.stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPublisher.release();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
        mMediaPublisher.initVideoGatherer(MainActivity.this, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
