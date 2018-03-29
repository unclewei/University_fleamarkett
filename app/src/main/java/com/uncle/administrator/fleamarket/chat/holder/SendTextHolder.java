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
import com.uncle.administrator.fleamarket.databinding.ItemChatSentMessageBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送的文本类型
 *
 * @author unclewei
 */
public class SendTextHolder extends BaseBindViewHolder<BmobIMMessage> {
    private boolean isShow;

    private BmobIMConversation c;

    public SendTextHolder(ViewDataBinding binding, boolean isShowTime, BmobIMConversation c) {
        super(binding);
        this.isShow = isShowTime;
        this.c = c;
    }

    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, final BmobIMMessage message) {
        final ItemChatSentMessageBinding itemChatSentMessageBinding = (ItemChatSentMessageBinding) binding;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        Glide.with(itemChatSentMessageBinding.ivAvatar.getContext())
                .load(info.getAvatar())
                .into(itemChatSentMessageBinding.ivAvatar);
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        itemChatSentMessageBinding.tvMessage.setText(content);
        itemChatSentMessageBinding.tvTime.setText(time);
        itemChatSentMessageBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        itemChatSentMessageBinding.tvTime.setVisibility(View.VISIBLE);
        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            itemChatSentMessageBinding.ivFailResend.setVisibility(View.VISIBLE);
            itemChatSentMessageBinding.progressLoad.setVisibility(View.GONE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            itemChatSentMessageBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentMessageBinding.progressLoad.setVisibility(View.VISIBLE);
        } else {
            itemChatSentMessageBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentMessageBinding.progressLoad.setVisibility(View.GONE);
        }
        itemChatSentMessageBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        itemChatSentMessageBinding.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemChatSentMessageBinding.tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        itemChatSentMessageBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //重发
        itemChatSentMessageBinding.ivFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        itemChatSentMessageBinding.progressLoad.setVisibility(View.VISIBLE);
                        itemChatSentMessageBinding.ivFailResend.setVisibility(View.GONE);
                        itemChatSentMessageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            itemChatSentMessageBinding.tvSendStatus.setVisibility(View.VISIBLE);
                            itemChatSentMessageBinding.tvSendStatus.setText("已发送");
                            itemChatSentMessageBinding.ivFailResend.setVisibility(View.GONE);
                            itemChatSentMessageBinding.progressLoad.setVisibility(View.GONE);
                        } else {
                            itemChatSentMessageBinding.ivFailResend.setVisibility(View.VISIBLE);
                            itemChatSentMessageBinding.progressLoad.setVisibility(View.GONE);
                            itemChatSentMessageBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}
