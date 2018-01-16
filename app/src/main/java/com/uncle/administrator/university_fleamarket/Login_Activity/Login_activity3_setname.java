package com.uncle.administrator.university_fleamarket.Login_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uncle.administrator.university_fleamarket.MainActivity;
import com.uncle.administrator.university_fleamarket.R;
import com.uncle.bomb.BOMB_openhelper;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class Login_activity3_setname extends Activity {
    private EditText editText;
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private Button button;
    private Intent intent;
    private String nub;
    private BOMB_openhelper bomb;
    private String college, organization;
    private String[] mItems1, mItems2, mItems3, mItems4;
    private ProgressDialog m_pDialog; //声明进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity3_setname);
        init();

    }

    private void init() {
        editText = (EditText) findViewById(R.id.login_activity3_edittext);
        spinner1 = (Spinner) findViewById(R.id.login_activity3_spinner1);
        spinner2 = (Spinner) findViewById(R.id.login_activity3_spinner2);
        spinner3 = (Spinner) findViewById(R.id.login_activity3_spinner3);
        spinner4 = (Spinner) findViewById(R.id.login_activity3_spinner4);
        button = (Button) findViewById(R.id.login_activity3_button);
        intent = getIntent();
        nub = intent.getStringExtra("nub");
        bomb = new BOMB_openhelper();

        mItems1 = getResources().getStringArray(R.array.spinnername1);
        mItems2 = getResources().getStringArray(R.array.spinnername2);
        mItems3 = getResources().getStringArray(R.array.spinnername3);
        mItems4 = getResources().getStringArray(R.array.spinnername4);

        ArrayAdapter<String> _Adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems1);
        ArrayAdapter<String> _Adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems2);
        ArrayAdapter<String> _Adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems3);
        ArrayAdapter<String> _Adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems4);
        spinner1.setAdapter(_Adapter1);
        spinner2.setAdapter(_Adapter2);
        spinner3.setAdapter(_Adapter3);
        spinner4.setAdapter(_Adapter4);

        spinner_setOnItemSelectedListener();
        bt_click();
    }

    private void bt_click() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wait_look();
            }
        });
    }

    private void Login_set_data() {
        if (editText.getText().toString().length() == 0) {
            Toast.makeText(Login_activity3_setname.this, "请输入你的昵称", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(0);
        } else {
            String name = editText.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nick_name", name);
            editor.putString("Login", "success");
            editor.putString("college", college);
            editor.putString("organization", organization);
            editor.commit();

            bomb.Login_update_name(nub, name);
            bomb.Login_update_school(nub, college, organization, new BOMB_openhelper.Login_update_school_callback() {
                @Override
                public void done() {
                    handler.sendEmptyMessage(0);
                    Intent intent = new Intent(Login_activity3_setname.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    //菊花转圈圈
    private void wait_look() {
        m_pDialog = ProgressDialog.show(Login_activity3_setname.this, null, "登录中…");
        new Thread() {
            @Override
            public void run() {
                super.run();
                Login_set_data();

            }
        }.start();

    }


    private void spinner_setOnItemSelectedListener() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });//省

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });//市

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                college = mItems3[position];
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                college = mItems3[0];
                parent.setVisibility(View.VISIBLE);
            }
        });//学校

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization = mItems4[position];
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                organization = mItems4[0];
                parent.setVisibility(View.VISIBLE);
            }
        });//学院
    }


    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            m_pDialog.dismiss();// 关闭ProgressDialog

        }
    };
}
