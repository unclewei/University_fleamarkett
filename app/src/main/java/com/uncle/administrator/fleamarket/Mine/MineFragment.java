package com.uncle.administrator.fleamarket.Mine;


import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.MineFragmentBinding;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class MineFragment extends BaseBindingFragment<MineFragmentBinding> {
    private SharedPreferences sharedPreferences;
    private MineVM mineVM;

    @Override
    protected void bindData(MineFragmentBinding dataBinding) {
        initData();
        mineVM = new MineVM(this, binding);
        binding.setModule(mineVM);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("nick_name", null);
        String college = sharedPreferences.getString("college", null);
        String organization = sharedPreferences.getString("organization", null);
        String avater = sharedPreferences.getString("head_portrait_adress", null);

        binding.tvName.setText(name);
        binding.tvSchool.setText(college + "·" + organization);
        Glide.with(getActivity())
                .load(avater)
                .placeholder(R.drawable.head_black)
                .error(R.drawable.head_black)
                .into(binding.imgAvatar);
    }

    @Override
    public void onStart() {
        super.onStart();
        if ("ture".equals(sharedPreferences.getString("refresh", "false"))) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh", "false");
            editor.commit();
            initData();
        }
    }
}
