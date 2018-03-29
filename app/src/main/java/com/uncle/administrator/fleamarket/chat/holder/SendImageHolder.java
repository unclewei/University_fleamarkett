package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatSentImageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送的文本类型
 */
public class SendImageHolder extends BaseBindViewHolder<BmobIMMessage> {


    private boolean isShow;
    private BmobIMConversation c;

    public SendImageHolder(ViewDataBinding binding, boolean isShowTime,BmobIMConversation c) {
        super(binding);
        this.isShow = isShowTime;
        this.c = c;
    }


    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, BmobIMMessage item) {
        final ItemChatSentImageBinding itemChatSentImageBinding = (ItemChatSentImageBinding) binding;
        BmobIMMessage msg = (BmobIMMessage) item;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(itemChatSentImageBinding.ivAvatar.getContext())
                .load(info.getAvatar())
                .into(itemChatSentImageBinding.ivAvatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        itemChatSentImageBinding.tvTime.setText(time);
        itemChatSentImageBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        //
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(true, msg);
        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus() || status == BmobIMSendStatus.UPLOAD_FAILED.getStatus()) {
            itemChatSentImageBinding.ivFailResend.setVisibility(View.VISIBLE);
            itemChatSentImageBinding.progressLoad.setVisibility(View.GONE);
            itemChatSentImageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            itemChatSentImageBinding.progressLoad.setVisibility(View.VISIBLE);
            itemChatSentImageBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentImageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
        } else {
            itemChatSentImageBinding.tvSendStatus.setVisibility(View.VISIBLE);
            itemChatSentImageBinding.tvSendStatus.setText("已发送");
            itemChatSentImageBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentImageBinding.progressLoad.setVisibility(View.GONE);
        }

        //发送的不是远程图片地址，则取本地地址
        Glide.with(itemChatSentImageBinding.ivPicture.getContext())
                .load(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath() : message.getRemoteUrl())
                .into(itemChatSentImageBinding.ivPicture);
//    ViewUtil.setPicture(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(), R.mipmap.ic_launcher, iv_picture,null);

        itemChatSentImageBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        itemChatSentImageBinding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        }
        });

        itemChatSentImageBinding.ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        //重发
        itemChatSentImageBinding.ivFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        itemChatSentImageBinding.progressLoad.setVisibility(View.VISIBLE);
                        itemChatSentImageBinding.ivFailResend.setVisibility(View.GONE);
                        itemChatSentImageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            itemChatSentImageBinding.tvSendStatus.setVisibility(View.VISIBLE);
                            itemChatSentImageBinding.tvSendStatus.setText("已发送");
                            itemChatSentImageBinding.ivFailResend.setVisibility(View.GONE);
                            itemChatSentImageBinding.progressLoad.setVisibility(View.GONE);
                        } else {
                            itemChatSentImageBinding.ivFailResend.setVisibility(View.VISIBLE);
                            itemChatSentImageBinding.progressLoad.setVisibility(View.GONE);
                            itemChatSentImageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}
