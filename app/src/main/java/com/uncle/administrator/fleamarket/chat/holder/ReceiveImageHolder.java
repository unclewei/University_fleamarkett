package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatReceivedImageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {
    ItemChatReceivedImageBinding binding = (ItemChatReceivedImageBinding) dataBinding;

    public ReceiveImageHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_image, onRecyclerViewListener);
    }

    @Override
    public void setData(Object o) {

        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(binding.ivAvatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        binding.tvTime.setText(time);
        //可使用buildFromDB方法转化为指定类型的消息
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false, msg);
        //显示图片
        Glide.with(getContext())
                .load(message.getRemoteUrl())
                .into(binding.ivPicture);
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
            }
        });

        binding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击图片:" + message.getRemoteUrl() + "");
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        binding.ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
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