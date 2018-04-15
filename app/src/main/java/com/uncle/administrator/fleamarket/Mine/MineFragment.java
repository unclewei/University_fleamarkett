package com.uncle.administrator.fleamarket.Mine;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.DTO.Profile;
import com.uncle.Util.SPUtil;
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
        String profileStr = SPUtil.getInstance(getActivity()).getData("account", "profile");
        Profile profile = new Gson().fromJson(profileStr, Profile.class);
        String school = profile.getCollege() + "·" + profile.getOrganization();
        binding.tvName.setText(profile.getName());
        binding.tvSchool.setText(school);
        Glide.with(getActivity())
                .load(profile.getAvatar())
                .placeholder(R.drawable.head_black)
                .error(R.drawable.head_black)
                .into(binding.imgAvatar);
    }
}
