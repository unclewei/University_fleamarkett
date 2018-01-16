package com.uncle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class chat_service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final BmobRealTimeData rtd = new BmobRealTimeData();
//        rtd.subRowUpdate("IM_conversation", objectId);
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Log.i("bmob", "(" + data.optString("action") + ")" + "数据：" + data);
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                Log.i("bmob", "连接成功:"+rtd.isConnected());
            }
        });
    }
}
