package com.uncle.administrator.fleamarket.Login_Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class welcome_page extends Activity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.welcome_page);
        init();
    }

    private void init() {
        tv = (TextView) findViewById(R.id.welcome_tv);
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 3; i >= 0; i--) {
                    String time = String.valueOf(i);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = time;
                    handler.sendMessage(message);


                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    String time = (String) msg.obj;
                    tv.setText(time);
                    if (time.equals("0")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                        String account = sharedPreferences.getString("Login", "");
                        if (account.equals(null) || account.length() <= 0) {
                            Intent intent = new Intent(welcome_page.this, Login_activity_1.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(welcome_page.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;
            }


        }
    };
}