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
import com.uncle.administrator.fleamarket.databinding.ItemChatReceivedImageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseBindViewHolder<BmobIMMessage> {
    private boolean isShow;

    public ReceiveImageHolder(ViewDataBinding binding, boolean isShowTime) {
        super(binding);
        this.isShow = isShowTime;
    }

    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, BmobIMMessage item) {
        ItemChatReceivedImageBinding itemChatReceivedImageBinding = (ItemChatReceivedImageBinding) binding;
        BmobIMMessage msg =  item;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(itemChatReceivedImageBinding.ivAvatar.getContext())
                .load(info.getAvatar())
                .into(itemChatReceivedImageBinding.ivAvatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        itemChatReceivedImageBinding.tvTime.setText(time);
        //可使用buildFromDB方法转化为指定类型的消息
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false, msg);
        itemChatReceivedImageBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        //显示图片
        Glide.with(itemChatReceivedImageBinding.ivPicture.getContext())
                .load(message.getRemoteUrl())
                .into(itemChatReceivedImageBinding.ivPicture);
        itemChatReceivedImageBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        itemChatReceivedImageBinding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemChatReceivedImageBinding.ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }
}