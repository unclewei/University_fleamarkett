package com.uncle.administrator.fleamarket.chat;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.chat.holder.ReceiveImageHolder;
import com.uncle.administrator.fleamarket.chat.holder.ReceiveTextHolder;
import com.uncle.administrator.fleamarket.chat.holder.ReceiveVoiceHolder;
import com.uncle.administrator.fleamarket.chat.holder.SendImageHolder;
import com.uncle.administrator.fleamarket.chat.holder.SendTextHolder;
import com.uncle.administrator.fleamarket.chat.holder.SendVoiceHolder;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * @author :smile
 * @project:ChatAdapter
 * @date :2016-01-22-14:18
 */
public class ChatAdapter extends BaseBindAdapter<BmobIMMessage> {
    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;


    private String currentUid = "";
    private BmobIMConversation c;
    private int itemPosition = 0;

    public ChatAdapter(Context context, String currentUid, BmobIMConversation c) {
        super(context);
        this.currentUid = currentUid;
        this.c = c;
    }

    public int findPosition(BmobIMMessage message) {
        int index = this.getItemCount();
        int position = -1;
        while (index-- > 0) {
            if (message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public BmobIMMessage getFirstMessage() {
        if (null != getData() && getData().size() > 0) {
            return getData().get(0);
        } else {
            return null;
        }
    }

    @Override
    protected BaseBindViewHolder<BmobIMMessage> createVH(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                viewType, parent, false);
        if (R.layout.item_chat_sent_message == viewType) {
            return new SendTextHolder(binding, shouldShowTime(itemPosition), c);
        }
        if (R.layout.item_chat_received_message == viewType) {
            return new ReceiveTextHolder(binding, shouldShowTime(itemPosition));
        }
        if (R.layout.item_chat_sent_image == viewType) {
            return new SendImageHolder(binding, shouldShowTime(itemPosition), c);
        }
        if (R.layout.item_chat_received_image == viewType) {
            return new ReceiveImageHolder(binding, shouldShowTime(itemPosition));
        }
        if (R.layout.item_chat_sent_voice == viewType) {
            return new SendVoiceHolder(binding, shouldShowTime(itemPosition), c);
        }
        if (R.layout.item_chat_received_voice == viewType) {
            return new ReceiveVoiceHolder(binding, shouldShowTime(itemPosition));
        }
        return new SendTextHolder(binding, shouldShowTime(itemPosition), c);
    }


    @Override
    public int getItemViewType(int position) {
        itemPosition = position;
        BmobIMMessage message = getData().get(position);
        if (message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
            return message.getFromId().equals(currentUid) ? R.layout.item_chat_sent_image : R.layout.item_chat_received_image;
        } else if (message.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            return message.getFromId().equals(currentUid) ? R.layout.item_chat_sent_voice : R.layout.item_chat_received_voice;
        } else if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            return message.getFromId().equals(currentUid) ? R.layout.item_chat_sent_message : R.layout.item_chat_received_message;
        } else {
            return -1;
        }
    }

    @Override
    protected int getViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = getData().get(position - 1).getCreateTime();
        long curTime = getData().get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }


}