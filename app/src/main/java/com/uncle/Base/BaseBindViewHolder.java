package com.lexing360.uibase.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nnv on 2017/8/28.
 */

public abstract class BaseBindViewHolder<T> extends RecyclerView.ViewHolder {

    protected ViewDataBinding binding;

    public BaseBindViewHolder(View view) {
        super(view);
    }

    public BaseBindViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static BaseBindViewHolder create(View view) {
        return new BaseBindViewHolder(view) {
            @Override
            public void bindTo(BaseBindViewHolder holder, Object item) {

            }
        };
    }

    public abstract void bindTo(BaseBindViewHolder<T> holder, T item);

}
