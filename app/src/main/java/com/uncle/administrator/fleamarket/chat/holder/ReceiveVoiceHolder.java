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
import com.uncle.administrator.fleamarket.databinding.ItemChatReceivedVoiceBinding;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static cn.bmob.newim.core.BmobIMClient.getContext;

/**
 * 接收到的文本类型
 * @author unclewei
 */
public class ReceiveVoiceHolder extends BaseBindViewHolder<BmobIMMessage> {

    private String currentUid = "";

    private boolean isShow;

    public ReceiveVoiceHolder(ViewDataBinding binding, boolean isShowTime) {
        super(binding);
        this.isShow = isShowTime;
    }

    @Override
    public void bindTo(BaseBindViewHolder<BmobIMMessage> holder, BmobIMMessage item) {
        final ItemChatReceivedVoiceBinding itemChatReceivedVoiceBinding = (ItemChatReceivedVoiceBinding) binding;
        BmobIMMessage msg = item;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(getContext())
                .load(info.getAvatar())
                .into(itemChatReceivedVoiceBinding.ivAvatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        itemChatReceivedVoiceBinding.tvTime.setText(time);
        itemChatReceivedVoiceBinding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        itemChatReceivedVoiceBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //显示特有属性
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(false, msg);
        boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(), msg, new FileDownloadListener() {

                @Override
                public void onStart() {
                    itemChatReceivedVoiceBinding.progressLoad.setVisibility(View.VISIBLE);
                    itemChatReceivedVoiceBinding.tvVoiceLength.setVisibility(View.GONE);
                    itemChatReceivedVoiceBinding.ivVoice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                }

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        itemChatReceivedVoiceBinding.progressLoad.setVisibility(View.GONE);
                        itemChatReceivedVoiceBinding.tvVoiceLength.setVisibility(View.VISIBLE);
                        itemChatReceivedVoiceBinding.tvVoiceLength.setText(message.getDuration() + "\''");
                        itemChatReceivedVoiceBinding.ivVoice.setVisibility(View.VISIBLE);
                    } else {
                        itemChatReceivedVoiceBinding.progressLoad.setVisibility(View.GONE);
                        itemChatReceivedVoiceBinding.tvVoiceLength.setVisibility(View.GONE);
                        itemChatReceivedVoiceBinding.ivVoice.setVisibility(View.INVISIBLE);
                    }
                }
            });
            downloadTask.execute(message.getContent());
        } else {
            itemChatReceivedVoiceBinding.tvVoiceLength.setVisibility(View.VISIBLE);
            itemChatReceivedVoiceBinding.tvVoiceLength.setText(message.getDuration() + "\''");
        }
        itemChatReceivedVoiceBinding.ivVoice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, itemChatReceivedVoiceBinding.ivVoice));

        itemChatReceivedVoiceBinding.ivVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

    }
}