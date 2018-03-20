package com.uncle.administrator.fleamarket.Login_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.bomb.BOMBOpenHelper;
import com.uncle.administrator.fleamarket.DTO.User_account;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class Login_activity_2 extends Activity {

    private Button send_sms,next;
    private EditText phone_nub,verification;
    private String nub;
    private BOMBOpenHelper bomb_openhelper;
    private  ProgressDialog m_pDialog; //声明进度条对话框
    private String verification_nub;//验证码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity2);
        init();
    }

    private void init() {
        send_sms = (Button) findViewById(R.id.login_activity2_bt_send_sms);
        next = (Button) findViewById(R.id.login_activity2_bt_next);
        phone_nub = (EditText) findViewById(R.id.login_activity2_et_phone_nub);
        verification = (EditText) findViewById(R.id.login_activity2_et_verification);
        bomb_openhelper = new BOMBOpenHelper();
        BmobSMS.initialize(Login_activity_2.this,"144dbb1fbca09ce5d3af201a05c54628");
        Bmob.initialize(Login_activity_2.this,"144dbb1fbca09ce5d3af201a05c54628");
        send_sms_onclick();
        bt_next_click();
    }

    public void send_sms_onclick(){
        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 nub = phone_nub.getText().toString().trim();

               if (nub.length() == 0){
                   Toast.makeText(Login_activity_2.this,"电话号码不能为空",Toast.LENGTH_SHORT).show();
               }else if (nub.length() !=11 ){
                   Toast.makeText(Login_activity_2.this,"电话号码长度不对",Toast.LENGTH_SHORT).show();
               }else {
                   send_sms.setClickable(false);
                   send_sms.setTextColor(Color.parseColor("#F0FFFF"));

                   //发送号码到后台并判断返回验证码
                   BmobSMS.requestSMSCode(Login_activity_2.this, nub, "登录注册验证码",new RequestSMSCodeListener() {

                       @Override
                       public void done(Integer smsId,BmobException ex) {
                           // TODO Auto-generated method stub
                           if(ex==null){//验证码发送成功
                               Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                               new Thread(){
                                   @Override
                                   public void run() {//创造一个新线程，把button的点击事件设置为不可点击，并倒数60s
                                       super.run();
                                       for (int i = 120 ;i >=0 ;i--) {
                                           String time = String.valueOf(i);
                                           Message message = new Message();
                                           message.what = 555;
                                           message.obj = time;
                                           handler.sendMessage(message);
                                           try {
                                               Thread.sleep(1000);
                                           } catch (InterruptedException e) {
                                               e.printStackTrace();
                                           }

                                       }
                                   }
                               }.start();
                           }
                       }
                   });
               }
            }
        });
    }
    public void bt_next_click(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 verification_nub = verification.getText().toString().trim();
                if (verification_nub.length() == 0) {
                    Toast.makeText(Login_activity_2.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    wait_look();

                }

            }
        });
    }

    private void Login_next_button(){
        //判断验证码正确与否，如果正确，就转入程序并把账号密码存入shareperference里面
        //再初始化人名，获取账号的objectid

        BmobSMS.verifySmsCode(Login_activity_2.this,nub, verification_nub, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    bomb_openhelper.findAccount(nub, new BOMBOpenHelper.FindAccountCallback() {
                        @Override
                        public void onSuccess(List<User_account> list) {
                            User_account userAccount = list.get(0);
                            SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("account", nub);
                            editor.putString("object_id", userAccount.getObjectId());
                            editor.putString("nick_name", userAccount.getName());
                            editor.putString("college", userAccount.getCollege());
                            editor.putString("organization", userAccount.getOrganization());
                            editor.putString("Login", "success");
                            editor.putString("head_portrait",userAccount.getAvatar());
                            editor.putString("refresh","false");
                            editor.commit();
                            handler.sendEmptyMessage(0);
                            Intent intent = new Intent(Login_activity_2.this,MainActivity.class);
                            startActivity (intent );
                            finish();
                        }

                        @Override
                        public void onFail(int failCode) {//在数据库中找不到账号的，创建新的账号，并且登录跳转
                            if (failCode == 9015){
                                bomb_openhelper.add_account("无名氏",null,nub,"五邑大学","计算机学院",new BOMBOpenHelper.AddAccountCallback() {
                                    @Override
                                    public void onSuccess(String object) {
                                        String objextId =object;
                                        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("account", nub);
                                        editor.putString("object_id", objextId);
                                        editor.putString("head_portrait_adress",null);
                                        editor.putString("refresh","false");
                                        editor.commit();
                                        handler.sendEmptyMessage(0);
                                        Intent intent = new Intent(Login_activity_2.this,Login_activity3_setname.class);
                                        intent.putExtra("nub",nub);
                                        startActivity (intent );

                                    }
                                });

                            }
                        }
                    });

                }else{
                    handler.sendEmptyMessage(0);
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    if (ex.getErrorCode() == 207) {
                        Toast.makeText(Login_activity_2.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Login_activity_2.this, "验证失败", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }
    //菊花转圈圈
    private void wait_look(){
        m_pDialog= ProgressDialog.show(Login_activity_2.this, null, "验证中…");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Login_next_button();

            }
        }.start();

    }


    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (m_pDialog != null){
                m_pDialog.dismiss();// 关闭ProgressDialog
            }
            switch (msg.what){
                case 555:
                    String time = (String) msg.obj;
                    send_sms.setText("发送成功" +"("+time+")");
                    if (time.equals("0")){
                        send_sms.setClickable(true);
                        send_sms.setTextColor(Color.parseColor("#000000"));
                        send_sms.setText("发送");
                    }
                    break;
            }


        }
    };

}
