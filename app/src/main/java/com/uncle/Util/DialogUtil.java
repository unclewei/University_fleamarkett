package com.uncle.Util;

import android.app.AlertDialog;
import android.content.Context;

import com.uncle.administrator.fleamarket.R;

/**
 * Created by Administrator on 2018/4/15 0015.
 */

public class DialogUtil {

    private static DialogUtil dialogUtil;

    public static DialogUtil getInstance(Context context) {
        if (dialogUtil == null) {
            synchronized (DialogUtil.class) {
                if (dialogUtil == null) {
                    dialogUtil = new DialogUtil(context);
                }
            }
        }
        return dialogUtil;
    }

    private AlertDialog dialog;
    private Context context;

    private DialogUtil(Context context) {
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_loading);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
