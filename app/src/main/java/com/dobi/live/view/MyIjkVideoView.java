package com.dobi.live.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dobi.live.Main1Activity;
import com.dobi.live.R;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.dobi.live.utils.ThreadUtils.TAG;

/**
 * Created by Admin on 2018/8/17.
 */

public class MyIjkVideoView extends RelativeLayout {


    IjkVideoView ijkplayer;
    ProgressBar progressbar;
    public static final String TAG = "MyIjkVideoView";

    String videoPath = null;

    public MyIjkVideoView(Context context) {
        this(context,null);
    }

    public MyIjkVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyIjkVideoView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        LayoutInflater layoutInflater = (LayoutInflater) context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.layout.my_ijkplayer, this);
        //父控件自己
         inflate(context, R.layout.my_ijkplayer,this);
        ijkplayer = findViewById(R.id.ijkplayer);
        progressbar = findViewById(R.id.progressbar);

        ijkplayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Toast.makeText(context,"播放出错啦",Toast.LENGTH_LONG).show();
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


    public void setVideoPath(String path) {
        videoPath = path;
        ijkplayer.setVideoPath(path);
    }

    public void start(){
        ijkplayer.start();
    }
    public void stopPlayback(){
      //  ijkplayer.stopPlayback();
      // ijkplayer.pause();
    }
    public void pause(){
        ijkplayer.pause();

    }
    public void onResume(){
        ijkplayer.onResume();

    }

    public void release(){
        ijkplayer.release(true);
}

    public String getVideoPath(){
        if(videoPath!=null){
            return videoPath;
        }else {
            return "";
        }
    }



}
