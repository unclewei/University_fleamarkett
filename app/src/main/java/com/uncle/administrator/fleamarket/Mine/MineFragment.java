package com.uncle.administrator.fleamarket.Mine;


import android.content.Intent;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.Login.LoginActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.MineFragmentBinding;

/**
 * @author Administrator
 * @date 2016/11/22 0022
 */

public class MineFragment extends BaseBindingFragment<MineFragmentBinding> {

    @Override
    protected void bindData(MineFragmentBinding dataBinding) {
        initData();
        MineVM mineVM = new MineVM(this, binding);
        binding.setModule(mineVM);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    private void initData() {
        String school = profile.getCollege() + "·" + profile.getOrganization();
        binding.tvName.setText(profile.getName());
        binding.tvSchool.setText(school);
        Glide.with(getActivity())
                .load(profile.getAvatar())
                .placeholder(R.drawable.head_black)
                .error(R.drawable.head_black)
                .into(binding.imgAvatar);
    }

    public void exit() {
        saveMyAccountFromSharePerFences(null);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(intent);
        ToastUtil.show(getActivity(), "退出登录");
        getActivity().finish();
    }
}
