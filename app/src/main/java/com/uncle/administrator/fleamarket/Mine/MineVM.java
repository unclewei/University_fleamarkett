package com.uncle.administrator.fleamarket.Mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.uncle.administrator.fleamarket.ChangePersonDataActivity;
import com.uncle.administrator.fleamarket.DTO.User_account;
import com.uncle.administrator.fleamarket.databinding.MineFragmentBinding;

/**
 * @author Administrator
 * @date 2018/3/19 0019
 */

public class MineVM {
    private MineFragment fragment;
    private MineFragmentBinding binding;

    public MineVM(MineFragment fragment, MineFragmentBinding binding) {
        this.fragment = fragment;
        this.binding = binding;
    }

    public void navToAvatar(View view) {
        Intent intent = new Intent(fragment.getContext(), ChangePersonDataActivity.class);
        fragment.startActivity(intent);
    }

    public void navToMyPublic(View view) {
        Intent intent = new Intent(fragment.getContext(), MineDataActivity.class);
        intent.putExtra(MineDataActivity.TYPE, MineDataActivity.MY_PUBLIC);
        fragment.startActivity(intent);
    }

    public void navToMyBuy(View view) {
        Intent intent = new Intent(fragment.getContext(), MineDataActivity.class);
        intent.putExtra(MineDataActivity.TYPE, MineDataActivity.MY_SCAN);
        fragment.startActivity(intent);
    }

    public void navToMyZan(View view) {
        Intent intent = new Intent(fragment.getContext(), MineDataActivity.class);
        intent.putExtra(MineDataActivity.TYPE, MineDataActivity.MY_ZAN);
        fragment.startActivity(intent);
    }

    public void navToSet(View view) {
        User_account userAccount = new User_account("http://bmob-cdn-8783.b0.upaiyun.com/2017/10/19/a9b5ff14ae814db1abe69f24eebaf01b.jpg",
                "威", "五邑大学", "计算机学院", null, null, null);
        userAccount.setObjectId("a646d91303");
        saveMyAccountFromSharePerFences(view.getContext(), userAccount);
    }

    public void saveMyAccountFromSharePerFences(Context context, User_account myAccount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myAccount", new Gson().toJson(myAccount));
        editor.commit();
    }
}
