package com.uncle.administrator.fleamarket.Login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uncle.DTO.Profile;
import com.uncle.Util.DialogUtil;
import com.uncle.Util.SPUtil;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.databinding.LoginActivityBinding;
import com.uncle.bomb.BOMBOpenHelper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * @author Administrator
 * @date 2018/4/15 0015
 */

public class LoginVM {
    private static final String nubPantem = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    private static final int SEND_SMS_SUCCESS = 0;
    private LoginActivityBinding binding;
    private LoginHandler loginHandler;
    private String phoneNub;
    private LoginActivity activity;

    public LoginVM(LoginActivity activity, LoginActivityBinding binding) {
        this.activity = activity;
        this.binding = binding;
        loginHandler = new LoginHandler();
        loginHandler.setBtCode(new WeakReference<TextView>(binding.tvGetCode));
    }

    public void sendSMSMessage(View view) {
        phoneNub = binding.etNub.getText().toString();
        if (TextUtils.isEmpty(phoneNub)) {
            ToastUtil.show(activity, "请输入电话号码");
            return;
        }
        Pattern pattern = Pattern.compile(nubPantem);
        Matcher matcher = pattern.matcher(phoneNub);
        if (!matcher.matches()) {
            ToastUtil.show(activity, "请输入正确的电话号码");
            return;
        }
        sendSms(phoneNub);
    }

    public void login(View view) {
        String code = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(activity, "请出入验证码");
            return;
        }
        DialogUtil.getInstance(activity).show();
        BmobSMS.verifySmsCode(activity, phoneNub, code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    findUserLogined();
                    return;
                }
                ToastUtil.show(activity, ex.toString());
                DialogUtil.getInstance(activity).dismiss();
            }
        });
    }

    public void deleteNub(View view) {
        binding.etNub.setText("");
    }


    private void findUserLogined() {
        BOMBOpenHelper.getInstance().findAccount(phoneNub, new BOMBOpenHelper.FindAccountCallback() {
            @Override
            public void onSuccess(List<Profile> list) {
                DialogUtil.getInstance(activity).dismiss();
                ToastUtil.show(activity, "验证成功");
                Profile profile = list.get(0);
                SPUtil.getInstance(activity).saveSP("profile", new Gson().toJson(profile));
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onFail(int failCode) {
                //数据库找不到数据
                DialogUtil.getInstance(activity).dismiss();
                if (failCode == 9015) {
                    Intent intent = new Intent(activity, SetUesrDataActivity.class);
                    intent.putExtra("phoneNub", phoneNub);
                    activity.startActivity(intent);
                    return;
                }
                ToastUtil.show(activity, "验证失败，请重试");
            }
        });
    }


    /**
     * 发送号码到后台并判断返回验证码
     */
    private void sendSms(String nub) {
        BmobSMS.requestSMSCode(activity, nub, "登录注册验证码", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {
                    loginHandler.sendEmptyMessage(SEND_SMS_SUCCESS);
                    return;
                }
                if (ex.getErrorCode() == 10010) {
                    ToastUtil.show(activity, "操作过于频繁，请稍后再试");
                    return;
                }
                ToastUtil.show(activity, ex.toString());
            }
        });
    }

    private static class LoginHandler extends Handler {
        private int time = 120;
        private WeakReference<TextView> weakReference;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TextView txCode = weakReference.get();
            if (time >= 0) {
                txCode.setText(time + "s后重新获取");
                txCode.setClickable(false);
                time--;
                sendEmptyMessageDelayed(SEND_SMS_SUCCESS, 1000);
                return;
            }
            txCode.setText("获取验证码");
            txCode.setClickable(true);
        }

        void setBtCode(WeakReference<TextView> btCode) {
            this.weakReference = btCode;
        }
    }
}
