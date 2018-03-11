package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
public class SendImageHolder extends BaseViewHolder {

    private ItemChatSentImageBinding binding = (ItemChatSentImageBinding) dataBinding;

    BmobIMConversation c;

    public SendImageHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_sent_image, onRecyclerViewListener);
        this.c = c;
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
        //
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(true, msg);
        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus() || status == BmobIMSendStatus.UPLOAD_FAILED.getStatus()) {
            binding.ivFailResend.setVisibility(View.VISIBLE);
            binding.progressLoad.setVisibility(View.GONE);
            binding.tvSendStatus.setVisibility(View.INVISIBLE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            binding.progressLoad.setVisibility(View.VISIBLE);
            binding.ivFailResend.setVisibility(View.GONE);
            binding.tvSendStatus.setVisibility(View.INVISIBLE);
        } else {
            binding.tvSendStatus.setVisibility(View.VISIBLE);
            binding.tvSendStatus.setText("已发送");
            binding.ivFailResend.setVisibility(View.GONE);
            binding.progressLoad.setVisibility(View.GONE);
        }

        //发送的不是远程图片地址，则取本地地址
        Glide.with(getContext())
                .load(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath() : message.getRemoteUrl())
                .into(binding.ivPicture);
//    ViewUtil.setPicture(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(), R.mipmap.ic_launcher, iv_picture,null);

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
            }
        });
        binding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击图片:" + (TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath() : message.getRemoteUrl()) + "");
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

        //重发
        binding.ivFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        binding.progressLoad.setVisibility(View.VISIBLE);
                        binding.ivFailResend.setVisibility(View.GONE);
                        binding.tvSendStatus.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            binding.tvSendStatus.setVisibility(View.VISIBLE);
                            binding.tvSendStatus.setText("已发送");
                            binding.ivFailResend.setVisibility(View.GONE);
                            binding.progressLoad.setVisibility(View.GONE);
                        } else {
                            binding.ivFailResend.setVisibility(View.VISIBLE);
                            binding.progressLoad.setVisibility(View.GONE);
                            binding.tvSendStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void showTime(boolean isShow) {
        binding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
