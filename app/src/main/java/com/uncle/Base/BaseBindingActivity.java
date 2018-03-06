package com.uncle.Base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author nnv
 * @date 2017/7/19
 */

public abstract class BaseBindingActivity<T extends ViewDataBinding> extends Activity {

    protected T binding;
    private boolean isCreated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        bindData(binding);
    }


    protected abstract void bindData(T dataBinding);

    protected abstract int getLayoutId();
}
