package com.uncle.Util;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author unclewei
 * @Data 2018/3/6.
 */

public class KeyboardUtil {

    public static void openKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void closeKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftInputView(Activity activity) {
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInputView(Activity activity, View view) {
        if (activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(view, 0);
        }
    }
}
