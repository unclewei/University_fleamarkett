package com.uncle.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Administrator
 * @date 2018/4/15 0015
 */

public class SPUtil {
    private static SPUtil spUtil;
    private Context context;

    public static SPUtil getInstance(Context context) {
        if (spUtil == null) {
            synchronized (SPUtil.class) {
                if (spUtil == null) {
                    spUtil = new SPUtil(context);
                }
            }
        }
        return spUtil;
    }

    public SPUtil(Context context) {
        this.context = context;
    }

    public void saveSP(String key, String date) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, date);
        editor.commit();
    }

    public String getData(String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
