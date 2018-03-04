package com.uncle.administrator.fleamarket.Home;


import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.AdapterHomeBinding;
import com.uncle.bomb.ShopGoods;

/**
 * @author unclewei
 */
public class HomeListAdapter extends BaseAdapter<ShopGoods> {

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
    protected BaseBindViewHolder<ShopGoods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<ShopGoods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<ShopGoods> holder, ShopGoods item) {
                AdapterHomeBinding baseBinding = (AdapterHomeBinding) binding;
                baseBinding.baseItemName.setText(item.getName());
                baseBinding.baseItemCollege.setText(item.getCollege());
                baseBinding.baseItemDescription.setText(item.getText());
                baseBinding.baseItemPrice.setText(item.getPrice());
                Glide.with(context).load(item.getImage1()).into(baseBinding.baseItemImg);
            }
        };
    }

    @Override
    protected boolean areItemsSame(ShopGoods oldItem, ShopGoods newItem) {
        return false;
    }

    @Override
    protected boolean areContentsSame(ShopGoods oldItem, ShopGoods newItem) {
        return false;
    }


}