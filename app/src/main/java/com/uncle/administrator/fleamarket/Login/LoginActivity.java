package com.uncle.administrator.fleamarket.Login;

import com.uncle.Base.BaseBindingActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.LoginActivityBinding;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class LoginActivity extends BaseBindingActivity<LoginActivityBinding> {


    @Override
    protected void bindData(LoginActivityBinding dataBinding) {
        LoginVM loginVM = new LoginVM(LoginActivity.this, binding);
        binding.setModule(loginVM);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }


}
