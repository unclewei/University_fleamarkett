package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
public class SendTextHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemChatSentMessageBinding binding = (ItemChatSentMessageBinding) dataBinding;

    BmobIMConversation c;

    public SendTextHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_sent_message, listener);
        this.c = c;
    }

    @Override
    public void setData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(binding.ivAvatar);
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        binding.tvMessage.setText(content);
        binding.tvTime.setText(time);

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            binding.ivFailResend.setVisibility(View.VISIBLE);
            binding.progressLoad.setVisibility(View.GONE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            binding.ivFailResend.setVisibility(View.GONE);
            binding.progressLoad.setVisibility(View.VISIBLE);
        } else {
            binding.ivFailResend.setVisibility(View.GONE);
            binding.progressLoad.setVisibility(View.GONE);
        }
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击了");
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
