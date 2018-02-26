package com.uncle.administrator.fleamarket.Login_Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uncle.administrator.fleamarket.R;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class Login_activity_1 extends Activity {

    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity1);
        init();
    }

    private void init() {
        login = (Button) findViewById(R.id.login_activity1_login);

        Login_onclick();
    }

    public void Login_onclick(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity_1.this,Login_activity_2.class);
                startActivity(intent);


            }
        });
    }

}
