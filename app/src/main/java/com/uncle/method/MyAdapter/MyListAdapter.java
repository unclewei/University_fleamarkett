package com.uncle.method.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.widget.ListView;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.university_fleamarket.R;
import com.uncle.administrator.university_fleamarket.databinding.AdapterBaseBinding;
import com.uncle.bomb.ShopGoods;

/**
 * @author unclewei
 */
public class MyListAdapter extends BaseAdapter<ShopGoods> {

    private ListView listView;
    private Context context;

    public MyListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.adapter_base;
    }

    @Override
    protected BaseBindViewHolder<ShopGoods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<ShopGoods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<ShopGoods> holder, ShopGoods item) {
                AdapterBaseBinding baseBinding = (AdapterBaseBinding) binding;
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