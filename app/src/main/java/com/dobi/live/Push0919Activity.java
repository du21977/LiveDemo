package com.dobi.live;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dobi.live.global.GlobalContants;
import com.dobi.live.pusher.LivePusher;
import com.dobi.live.view.MyIjkVideoView;
import com.dobi.live.view.OnDoubleClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Push0919Activity extends AppCompatActivity {

    private RelativeLayout rl_item_total;
    private MyIjkVideoView myIjkVideoView_1;
    private MyIjkVideoView myIjkVideoView_2;
//    private MyIjkVideoView[] myIjkVideoView_3;
    private SurfaceView surfaceView;
    private LivePusher live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_push);
        rl_item_total = (RelativeLayout) findViewById(R.id.rl_item_total);
        final List<MyIjkVideoView[]> list = new ArrayList<MyIjkVideoView[]>() ;
        final Map<String ,Object> myHashMap = new HashMap<>();
        //相机图像的预览
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
//        live = new LivePusher(surfaceView.getHolder());
        //在这里搞事情




        // 1.添加左边的播放器
        final RelativeLayout.LayoutParams   layoutParams_left = new RelativeLayout.LayoutParams(dp2px(400), RelativeLayout.LayoutParams.MATCH_PARENT);
//        myIjkVideoView_1 = new MyIjkVideoView[]{new MyIjkVideoView(this)};
        myIjkVideoView_1 = new MyIjkVideoView(this);
        myIjkVideoView_1.setBackgroundColor(Color.BLUE);
        myIjkVideoView_1.setLayoutParams(layoutParams_left);

        //2.添加右上角的播放器
        final RelativeLayout.LayoutParams layoutParams_right_top = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        myIjkVideoView_2 = new MyIjkVideoView(this);
        myIjkVideoView_2.setBackgroundColor(Color.BLACK);
        myIjkVideoView_2.setLayoutParams(layoutParams_right_top);


        //3.添加右下角的播放器
        final RelativeLayout.LayoutParams layoutParams_right_bottom = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        surfaceView = new SurfaceView(this);
//        surfaceView.setBackgroundColor(Color.GREEN);
        surfaceView.setLayoutParams(layoutParams_right_bottom);
        live = new LivePusher(surfaceView.getHolder());
        rl_item_total.addView(surfaceView);



        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        textView1.setLayoutParams(layoutParams_right_top);
        textView2.setLayoutParams(layoutParams_right_bottom);

        rl_item_total.addView(myIjkVideoView_1);
        rl_item_total.addView(myIjkVideoView_2);
//        rl_item_total.addView(surfaceView);
        rl_item_total.addView(textView1);
        rl_item_total.addView(textView2);
//        live = new LivePusher(surfaceView.getHolder());

//        list.add(myIjkVideoView_1);
//        list.add(myIjkVideoView_2);
//        list.add(myIjkVideoView_3);

        myHashMap.put("1",myIjkVideoView_1);
        myHashMap.put("2",myIjkVideoView_2);
        myHashMap.put("3",surfaceView);

//        String PULL_0 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//        String PULL_1 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//        String PULL_2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        myIjkVideoView_1.setVideoPath(GlobalContants.La1_URL);
        myIjkVideoView_2.setVideoPath(GlobalContants.La2_URL);
//        myIjkVideoView_3[0].setVideoPath(PULL_2);

        myIjkVideoView_1.start();
        myIjkVideoView_2.start();
//        myIjkVideoView_3[0].start();

        myIjkVideoView_1.SetVolumeListener(new MyIjkVideoView.VolListener() {
            @Override
            public void setVol() {
                myIjkVideoView_1.setVolume(20.0f,20.0f);
            }
        });
        myIjkVideoView_2.SetVolumeListener(new MyIjkVideoView.VolListener() {
            @Override
            public void setVol() {
                myIjkVideoView_2.setVolume(0.0f,0.0f);
            }
        });

        textView1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                //  Toast.makeText(ThreePull2Activity.this,"双击11",Toast.LENGTH_LONG).show();

//                if(myHashMap.get("1")==myIjkVideoView_1&&myHashMap.get("2")==myIjkVideoView_2){
//                    myHashMap.put("2",myIjkVideoView_1);
//                    myHashMap.put("1",myIjkVideoView_2);
//                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
//                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
//                }else if(myHashMap.get("1")==myIjkVideoView_2&&myHashMap.get("2")==myIjkVideoView_1){
//                    myHashMap.put("1",myIjkVideoView_1);
//                    myHashMap.put("2",myIjkVideoView_2);
//                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
//                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
//                }

                if(myHashMap.get("1")==myIjkVideoView_1&&myHashMap.get("2")==myIjkVideoView_2){
                    myHashMap.put("2",myIjkVideoView_1);
                    myHashMap.put("1",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }else if(myHashMap.get("1")==myIjkVideoView_2&&myHashMap.get("2")==myIjkVideoView_1){
                    myHashMap.put("1",myIjkVideoView_1);
                    myHashMap.put("2",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }

                if(myHashMap.get("1")==myIjkVideoView_1&&myHashMap.get("2")==surfaceView){
                    myHashMap.put("1",surfaceView);
                    myHashMap.put("2",myIjkVideoView_1);
                    ((SurfaceView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }else if(myHashMap.get("1")==surfaceView&&myHashMap.get("2")==myIjkVideoView_1){
                    myHashMap.put("2",surfaceView);
                    myHashMap.put("1",myIjkVideoView_1);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((SurfaceView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }

                if(myHashMap.get("1")==surfaceView&&myHashMap.get("2")==myIjkVideoView_2){
                    myHashMap.put("2",surfaceView);
                    myHashMap.put("1",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((SurfaceView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }else if(myHashMap.get("1")==myIjkVideoView_2&&myHashMap.get("2")==surfaceView){
                    myHashMap.put("1",surfaceView);
                    myHashMap.put("2",myIjkVideoView_2);
                    ((SurfaceView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("2")).setLayoutParams(layoutParams_right_top);
                }


            }
        }));

        textView2.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                //  Toast.makeText(ThreePull2Activity.this,"双击11",Toast.LENGTH_LONG).show();

                if(myHashMap.get("1")==myIjkVideoView_1&&myHashMap.get("3")==myIjkVideoView_2){
                    myHashMap.put("3",myIjkVideoView_1);
                    myHashMap.put("1",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }else if(myHashMap.get("1")==myIjkVideoView_2&&myHashMap.get("3")==myIjkVideoView_1){
                    myHashMap.put("1",myIjkVideoView_1);
                    myHashMap.put("3",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }

                if(myHashMap.get("1")==myIjkVideoView_1&&myHashMap.get("3")==surfaceView){
                    myHashMap.put("1",surfaceView);
                    myHashMap.put("3",myIjkVideoView_1);
                    ((SurfaceView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }else if(myHashMap.get("1")==surfaceView&&myHashMap.get("3")==myIjkVideoView_1){
                    myHashMap.put("3",surfaceView);
                    myHashMap.put("1",myIjkVideoView_1);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((SurfaceView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }

                if(myHashMap.get("1")==surfaceView&&myHashMap.get("3")==myIjkVideoView_2){
                    myHashMap.put("3",surfaceView);
                    myHashMap.put("1",myIjkVideoView_2);
                    ((MyIjkVideoView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((SurfaceView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }else if(myHashMap.get("1")==myIjkVideoView_2&&myHashMap.get("3")==surfaceView){
                    myHashMap.put("1",surfaceView);
                    myHashMap.put("3",myIjkVideoView_2);
                    ((SurfaceView)myHashMap.get("1")).setLayoutParams(layoutParams_left);
                    ((MyIjkVideoView)myHashMap.get("3")).setLayoutParams(layoutParams_right_bottom);
                }

            }
        }));

        surfaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("长安---","-----");
                    live.switchCamera();
                return false;
            }
        });

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
        live.switchCamera();
    }

    public  int dp2px( float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
