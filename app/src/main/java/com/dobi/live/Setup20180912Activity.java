package com.dobi.live;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dobi.live.global.GlobalContants;

import java.util.HashMap;
import java.util.Map;

public class Setup20180912Activity extends AppCompatActivity {

    EditText et_push;
    EditText et_pull1;
    EditText et_pull2;
    EditText et_pull3;
    TextView tv_play;

    Map<String,Object> myMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup20180912);
        et_push = (EditText) findViewById(R.id.et_push);
        et_pull1 = (EditText) findViewById(R.id.et_pull1);
        et_pull2 = (EditText) findViewById(R.id.et_pull2);
        et_pull3 = (EditText) findViewById(R.id.et_pull3);
        tv_play = (TextView) findViewById(R.id.tv_play);

        tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalContants.Tui_URL = et_push.getText().toString().trim();
                GlobalContants.La1_URL = et_pull1.getText().toString().trim();
                GlobalContants.La2_URL = et_pull2.getText().toString().trim();
//                startActivity(new Intent(Setup20180912Activity.this,PushActivity.class));
                startActivity(new Intent(Setup20180912Activity.this,Push0919Activity.class));
//                myMap.put("1","haha");
//                Log.e("1--",myMap.get("1")+"");
//                Log.e("size--",myMap.size()+"");
//                myMap.put("2","网吧");
//                Log.e("2--",myMap.get("2")+"");
//                Log.e("size--",myMap.size()+"");
//                myMap.put("1",666);
//                Log.e("1--",myMap.get("1")+"");
//                myMap.put("1","haha");
//                Log.e("size--",myMap.size()+"");

            }
        });
    }
}
