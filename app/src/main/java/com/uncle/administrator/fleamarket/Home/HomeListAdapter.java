package com.uncle.administrator.fleamarket.Home;


import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.DTO.shopGoods;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.AdapterHomeBinding;

/**
 * @author unclewei
 */
public class HomeListAdapter extends BaseAdapter<shopGoods> {

    private Context context;

    public HomeListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.adapter_home;
    }

    @Override
    protected BaseBindViewHolder<shopGoods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<shopGoods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<shopGoods> holder, shopGoods item) {
                AdapterHomeBinding baseBinding = (AdapterHomeBinding) binding;
                baseBinding.baseItemName.setText(item.getName());
                baseBinding.baseItemCollege.setText(item.getCollege());
                baseBinding.baseItemDescription.setText(item.getText());
                baseBinding.baseItemPrice.setText(item.getPrice());
                Glide.with(context).load(item.getImgFileList().get(0)).into(baseBinding.baseItemImg);
            }
        };
    }

    @Override
    protected boolean areItemsSame(shopGoods oldItem, shopGoods newItem) {
        return false;
    }

    @Override
    protected boolean areContentsSame(shopGoods oldItem, shopGoods newItem) {
        return false;
    }


}