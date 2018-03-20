package com.uncle.Base;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.uncle.administrator.fleamarket.DTO.User_account;

/**
 * @author nnv
 * @date 2017/7/19
 */

public abstract class BaseBindingFragment<T extends ViewDataBinding> extends Fragment {

    protected T binding;
    private boolean isCreated = false;
    protected User_account myAccount;

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
        bindData(binding);
        getMyAccountFromSharePerFences();
        return binding.getRoot();
    }

    private void getMyAccountFromSharePerFences() {
        SharedPreferences sp = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String string = sp.getString("myAccount", null);
        if (string != null) {
            myAccount = new Gson().fromJson(string, User_account.class);
        }
    }

    public void saveMyAccountFromSharePerFences(User_account myAccount) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myAccount", myAccount.toString());
        editor.commit();
    }

    protected abstract void bindData(T dataBinding);

    protected abstract int getLayoutId();
}
