package com.uncle.administrator.fleamarket.Login;

import com.uncle.Base.BaseBindingActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.LoginActivityBinding;

import cn.bmob.sms.BmobSMS;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class LoginActivity extends BaseBindingActivity<LoginActivityBinding> {


    @Override
    protected void bindData(LoginActivityBinding dataBinding) {
        BmobSMS.initialize(LoginActivity.this, "144dbb1fbca09ce5d3af201a05c54628");
        LoginVM loginVM = new LoginVM(LoginActivity.this, binding);
        binding.setModule(loginVM);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }


}
