package com.uncle.administrator.fleamarket.Home;


import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.AdapterBaseBinding;
import com.uncle.bomb.shop_goods;

/**
 * @author unclewei
 */
public class HomeListAdapter extends BaseAdapter<shop_goods> {

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
    protected BaseBindViewHolder<shop_goods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<shop_goods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<shop_goods> holder, shop_goods item) {
                AdapterBaseBinding baseBinding = (AdapterBaseBinding) binding;
                baseBinding.baseItemName.setText(item.getName());
                baseBinding.baseItemCollege.setText(item.getCollege());
                baseBinding.baseItemDescription.setText(item.getText());
                baseBinding.baseItemPrice.setText(item.getPrice());
                Glide.with(context).load(item.getImage1()).into(baseBinding.baseItemImg);
            }
        };
    }

    @Override
    protected boolean areItemsSame(shop_goods oldItem, shop_goods newItem) {
        return false;
    }

    @Override
    protected boolean areContentsSame(shop_goods oldItem, shop_goods newItem) {
        return false;
    }


}