package com.uncle.Base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.uncle.DTO.Profile;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.Login.LoginActivity;

/**
 * @author nnv
 * @date 2017/7/19
 */

public abstract class BaseBindingFragment<T extends ViewDataBinding> extends Fragment {

    protected T binding;
    private boolean isCreated = false;
    protected Profile profile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        getMyAccountFromSharePerFences();
        bindData(binding);
        return binding.getRoot();
    }

    private void getMyAccountFromSharePerFences() {
        SharedPreferences sp = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String string = sp.getString("profile", null);
        if (string != null) {
            profile = new Gson().fromJson(string, Profile.class);
            return;
        }
        ToastUtil.show(getContext(), "登录过期，请重新登录");
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(intent);
    }

    public void saveMyAccountFromSharePerFences(Profile myAccount) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile", new Gson().toJson(myAccount));
        editor.commit();
    }

    protected abstract void bindData(T dataBinding);

    protected abstract int getLayoutId();
}
