package com.uncle.administrator.fleamarket.Conversation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ConversationItemBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * @author unclewei
 * @Data 2018/3/19.
 */

public class ConversationAdapter extends BaseAdapter<BmobIMConversation> {
    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.conversation_item;
    }

    @Override
    protected BaseBindViewHolder<BmobIMConversation> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<BmobIMConversation>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<BmobIMConversation> holder, BmobIMConversation item) {
                ConversationItemBinding conversationItemBinding = (ConversationItemBinding) binding;
                if (item.getMessages().size() == 0) {
                    return;
                }
                BmobIMMessage bmobIMMessage = item.getMessages().get(0);
                String text = bmobIMMessage.getMsgType().equals(BmobIMMessageType.TEXT.getType()) ?
                        bmobIMMessage.getContent() :
                        (bmobIMMessage.getMsgType().equals(BmobIMMessageType.IMAGE.getType()) ? "[照片]" : "[语音]");
                Glide.with(getContext())
                        .load(item.getConversationIcon())
                        .error(R.drawable.head_yard)
                        .into(conversationItemBinding.ivAvatar);
                conversationItemBinding.tvName.setText(item.getConversationTitle());
                conversationItemBinding.tvContact.setText(text);
                conversationItemBinding.tvLastTime.setText(longToString(item.getUpdateTime()));
            }
        };
    }

    public static String longToString(long currentTime) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format2.format(new Date(currentTime));
    }

    @Override
    protected boolean areItemsSame(BmobIMConversation oldItem, BmobIMConversation newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsSame(BmobIMConversation oldItem, BmobIMConversation newItem) {
        return oldItem.getConversationIcon().equals(newItem.getConversationIcon())
                && oldItem.getConversationType() == newItem.getConversationType()
                && oldItem.getConversationTitle().equals(newItem.getConversationTitle());
    }
}
