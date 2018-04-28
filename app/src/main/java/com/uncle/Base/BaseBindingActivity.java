package com.uncle.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.uncle.DTO.Profile;

/**
 * @author nnv
 * @date 2017/7/19
 */

public abstract class BaseBindingActivity<T extends ViewDataBinding> extends Activity {

    protected T binding;
    protected Profile profile;
    public String TAG = this.getClass().getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyAccountFromSharePerFences();
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        bindData(binding);
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName())) {
            return getIntent().getBundleExtra(getPackageName());
        }
        return null;
    }

    private void getMyAccountFromSharePerFences() {
        SharedPreferences sp = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        String string = sp.getString("profile", null);
        if (string != null) {
            profile = new Gson().fromJson(string, Profile.class);
        }
    }

    public void saveMyAccountFromSharePerFences(Profile profile){
        SharedPreferences sharedPreferences = this.getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile",new Gson().toJson(profile));
        editor.apply();
        this.profile = profile;
    }

    protected abstract void bindData(T dataBinding);

    protected abstract int getLayoutId();
}
