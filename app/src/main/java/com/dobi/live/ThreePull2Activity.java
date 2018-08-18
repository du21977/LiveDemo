package com.dobi.live;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.live.view.MyIjkVideoView;
import com.dobi.live.view.OnDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

public class ThreePull2Activity extends AppCompatActivity {


    int left = 0;
    boolean leftTop = true;
    boolean rightBottom = true;
    private RelativeLayout.LayoutParams layoutParams_left;
    private RelativeLayout.LayoutParams layoutParams_right_top;
    private RelativeLayout.LayoutParams layoutParams_right_bottom;

    List<MyIjkVideoView[]> list = new ArrayList<MyIjkVideoView[]>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
      //  layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(this);
        textView.setText("hhhhh");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(60);
        textView.setLayoutParams(layoutParams);
        //relativeLayout.addView(textView);

       // 1.添加左边的播放器
        layoutParams_left = new RelativeLayout.LayoutParams(dp2px(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        final MyIjkVideoView[] myIjkVideoView_1 = {new MyIjkVideoView(this)};
        myIjkVideoView_1[0].setBackgroundColor(Color.BLUE);
        myIjkVideoView_1[0].setLayoutParams(layoutParams_left);


        //2.添加右上角的播放器
        layoutParams_right_top = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        final MyIjkVideoView[] myIjkVideoView_2 = {new MyIjkVideoView(this)};
        myIjkVideoView_2[0].setBackgroundColor(Color.WHITE);
        myIjkVideoView_2[0].setLayoutParams(layoutParams_right_top);

        //3.添加右下角的播放器
        layoutParams_right_bottom = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams_right_bottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        final MyIjkVideoView[] myIjkVideoView_3 = {new MyIjkVideoView(this)};
        myIjkVideoView_3[0].setBackgroundColor(Color.BLACK);
        myIjkVideoView_3[0].setLayoutParams(layoutParams_right_bottom);

        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        textView1.setLayoutParams(layoutParams_right_top);
        textView2.setLayoutParams(layoutParams_right_bottom);

        relativeLayout.addView(myIjkVideoView_1[0]);
        relativeLayout.addView(myIjkVideoView_2[0]);
        relativeLayout.addView(myIjkVideoView_3[0]);
        relativeLayout.addView(textView1);
        relativeLayout.addView(textView2);

        list.add(myIjkVideoView_1);
        list.add(myIjkVideoView_2);
        list.add(myIjkVideoView_3);
        setContentView(relativeLayout);
//        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dp2px(100), dp2px(100));
//        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textView.setLayoutParams(layoutParams1);
//        textView.setText("8888");



//        setContentView(R.layout.activity_three_pull2);



        myIjkVideoView_1[0].setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        myIjkVideoView_2[0].setVideoPath("rtmp://202.69.69.180:443/webcast/bshdlive-pc");
        myIjkVideoView_3[0].setVideoPath("rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp");

        myIjkVideoView_1[0].start();
        myIjkVideoView_2[0].start();
        myIjkVideoView_3[0].start();

        textView1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                Toast.makeText(ThreePull2Activity.this,"双击11",Toast.LENGTH_LONG).show();

                MyIjkVideoView myIjkVideoView = new MyIjkVideoView(ThreePull2Activity.this);
                myIjkVideoView = list.get(0)[0];
                list.get(0)[0] =list.get(1)[0];
                list.get(1)[0] = myIjkVideoView;

                list.get(0)[0].setLayoutParams(layoutParams_left);
                list.get(1)[0].setLayoutParams(layoutParams_right_top);



//                myIjkVideoView = myIjkVideoView_1[0];
//
//                myIjkVideoView_1[0] = myIjkVideoView_2[0];
//                myIjkVideoView_2[0] = myIjkVideoView;
//                myIjkVideoView_1[0].setLayoutParams(layoutParams_left);
//                myIjkVideoView_2[0].setLayoutParams(layoutParams_right_top);




            }
        }));

        textView2.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                Toast.makeText(ThreePull2Activity.this,"双击22",Toast.LENGTH_LONG).show();
                MyIjkVideoView myIjkVideoView = new MyIjkVideoView(ThreePull2Activity.this);
                myIjkVideoView = list.get(0)[0];
                list.get(0)[0] =list.get(2)[0];
                list.get(2)[0] = myIjkVideoView;

                list.get(0)[0].setLayoutParams(layoutParams_left);
                list.get(2)[0].setLayoutParams(layoutParams_right_bottom);

            }
        }));


        /*
        myIjkVideoView_2.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
//                RelativeLayout.LayoutParams layoutParams_right_top = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
//                layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                RelativeLayout.LayoutParams layoutParams_left = new RelativeLayout.LayoutParams(dp2px(400), RelativeLayout.LayoutParams.MATCH_PARENT);
                left = 1;
                Log.e("leftTop----",leftTop+"");
                if(leftTop){
                    //左边到右上角
                    myIjkVideoView_1.setLayoutParams(layoutParams_right_top);
                    //右上角到左边
                    myIjkVideoView_2.setLayoutParams(layoutParams_left);
                    leftTop = false;
                }else {
//                    myIjkVideoView_1.setLayoutParams(layoutParams_left);
//                    myIjkVideoView_2.setLayoutParams(layoutParams_right_top);
                    leftTop = true;
                }


            }
        }));

        myIjkVideoView_3.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
//                RelativeLayout.LayoutParams layoutParams_right_top = new RelativeLayout.LayoutParams(dp2px(200), dp2px(200));
//                layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                layoutParams_right_top.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                RelativeLayout.LayoutParams layoutParams_left = new RelativeLayout.LayoutParams(dp2px(400), RelativeLayout.LayoutParams.MATCH_PARENT);
                left = 2;
                Log.e("rightBottom----",rightBottom+"");
                if(rightBottom){
                    //左边到右下角
                    myIjkVideoView_1.setLayoutParams(layoutParams_right_bottom);
                    //右下角到左边
                    myIjkVideoView_3.setLayoutParams(layoutParams_left);
                    rightBottom = false;
                }else {
//                    myIjkVideoView_1.setLayoutParams(layoutParams_left);
//                    myIjkVideoView_3.setLayoutParams(layoutParams_right_bottom);
                    rightBottom = true;
                }


            }
        }));


        myIjkVideoView_1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {

                if(left ==1){
                    myIjkVideoView_1.setLayoutParams(layoutParams_left);
                    myIjkVideoView_2.setLayoutParams(layoutParams_right_top);
                }else if(left ==2){
                    myIjkVideoView_1.setLayoutParams(layoutParams_left);
                    myIjkVideoView_3.setLayoutParams(layoutParams_right_bottom);
                }

            }
        }));

        */



    }



    public  int dp2px( float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
