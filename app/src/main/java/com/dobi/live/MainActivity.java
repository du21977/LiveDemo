package com.dobi.live;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.live.jni.PushNative;
import com.dobi.live.listener.LiveStateChangeListener;
import com.dobi.live.pusher.LivePusher;
import com.yinglian.baselibrary.ijkplayer.widget.IjkVideoView;

/**
 * 在jason基础上自己修改
 */
public class MainActivity extends AppCompatActivity implements LiveStateChangeListener {

    static final String URL = "rtmp://119.131.176.169/live/test2";

    static final String PULL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    //static final String URL = "rtmp://192.168.0.122/live/jason";
    //static final String URL = "rtmp://112.74.96.116/live/jason";
    //jason老师的 对应  http://dongnaoedu.com:9080
    //static final String URL = "rtmp://139.196.34.133/live/jason";
    private LivePusher live;


    IjkVideoView ijkplayer;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PushNative.CONNECT_FAILED:
                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    //Log.d("jason", "连接失败..");
                    break;
                case PushNative.INIT_FAILED:
                    Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
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
        ijkplayer = (IjkVideoView) findViewById(R.id.ijkplayer);
        //System.out.println("isInMainThread()=" + ThreadUtils.isInMainThread());//在这里调用一下方法

        //相机图像的预览
        live = new LivePusher(surfaceView.getHolder());

        ijkplayer.setVideoPath(PULL);
        ijkplayer.start();
    }

    /**
     * 开始直播
     * @param
     */
    public void mStartLive(View view) {
        Button btn = (Button)view;
        if(btn.getText().equals("开始直播")){
            live.startPush(URL,this);
            btn.setText("停止直播");
        }else{
            live.stopPush();
            btn.setText("开始直播");
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
}