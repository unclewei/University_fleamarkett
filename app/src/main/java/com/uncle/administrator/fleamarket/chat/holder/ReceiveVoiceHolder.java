package com.uncle.administrator.fleamarket.chat.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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

/**
 * 接收到的文本类型
 */
public class ReceiveVoiceHolder extends BaseViewHolder {

private ItemChatReceivedVoiceBinding binding = (ItemChatReceivedVoiceBinding) dataBinding;

  private String currentUid="";

  public ReceiveVoiceHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_received_voice,onRecyclerViewListener);
    try {
      currentUid = BmobUser.getCurrentUser().getObjectId();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();

      Glide.with(getContext())
              .load(info.getAvatar())
              .into(binding.ivAvatar);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    binding.tvTime.setText(time);
    binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
      }
    });
    //显示特有属性
    final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(false, msg);
    boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
    if(!isExists){//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
        BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(),msg,new FileDownloadListener() {

          @Override
          public void onStart() {
              binding.progressLoad.setVisibility(View.VISIBLE);
              binding.tvVoiceLength.setVisibility(View.GONE);
              binding.ivVoice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
          }

          @Override
          public void done(BmobException e) {
            if(e==null){
                binding.progressLoad.setVisibility(View.GONE);
                binding.tvVoiceLength.setVisibility(View.VISIBLE);
                binding.tvVoiceLength.setText(message.getDuration()+"\''");
                binding.ivVoice.setVisibility(View.VISIBLE);
            }else{
                binding.progressLoad.setVisibility(View.GONE);
                binding.tvVoiceLength.setVisibility(View.GONE);
                binding.ivVoice.setVisibility(View.INVISIBLE);
            }
          }
        });
        downloadTask.execute(message.getContent());
    }else{
        binding.tvVoiceLength.setVisibility(View.VISIBLE);
        binding.tvVoiceLength.setText(message.getDuration() + "\''");
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

  }

  public void showTime(boolean isShow) {
    binding.tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);

  }
}