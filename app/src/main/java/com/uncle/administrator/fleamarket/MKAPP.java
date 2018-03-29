package com.uncle.administrator.fleamarket;

import android.app.Application;
import android.content.Context;

import com.uncle.administrator.fleamarket.chat.ChatMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 *
 * @author Administrator
 * @date 2018/3/11 0011
 */

public class MKAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initBomb();
    }

    private void initBomb() {
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new ChatMessageHandler());
        }
    }

    /**
     * 获取当前运行的进程名
     *
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Context getContext(){
        return this;
    }
}
