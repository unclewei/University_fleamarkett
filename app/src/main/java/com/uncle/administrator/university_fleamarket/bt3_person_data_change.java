package com.uncle.administrator.university_fleamarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uncle.bomb.BOMB_openhelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class bt3_person_data_change extends Activity {
    private LinearLayout icon, name, school;
    private ImageView hand;
    private TextView text_name, text_school;
    private Button no, yes;
    private String urlpath, myorganization;//头像存储的地方
    private String college;
    private String objectid;//本人id
    private  ProgressDialog m_pDialog; //声明进度条对话框
    private static final int REQUESTCODE_PICK = 784847001;
    private static final int REQUESTCODE_CUTTING = 784847002;
    private static final int SCHOOL_NAME = 784847003;
    private static final int ORGANIXZATION = 784847004;
    private static final int BTUUON_SURE = 784847005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_3_person_data_change);
        init();
    }

    private void init() {
        icon = (LinearLayout) findViewById(R.id.person_data_change_icon);
        name = (LinearLayout) findViewById(R.id.person_data_change_name);
        school = (LinearLayout) findViewById(R.id.person_data_change_school);
        hand = (ImageView) findViewById(R.id.person_data_change_icon_hand);
        text_name = (TextView) findViewById(R.id.person_data_change_text_name);
        text_school = (TextView) findViewById(R.id.person_data_change_text_school);
        no = (Button) findViewById(R.id.person_data_change_button_no);
        yes = (Button) findViewById(R.id.person_data_change_button_yes);
        click_name_and_school();
        get_data_from_sharepereference();
        change_icon();
        yes_onclick();
        no_onclick();
    }

    private void get_data_from_sharepereference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        myorganization = sharedPreferences.getString("organization", null);
        college = sharedPreferences.getString("college", null);
        objectid = sharedPreferences.getString("object_id", null);
        String school = college + "·" + myorganization;
        String name = sharedPreferences.getString("nick_name", null);
        urlpath = sharedPreferences.getString("head_portrait_adress", null);
        text_name.setText(name);
        text_school.setText(school);
    }

    //姓名和 学校两栏的点击事件
    private void click_name_and_school() {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCenterPopupWindow_naem(name);
            }
        });
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCenterPopupWindow_school(school);
            }
        });
    }

    //菊花转圈圈
    private void wait_look(){
        m_pDialog= ProgressDialog.show(bt3_person_data_change.this, null, "修改中…");
        new Thread(){
            @Override
            public void run() {
                super.run();
                updata_data_on_bomb();

            }
        }.start();

    }


    //确认按钮
    private void yes_onclick() {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wait_look();
                change_shareperference_data();

            }
        });

    }

    private void no_onclick(){
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //修改shareperference的数据
    private void change_shareperference_data(){
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nick_name", text_name.getText().toString().trim());
        editor.putString("college", college);
        editor.putString("organization", myorganization);
        editor.putString("head_portrait_adress",urlpath);
        editor.putString("refresh","ture");
        editor.commit();

    }

    //联网到云端保存数据
    private void updata_data_on_bomb(){
        BOMB_openhelper bomb = new BOMB_openhelper();
        bomb.upload_head_portrait(objectid, college, myorganization, urlpath, text_name.getText().toString().trim(), new BOMB_openhelper.Login_update_school_callback() {
            @Override
            public void done() {
                viewHandler.sendEmptyMessage(0);
                Intent intent = new Intent(bt3_person_data_change.this,MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void fail() {
                Toast.makeText(bt3_person_data_change.this,"网络不好，上传失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //选择照片
    private void change_icon() {
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUESTCODE_PICK);
            }
        });
    }

    //剪裁头像
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    //剪裁返回头像与保存文件
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = saveFile(this, "", "temphead.jpg", photo);
            hand.setImageDrawable(drawable);
        }
    }

    /**
     * 保存字节数组文件
     *
     * @param c        上下文
     * @param filePath 保存文件路径
     * @param fileName 文件名称
     * @return Added by Bao guangpu on 2016/4/10
     */
    public static String saveFile(Context c, String filePath, String fileName, Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/JiaXT/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    @Override//返回值做工
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_PICK:
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }

    /**
     * 中间弹出PopupWindow_for_name
     * <p>
     * 设置PopupWindow以外部分有一中变暗的效果
     *
     * @param view parent view
     */
    public void showCenterPopupWindow_naem(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.bt3_person_data_change_popupwindow_name, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText et = (EditText) contentView.findViewById(R.id.popupwindow_name_et);
        Button bt = (Button) contentView.findViewById(R.id.popupwindow_name_btoon);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().length() != 0) {
                    text_name.setText(et.getText().toString().trim());
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 中间弹出PopupWindow_for_school
     * <p>
     * 设置PopupWindow以外部分有一中变暗的效果
     *
     * @param view parent view
     */
    public void showCenterPopupWindow_school(View view) {
        final String[] mItems1, mItems2, mItems3, mItems4;
        View contentView = LayoutInflater.from(this).inflate(R.layout.bt3_person_data_change_popupwindow_school, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Spinner spinner1 = (Spinner) contentView.findViewById(R.id.popupwindow_school_spinner1);
        final Spinner spinner2 = (Spinner) contentView.findViewById(R.id.popupwindow_school_spinner2);
        final Spinner spinner3 = (Spinner) contentView.findViewById(R.id.popupwindow_school_spinner3);
        final Spinner spinner4 = (Spinner) contentView.findViewById(R.id.popupwindow_school_spinner4);
        Button bt = (Button) contentView.findViewById(R.id.popupwindow_school_buttton);
        mItems1 = getResources().getStringArray(R.array.spinnername1);
        mItems2 = getResources().getStringArray(R.array.spinnername2);
        mItems3 = getResources().getStringArray(R.array.spinnername3);
        mItems4 = getResources().getStringArray(R.array.spinnername4);

        ArrayAdapter<String> _Adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems1);
        ArrayAdapter<String> _Adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems2);
        ArrayAdapter<String> _Adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems3);
        ArrayAdapter<String> _Adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems4);
        spinner1.setAdapter(_Adapter1);
        spinner2.setAdapter(_Adapter2);
        spinner3.setAdapter(_Adapter3);
        spinner4.setAdapter(_Adapter4);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });//省

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });//市

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message msg = new Message();
                msg.what = SCHOOL_NAME;
                msg.obj = mItems3[position];
                viewHandler.sendMessage(msg);
                Log.i("textview改变了的值是",mItems3[position]);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                college = mItems3[0];
                parent.setVisibility(View.VISIBLE);
            }
        });//学校

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message msg2 = new Message();
                msg2.what = ORGANIXZATION;
                msg2.obj = mItems4[position];
                Log.i("textview改变了的值是",mItems4[position]);
                viewHandler.sendMessage(msg2);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                myorganization = mItems4[0];
                parent.setVisibility(View.VISIBLE);
            }
        });//学院

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = BTUUON_SURE;
                viewHandler.sendMessage(msg);

                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    //消息处理机制
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            m_pDialog.dismiss();// 关闭ProgressDialog
            super.handleMessage(msg);
            switch (msg.what){
                case SCHOOL_NAME:
                    college = (String) msg.obj;
                    Log.i("textview改变了的值是",college);
                    break;
                case ORGANIXZATION:
                    myorganization = (String) msg.obj;
                    Log.i("textview改变了的值是",myorganization);
                    break;
                case BTUUON_SURE:
                    String school = college + "·" + myorganization;
                    text_school.setText(school);
                    Log.i("textview改变了的值是",school);
                    break;
            }
        }
    };
}
