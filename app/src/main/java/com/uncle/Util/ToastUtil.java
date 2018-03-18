package com.uncle.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class ToastUtil {
    public static void show(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show();
    }
}
