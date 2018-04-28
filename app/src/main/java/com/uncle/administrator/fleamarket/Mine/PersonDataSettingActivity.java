package com.uncle.administrator.fleamarket.Mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.DTO.Profile;
import com.uncle.Util.DialogUtil;
import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.PersonDataSettingActivityBinding;
import com.uncle.bomb.BOMBOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2017/5/22 0022
 */

public class PersonDataSettingActivity extends BaseBindingActivity<PersonDataSettingActivityBinding> implements View.OnClickListener {
    private String college, organization, avaterUrl;
    private static final int REQUESTCODE_PICK = 784847001;
    private static final int REQUESTCODE_CUTTING = 784847002;
    private static final int SCHOOL_NAME = 784847003;
    private static final int ORGANIXZATION = 784847004;
    private static final int BTUUON_SURE = 784847005;

    @Override
    protected void bindData(PersonDataSettingActivityBinding dataBinding) {
        initData();
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.person_data_setting_activity;
    }

    private void init() {
        binding.name.setOnClickListener(this);
        binding.school.setOnClickListener(this);
        binding.btnComfig.setOnClickListener(this);
        binding.avater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.name) {
            popupWindowName(binding.name);
            return;
        }
        if (v == binding.school) {
            popupWindowSchool(v);
            return;
        }
        if (v == binding.btnComfig) {
            DialogUtil.getInstance(PersonDataSettingActivity.this).show();
            updateDataOnBomb();
            return;
        }
        if (v == binding.avater) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, REQUESTCODE_PICK);
        }
    }

    private void initData() {
        String school = profile.getCollege() + "." + profile.getOrganization();
        binding.tvSchool.setText(school);
        if (!TextUtils.isEmpty(profile.getName())) {
            binding.tvName.setText(profile.getName());
        }
        if (!TextUtils.isEmpty(profile.getAvatar())) {
            Glide.with(PersonDataSettingActivity.this)
                    .load(profile.getAvatar())
                    .into(binding.imgAvater);
        }
    }

    private void changeShareSharePreferenceData() {
        profile.setName(binding.tvName.getText().toString());
        profile.setCollege(college);
        profile.setOrganization(organization);
        profile.setAvatar(avaterUrl);
        saveMyAccountFromSharePerFences(profile);
    }

    private void updateDataOnBomb() {
        Profile profile = new Profile();
        profile.setCollege(college);
        profile.setOrganization(organization);
        profile.setAvatar(avaterUrl);
        profile.setName(binding.tvName.getText().toString());
        BOMBOpenHelper.getInstance().uploadHeadPortrait(this.profile.getObjectId(), profile, new BOMBOpenHelper.LoginUpdateSchoolCallback() {
            @Override
            public void done() {
                DialogUtil.getInstance(PersonDataSettingActivity.this).dismiss();
                changeShareSharePreferenceData();
                viewHandler.sendEmptyMessage(0);
                Intent intent = new Intent(PersonDataSettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void fail() {
                DialogUtil.getInstance(PersonDataSettingActivity.this).dismiss();
                Toast.makeText(PersonDataSettingActivity.this, "网络不好，上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            avaterUrl = saveFile(this, photo);
            binding.imgAvater.setImageDrawable(drawable);
        }
    }

    public static String saveFile(Context c, Bitmap bm) {
        String filePath = null;
        String fileName = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        fileName = dateFolder + ".jpg";
        try {
            filePath = Environment.getExternalStorageDirectory() + "/Tiaotiao/";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName));
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

    @Override
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
            default:
                break;
        }
    }

    public void popupWindowName(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window_name, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText et = contentView.findViewById(R.id.et_name);
        TextView config = contentView.findViewById(R.id.config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().length() != 0) {
                    binding.tvName.setText(et.getText().toString().trim());
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;
        getWindow().setAttributes(wlBackground);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    public void popupWindowSchool(View view) {
        final String[] mItems1, mItems2, mItems3, mItems4;
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window_school, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Spinner spinner1 = contentView.findViewById(R.id.popupwindow_school_spinner1);
        final Spinner spinner2 = contentView.findViewById(R.id.popupwindow_school_spinner2);
        final Spinner spinner3 = contentView.findViewById(R.id.popupwindow_school_spinner3);
        final Spinner spinner4 = contentView.findViewById(R.id.popupwindow_school_spinner4);
        TextView config = contentView.findViewById(R.id.config);
        mItems1 = getResources().getStringArray(R.array.spinnername1);
        mItems2 = getResources().getStringArray(R.array.spinnername2);
        mItems3 = getResources().getStringArray(R.array.spinnername3);
        mItems4 = getResources().getStringArray(R.array.spinnername4);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems4);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message msg = new Message();
                msg.what = SCHOOL_NAME;
                msg.obj = mItems3[position];
                viewHandler.sendMessage(msg);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message msg2 = new Message();
                msg2.what = ORGANIXZATION;
                msg2.obj = mItems4[position];
                viewHandler.sendMessage(msg2);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                organization = mItems4[0];
                parent.setVisibility(View.VISIBLE);
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
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
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;
        getWindow().setAttributes(wlBackground);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCHOOL_NAME:
                    college = (String) msg.obj;
                    break;
                case ORGANIXZATION:
                    organization = (String) msg.obj;
                    break;
                case BTUUON_SURE:
                    String school = college + "·" + organization;
                    binding.tvSchool.setText(school);
                    break;
                default:
                    break;
            }
        }
    };


}
