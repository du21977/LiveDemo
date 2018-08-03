package com.dobi.live;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends AppCompatActivity {

    EditText et_push;
    EditText et_pull;
    Button btn_enter;
    static  String push__ = "rtmp://119.131.176.169/live/test2";
    static  String pull__ = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        et_push = (EditText) findViewById(R.id.et_push);
        et_pull = (EditText) findViewById(R.id.et_pull);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push__ = et_push.getText().toString().trim();
                pull__ = et_pull.getText().toString().trim();
                startActivity(new Intent(SetupActivity.this,MainActivity.class));
            }
        });
    }
}
