package com.uncle.administrator.fleamarket.Sell;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.bean.ImageItem;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.SellImageItemBinding;

/**
 * @author unclewei
 * @Data 2018/4/4.
 */

public class SellImageAdapter extends BaseAdapter<ImageItem> {
    public SellImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.sell_image_item;
    }

    @Override
    protected BaseBindViewHolder<ImageItem> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<ImageItem>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<ImageItem> holder, ImageItem item) {
                SellImageItemBinding sellImageItemBinding = (SellImageItemBinding) binding;
                Glide.with(getContext()).load(item.path).into(sellImageItemBinding.imageView);
            }
        };
    }

    @Override
    protected boolean areItemsSame(ImageItem oldItem, ImageItem newItem) {
        return false;
    }

    @Override
    protected boolean areContentsSame(ImageItem oldItem, ImageItem newItem) {
        return false;
    }
}
