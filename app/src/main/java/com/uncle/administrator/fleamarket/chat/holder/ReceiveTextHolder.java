package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatReceivedMessageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

import static cn.bmob.newim.core.BmobIMClient.getContext;

/**
 * 接收到的文本类型
 *
 * @author unclewei
 */
public class ReceiveTextHolder extends BaseBindViewHolder<BmobIMMessage> {
    private boolean isShow;

    public ReceiveTextHolder(ViewDataBinding binding, boolean isShowTime) {
        super(binding);
        this.isShow = isShowTime;
    }

    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, BmobIMMessage item) {
        ItemChatReceivedMessageBinding itemChatReceivedMessageBinding = (ItemChatReceivedMessageBinding) binding;
        final BmobIMMessage message = item;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        itemChatReceivedMessageBinding.tvTime.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(itemChatReceivedMessageBinding.ivAvatar);
        String content = message.getContent();
        itemChatReceivedMessageBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        itemChatReceivedMessageBinding.tvMessage.setText(content);
        itemChatReceivedMessageBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        itemChatReceivedMessageBinding.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemChatReceivedMessageBinding.tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }
}