package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.NewRecordPlayClickListener;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatSentVoiceBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

import static cn.bmob.newim.core.BmobIMClient.getContext;

/**
 * 发送的语音类型
 */
public class SendVoiceHolder extends BaseBindViewHolder<BmobIMMessage> {


    private boolean isShow;
    private BmobIMConversation c;

    public SendVoiceHolder(ViewDataBinding binding, boolean isShowTime, BmobIMConversation c) {
        super(binding);
        this.isShow = isShowTime;
        this.c = c;
    }

    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, BmobIMMessage item) {
        final ItemChatSentVoiceBinding itemChatSentVoiceBinding = (ItemChatSentVoiceBinding) binding;
        BmobIMMessage msg = (BmobIMMessage) item;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(itemChatSentVoiceBinding.ivAvatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        itemChatSentVoiceBinding.tvTime.setText(time);
        //使用buildFromDB方法转化成指定类型的消息
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
        itemChatSentVoiceBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        itemChatSentVoiceBinding.tvVoiceLength.setText(message.getDuration() + "\''");

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus() || status == BmobIMSendStatus.UPLOAD_FAILED.getStatus()) {//发送失败/上传失败
            itemChatSentVoiceBinding.ivFailResend.setVisibility(View.VISIBLE);
            itemChatSentVoiceBinding.progressLoad.setVisibility(View.GONE);
            itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.INVISIBLE);
            itemChatSentVoiceBinding.tvVoiceLength.setVisibility(View.INVISIBLE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            itemChatSentVoiceBinding.progressLoad.setVisibility(View.VISIBLE);
            itemChatSentVoiceBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.INVISIBLE);
            itemChatSentVoiceBinding.tvVoiceLength.setVisibility(View.INVISIBLE);
        } else {//发送成功
            itemChatSentVoiceBinding.ivFailResend.setVisibility(View.GONE);
            itemChatSentVoiceBinding.progressLoad.setVisibility(View.GONE);
            itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.GONE);
            itemChatSentVoiceBinding.tvVoiceLength.setVisibility(View.VISIBLE);
        }

        itemChatSentVoiceBinding.ivVoice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, itemChatSentVoiceBinding.ivVoice));

        itemChatSentVoiceBinding.ivVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        itemChatSentVoiceBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //重发
        itemChatSentVoiceBinding.ivFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        itemChatSentVoiceBinding.progressLoad.setVisibility(View.VISIBLE);
                        itemChatSentVoiceBinding.ivFailResend.setVisibility(View.GONE);
                        itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.VISIBLE);
                            itemChatSentVoiceBinding.tvSendStatus.setText("已发送");
                            itemChatSentVoiceBinding.ivFailResend.setVisibility(View.GONE);
                            itemChatSentVoiceBinding.progressLoad.setVisibility(View.GONE);
                        } else {
                            itemChatSentVoiceBinding.ivFailResend.setVisibility(View.VISIBLE);
                            itemChatSentVoiceBinding.progressLoad.setVisibility(View.GONE);
                            itemChatSentVoiceBinding.tvSendStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}
