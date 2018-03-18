package com.uncle.Base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 *
 * @author nnv
 * @date 2017/7/19
 */

    public abstract class BaseBindingActivity<T extends ViewDataBinding> extends Activity {

    protected T binding;
    private boolean isCreated = false;
    public String TAG = this.getClass().getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        bindData(binding);
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }
    protected abstract void bindData(T dataBinding);

    protected abstract int getLayoutId();
}
