package com.uncle.Util;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class CommUtil {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
