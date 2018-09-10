package com.dobi.live;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dobi.live.pusher.LivePusher;
import com.dobi.live.view.MyIjkVideoView;
import com.dobi.live.view.OnDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

public class PushActivity extends AppCompatActivity {

    private RelativeLayout rl_item_total;
    private MyIjkVideoView[] myIjkVideoView_1;
    private MyIjkVideoView[] myIjkVideoView_2;
//    private MyIjkVideoView[] myIjkVideoView_3;
//    private SurfaceView surfaceView;
    private LivePusher live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        rl_item_total = (RelativeLayout) findViewById(R.id.rl_item_total);
        final List<MyIjkVideoView[]> list = new ArrayList<MyIjkVideoView[]>() ;
        //相机图像的预览
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        live = new LivePusher(surfaceView.getHolder());
        //在这里搞事情
        // 1.添加左边的播放器
        final RelativeLayout.LayoutParams   layoutParams_left = new RelativeLayout.LayoutParams(dp2px(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        myIjkVideoView_1 = new MyIjkVideoView[]{new MyIjkVideoView(this)};
        myIjkVideoView_1[0].setBackgroundColor(Color.BLUE);
        myIjkVideoView_1[0].setLayoutParams(layoutParams_left);

        //2.添加右上角的播放器
        final RelativeLayout.LayoutParams layoutParams_right_top = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        myIjkVideoView_2 = new MyIjkVideoView[]{new MyIjkVideoView(this)};
        myIjkVideoView_2[0].setBackgroundColor(Color.BLACK);
        myIjkVideoView_2[0].setLayoutParams(layoutParams_right_top);



        //3.添加右下角的播放器
//        final RelativeLayout.LayoutParams layoutParams_right_bottom = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
//        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        myIjkVideoView_3 = new MyIjkVideoView[]{new MyIjkVideoView(this)};
//        myIjkVideoView_3[0].setBackgroundColor(Color.BLACK);
//        myIjkVideoView_3[0].setLayoutParams(layoutParams_right_bottom);

        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        textView1.setLayoutParams(layoutParams_right_top);
//        textView2.setLayoutParams(layoutParams_right_bottom);

        rl_item_total.addView(myIjkVideoView_1[0]);
        rl_item_total.addView(myIjkVideoView_2[0]);
//        rl_item_total.addView(myIjkVideoView_3[0]);
        rl_item_total.addView(textView1);
        rl_item_total.addView(textView2);

        list.add(myIjkVideoView_1);
        list.add(myIjkVideoView_2);
//        list.add(myIjkVideoView_3);

        String PULL_0 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        String PULL_1 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        String PULL_2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        myIjkVideoView_1[0].setVideoPath(PULL_1);
        myIjkVideoView_2[0].setVideoPath(PULL_0);
//        myIjkVideoView_3[0].setVideoPath(PULL_2);

        myIjkVideoView_1[0].start();
        myIjkVideoView_2[0].start();
//        myIjkVideoView_3[0].start();

        myIjkVideoView_1[0].SetVolumeListener(new MyIjkVideoView.VolListener() {
            @Override
            public void setVol() {
                myIjkVideoView_1[0].setVolume(20.0f,20.0f);
            }
        });
        myIjkVideoView_2[0].SetVolumeListener(new MyIjkVideoView.VolListener() {
            @Override
            public void setVol() {
                myIjkVideoView_2[0].setVolume(0.0f,0.0f);
            }
        });

        textView1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                //  Toast.makeText(ThreePull2Activity.this,"双击11",Toast.LENGTH_LONG).show();

                MyIjkVideoView myIjkVideoView = new MyIjkVideoView(PushActivity.this);
                myIjkVideoView = list.get(0)[0];
                list.get(0)[0] =list.get(1)[0];
                list.get(1)[0] = myIjkVideoView;

                list.get(0)[0].setLayoutParams(layoutParams_left);
                list.get(1)[0].setLayoutParams(layoutParams_right_top);
                list.get(0)[0].setVolume(20.0f,20.0f);
                list.get(1)[0].setVolume(0.0f,0.0f);
            }
        }));

    }

    /**
     * 开始直播
     * @param
     */
    public void mStartLive(View view) {

    }

    /**
     * 切换摄像头
     * @param btn
     */
    public void mSwitchCamera(View btn) {
//        live.switchCamera();
    }

    public  int dp2px( float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
