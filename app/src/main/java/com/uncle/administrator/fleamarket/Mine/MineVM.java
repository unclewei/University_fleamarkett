package com.uncle.administrator.fleamarket.Mine;

import android.content.Intent;
import android.view.View;

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
        Intent intent = new Intent(fragment.getContext(), PersonDataSettingActivity.class);
        fragment.startActivity(intent);
    }

    public void navToMyPublic(View view) {
        Intent intent = new Intent(fragment.getContext(), MineDataActivity.class);
        intent.putExtra(MineDataActivity.TYPE, MineDataActivity.MY_PUBLIC);
        fragment.startActivity(intent);
    }

    public void navToMyZan(View view) {
        Intent intent = new Intent(fragment.getContext(), MineDataActivity.class);
        intent.putExtra(MineDataActivity.TYPE, MineDataActivity.MY_ZAN);
        fragment.startActivity(intent);
    }

    public void navToSet(View view) {
        fragment.exit();
    }

}
