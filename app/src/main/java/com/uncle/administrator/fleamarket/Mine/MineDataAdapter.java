package com.uncle.administrator.fleamarket.Mine;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.DTO.shopGoods;
import com.uncle.administrator.fleamarket.R;

/**
 *
 * @author Administrator
 * @date 2018/3/19 0019
 */

public class MineDataAdapter extends BaseAdapter<shopGoods> {
    public MineDataAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.adapter_mine_data;
    }

    @Override
    protected BaseBindViewHolder<shopGoods> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<shopGoods>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<shopGoods> holder, shopGoods item) {
                binding.setVariable(getPosition(),item);
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
