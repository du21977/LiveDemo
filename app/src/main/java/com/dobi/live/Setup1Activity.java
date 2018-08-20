package com.dobi.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Setup1Activity extends AppCompatActivity {

    EditText et_pull0;
    EditText et_pull1;
    EditText et_pull2;
    Button btn_enter;
    static  String pull__0 = "rtmp://119.131.176.169/live/test2";
    static  String pull__1 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    static  String pull__2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        et_pull0 = (EditText) findViewById(R.id.et_pull0);
        et_pull1 = (EditText) findViewById(R.id.et_pull1);
        et_pull2 = (EditText) findViewById(R.id.et_pull2);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pull__0 = et_pull0.getText().toString().trim();
                pull__1 = et_pull1.getText().toString().trim();
                pull__2 = et_pull2.getText().toString().trim();
                startActivity(new Intent(Setup1Activity.this,ThreePull2Activity.class));
            }
        });
    }
}
