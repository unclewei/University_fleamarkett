package com.uncle.method;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author unclewei
 * @Data 2018/3/6.
 */

public class KeyboardUtil {

    public static void openKeyBoard(Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static void closeKeyBoard(Activity activity,View view){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
