package com.uncle.administrator.fleamarket.chat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.uncle.Base.BaseBindingActivity;
import com.uncle.Util.CommUtil;
import com.uncle.Util.KeyboardUtil;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ActivityChatBinding;
import com.uncle.database.ChatDataDao;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * 聊天界面
 *
 * @author :smile
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends BaseBindingActivity<ActivityChatBinding> implements MessageListHandler {
    private BmobRecordManager recordManager;
    private ChatAdapter adapter;
    private BmobIMConversation mConversationManager;
    private Drawable[] drawableAnims;
    protected LinearLayoutManager layoutManager;
    private ChatVM chatVM;
    public static final String TARGET_OBJECT_ID = "TARGET_OBJECT_ID";

    @Override
    protected void bindData(ActivityChatBinding dataBinding) {
        chatVM = new ChatVM(ChatActivity.this, binding);
        initConversationManager();
        initSwipeLayout();
        initVoiceView();
        initBottomView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private void initConversationManager() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("chat");
        BmobIMConversation conversationEntrance = (BmobIMConversation) bundle.getSerializable("c");
        //TODO 消息：5.1、根据会话入口获取消息管理，聊天页面
        if (conversationEntrance == null) {
            ToastUtil.show(ChatActivity.this, "获取聊天对象失败，请重试");
            return;
        }
        mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
    }

    private void initSwipeLayout() {
        binding.swRefresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        binding.rcView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this, profile.getObjectId(), mConversationManager);
        adapter.setEmptyView(LayoutInflater.from(ChatActivity.this).inflate(R.layout.load_more_view, null));
        binding.rcView.setAdapter(adapter);
        binding.llChat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.llChat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                binding.swRefresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        binding.swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
//        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
//            @Override
//            public void onItemClick(int position) {
//                Log.i(TAG, "" + position);
//            }
//
//            @Override
//            public boolean onItemLongClick(int position) {
//                //TODO 消息：5.3、删除指定聊天消息
//                mConversationManager.deleteMessage(adapter.getItem(position));
//                adapter.remove(position);
//                ToastUtil.show(ChatActivity.this, "删除成功");
//                return true;
//            }
//        });
    }

    private void initBottomView() {
        binding.editMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
        binding.editMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {

                    binding.btnChatSend.setVisibility(View.VISIBLE);
                    binding.btnChatKeyboard.setVisibility(View.GONE);
                    binding.btnChatVoice.setVisibility(View.GONE);
                } else {
                    if (binding.btnChatVoice.getVisibility() != View.VISIBLE) {
                        binding.btnChatVoice.setVisibility(View.VISIBLE);
                        binding.btnChatSend.setVisibility(View.GONE);
                        binding.btnChatKeyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化语音布局
     *
     * @param
     */
    private void initVoiceView() {
        binding.btnSpeak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * 初始化语音动画资源
     */
    private void initVoiceAnimRes() {
        drawableAnims = new Drawable[]{
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6)};
    }

    private void initRecordManager() {
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {


            @Override
            public void onVolumeChanged(int value) {
                binding.ivRecord.setImageDrawable(drawableAnims[value]);

            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Log.i(TAG, "voice: 已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {
                    // 需要重置按钮
                    binding.btnSpeak.setPressed(false);
                    binding.btnSpeak.setClickable(false);
                    // 取消录音框
                    binding.layoutRecord.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            binding.btnSpeak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommUtil.checkSdCard()) {
                        ToastUtil.show(ChatActivity.this, "发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        binding.layoutRecord.setVisibility(View.VISIBLE);
                        binding.tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(mConversationManager.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        binding.tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        binding.tvVoiceTips.setTextColor(Color.RED);
                    } else {
                        binding.tvVoiceTips.setText(getString(R.string.voice_up_tips));
                        binding.tvVoiceTips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    binding.layoutRecord.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            Log.i("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(mConversationManager.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                binding.layoutRecord.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     *
     * @return void
     * @Title: showShortToast
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public void onSendClick(View view) {
        sendMessage();
    }

    public void onPictureClick(View view) {
        sendLocalImageMessage();
    }

    public void onCameraClick(View view) {
        sendRemoteImageMessage();
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = binding.editMsg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            ToastUtil.show(ChatActivity.this, "请输入内容");
            return;
        }
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        msg.setExtra(text);
        mConversationManager.sendMessage(msg, listener);
    }

    /**
     * 发送本地图片文件
     */
    public void sendLocalImageMessage() {
        //TODO 发送消息：6.2、发送本地图片消息
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image = new BmobIMImageMessage("/storage/emulated/0/netease/cloudmusic/网易云音乐相册/小梦大半_1371091013186741.jpg");
        image.setExtra("[图片]");
        mConversationManager.sendMessage(image, listener);
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage() {
        //TODO 发送消息：6.3、发送远程图片消息
        BmobIMImageMessage image = new BmobIMImageMessage();
        image.setRemoteUrl("https://avatars3.githubusercontent.com/u/11643472?v=4&u=df609c8370b3ef7a567457eafd113b3ba6ba3bb6&s=400");
        image.setExtra("[图片]");
        mConversationManager.sendMessage(image, listener);
    }


    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        //TODO 发送消息：6.5、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
        audio.setExtra("[语音]");
        //设置语音文件时长：可选
        audio.setDuration(length);
        mConversationManager.sendMessage(audio, listener);
    }


    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Log.i(TAG, "onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.add(msg);
            binding.editMsg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            binding.editMsg.setText("");
            scrollToBottom();
            if (e != null) {
                ToastUtil.show(ChatActivity.this, e.getMessage());
            }
        }
    };

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversationManager.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                binding.swRefresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addAll(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    ToastUtil.show(ChatActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }


    //TODO 消息接收：8.2、单个页面的自定义接收器
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.i(TAG, "聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        if (event == null) {
            return;
        }
        BmobIMMessage msg = event.getMessage();
        if (mConversationManager != null && mConversationManager.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                adapter.add(msg);
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            Log.i(TAG, "不是与当前聊天对象的消息");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.layoutMore.getVisibility() == View.VISIBLE) {
                binding.layoutMore.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        KeyboardUtil.hideSoftInputView(ChatActivity.this);
        if (adapter.getItemCount() == 0) {
            return;
        }
        BmobIMMessage bmobIMMessage = adapter.getItem(adapter.getItemCount() - 1);
        ChatDataDao dataDao = ChatDataDao.getInstance(ChatActivity.this);
        String text = bmobIMMessage.getMsgType().equals(BmobIMMessageType.TEXT.getType()) ?
                bmobIMMessage.getContent() :
                (bmobIMMessage.getMsgType().equals(BmobIMMessageType.IMAGE.getType()) ? "[照片]" : "[语音]");
        if (!dataDao.isUserExist(bmobIMMessage.getConversationId())) {
            dataDao.addChatDB(mConversationManager.getConversationTitle()
                    , bmobIMMessage.getConversationId()
                    , bmobIMMessage.getCreateTime()
                    , mConversationManager.getConversationIcon()
                    , text);
        } else {
            dataDao.updateLastWord(mConversationManager.getConversationTitle()
                    , bmobIMMessage.getConversationId()
                    , bmobIMMessage.getCreateTime()
                    , mConversationManager.getConversationIcon()
                    , text);
        }
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null) {
            mConversationManager.updateLocalCache();
        }
        super.onDestroy();
    }

}
