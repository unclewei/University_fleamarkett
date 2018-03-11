package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.uncle.administrator.fleamarket.R;
import com.uncle.Base.BaseViewHolder;
import com.uncle.administrator.fleamarket.chat.OnRecyclerViewListener;
import com.uncle.administrator.fleamarket.databinding.ItemChatAgreeBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 同意添加好友的agree类型
 */
public class AgreeHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {


    public AgreeHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_agree, listener);
    }

    @Override
    public void setData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        ((ItemChatAgreeBinding) dataBinding).tvMessage.setText(content);
        ((ItemChatAgreeBinding) dataBinding).tvTime.setText(time);
    }


    public void showTime(boolean isShow) {
        ((ItemChatAgreeBinding) dataBinding).tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
