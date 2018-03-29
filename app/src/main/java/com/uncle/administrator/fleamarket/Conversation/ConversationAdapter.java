package com.uncle.administrator.fleamarket.Conversation;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseAdapter;
import com.uncle.Base.BaseBindViewHolder;
import com.uncle.administrator.fleamarket.DTO.ConversationDTO;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ConversationItemBinding;

/**
 * @author unclewei
 * @Data 2018/3/19.
 */

public class ConversationAdapter extends BaseAdapter<ConversationDTO> {
    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayout(int position) {
        return R.layout.conversation_item;
    }

    @Override
    protected BaseBindViewHolder<ConversationDTO> createHolder(ViewDataBinding binding) {
        return new BaseBindViewHolder<ConversationDTO>(binding) {
            @Override
            public void bindTo(BaseBindViewHolder<ConversationDTO> holder, ConversationDTO item) {
                ConversationItemBinding conversationItemBinding = (ConversationItemBinding) binding;
                Glide.with(getContext()).load(item.getAvatar()).into(conversationItemBinding.ivAvatar);
                conversationItemBinding.tvName.setText(item.getUsername());
                conversationItemBinding.tvContact.setText(item.getLastWord());
                conversationItemBinding.tvLastTime.setText(item.getStringTime(item.getLastTime()));
            }
        };
    }

    @Override
    protected boolean areItemsSame(ConversationDTO oldItem, ConversationDTO newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsSame(ConversationDTO oldItem, ConversationDTO newItem) {
        return oldItem.getAvatar().equals(newItem.getAvatar())
                && oldItem.getLastTime() == newItem.getLastTime()
                && oldItem.getLastWord().equals(newItem.getLastWord());
    }
}
