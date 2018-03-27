package com.uncle.administrator.fleamarket.chat;

import android.content.Context;
import android.content.Intent;
import android.databinding.repacked.google.common.eventbus.EventBus;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 *
 * @author Administrator
 * @date 2018/3/11 0011
 */

public class ChatMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
    }
}