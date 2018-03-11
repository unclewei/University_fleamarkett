package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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

/**
 * 发送的语音类型
 */
public class SendVoiceHolder extends BaseViewHolder {

    private ItemChatSentVoiceBinding binding = (ItemChatSentVoiceBinding) dataBinding;

    BmobIMConversation c;

    public SendVoiceHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_sent_voice, onRecyclerViewListener);
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
        //使用buildFromDB方法转化成指定类型的消息
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
        binding.tvVoiceLength.setText(message.getDuration() + "\''");

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus() || status == BmobIMSendStatus.UPLOAD_FAILED.getStatus()) {//发送失败/上传失败
            binding.ivFailResend.setVisibility(View.VISIBLE);
            binding.progressLoad.setVisibility(View.GONE);
            binding.tvSendStatus.setVisibility(View.INVISIBLE);
            binding.tvVoiceLength.setVisibility(View.INVISIBLE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            binding.progressLoad.setVisibility(View.VISIBLE);
            binding.ivFailResend.setVisibility(View.GONE);
            binding.tvSendStatus.setVisibility(View.INVISIBLE);
            binding.tvVoiceLength.setVisibility(View.INVISIBLE);
        } else {//发送成功
            binding.ivFailResend.setVisibility(View.GONE);
            binding.progressLoad.setVisibility(View.GONE);
            binding.tvSendStatus.setVisibility(View.GONE);
            binding.tvVoiceLength.setVisibility(View.VISIBLE);
        }

        binding.ivVoice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, binding.ivVoice));

        binding.ivVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
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
