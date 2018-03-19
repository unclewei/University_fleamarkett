package com.uncle.administrator.fleamarket.Mine;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.DTO.shop_goods;
import com.uncle.administrator.fleamarket.R;

/**
 *
 * @author Administrator
 * @date 2018/3/19 0019
 */

public class MineDataAdapter extends BaseAdapter<shop_goods> {
    public MineDataAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.adapter_mine_data;
    }

    @Override
    protected BaseBindViewHolder<shop_goods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<shop_goods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<shop_goods> holder, shop_goods item) {
                binding.setVariable(getPosition(),item);
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
