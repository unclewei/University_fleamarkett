package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatReceivedMessageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的视频类型--这是举个例子，并没有展示出视频缩略图等信息，开发者可自行设置
 */
public class ReceiveVideoHolder extends BaseViewHolder {

    private ItemChatReceivedMessageBinding binding = (ItemChatReceivedMessageBinding) dataBinding;

    public ReceiveVideoHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
    }

    @Override
    public void setData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        binding.tvTime.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(binding.ivAvatar);
        String content = message.getContent();
        binding.tvMessage.setText("接收到的视频文件：" + content);
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
            }
        });

        binding.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        binding.tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });
    }

    public void showTime(boolean isShow) {
        binding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}