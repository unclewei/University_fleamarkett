package com.uncle.administrator.fleamarket;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uncle.database.Chat_data_Dao;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class TalkFragment extends Fragment {
    private LinearLayout base_linearLayout;
    private Chat_data_Dao chat_dao;
    private Drawable drawable2;
    private LinearLayout linearLayout;//创建于显示聊天条的控件
    private View view;//添加聊天下划线区分的空间
    private String name,target_id,time,head_portrait,last_sentence;//获取目标的属



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.the_base_button_2, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init() {
        base_linearLayout = (LinearLayout) getActivity().findViewById(R.id.the_base_button_2_base_LinearLayout);
        chat_dao = new Chat_data_Dao(getContext());


    }

    private void get_data_from_chat_database() {
        ArrayList<HashMap<String, Object>> arrayList = chat_dao.find_and_get_data_from_Chat_database();
        if (arrayList!= null) {
            for (int i = 0; i < arrayList.size(); i++) {
                HashMap<String, Object> hashMap = arrayList.get(i);
                 name = String.valueOf(hashMap.get("name"));
                 target_id = String.valueOf(hashMap.get("target_objectID"));
                 head_portrait = String.valueOf(hashMap.get("head_portrait"));
                 time = String.valueOf(hashMap.get("time"));
                 last_sentence = (String) hashMap.get("last_sentence");
                get_head_protrait(head_portrait);


            }
        }
    }

//获取头像
    private void get_head_protrait( String head_portrait){
        if (head_portrait.length() ==0){
            drawable2 = getContext().getDrawable(R.drawable.head);
        }else {

            addLinearLayoutLeft(name, last_sentence, target_id, head_portrait,time);

        }


    }


    //把控件清除
    private void hide_linearlayout() {
        if (linearLayout !=null) {
            linearLayout.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    //增加聊天条
    public void addLinearLayoutLeft(String name, String last_sentence, String target_ID, String  headUrl, String time) {
        linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        layoutParams.gravity = Gravity.LEFT;
        linearLayout.setLayoutParams(layoutParams);
        base_linearLayout.addView(linearLayout);

        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams_image = new LinearLayout.LayoutParams(
                150, 150);
        layoutParams_image.gravity = Gravity.CENTER_VERTICAL;
        layoutParams_image.rightMargin = 15;
        imageView.setLayoutParams(layoutParams_image);
        Glide.with(this).load(headUrl).into(imageView);
        linearLayout.addView(imageView);

        LinearLayout linearLayout2 = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.weight = 1;
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        layoutParams2.gravity = Gravity.CENTER;
        linearLayout2.setLayoutParams(layoutParams2);
        linearLayout.addView(linearLayout2);
        //time
        TextView textView_time = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams_text_time = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_text_time.gravity = Gravity.RIGHT;
        textView_time.setLayoutParams(layoutParams_text_time);
        textView_time.setText(time);
        textView_time.setTextColor(Color.rgb(0, 0, 0));
        textView_time.setTextSize(12);
        linearLayout.addView(textView_time);
        //name
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams_text = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams_text);
        textView.setText(name);
        textView.setTextColor(Color.rgb(0, 0, 0));
        textView.setTextSize(20);
        linearLayout2.addView(textView);

        //last_sentence
        TextView textView_last_sentence = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams_text_last_sentence = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView_last_sentence.setLayoutParams(layoutParams_text_last_sentence);
        textView_last_sentence.setText(last_sentence);
        textView_last_sentence.setTextColor(Color.rgb(128, 128, 128));
        textView_last_sentence.setTextSize(12);
        linearLayout2.addView(textView_last_sentence);

        view = new View(getContext());
        LinearLayout.LayoutParams layoutParams_view = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        view.setLayoutParams(layoutParams_view);
        view.setBackgroundColor(Color.rgb(0, 0, 0));
        layoutParams_view.bottomMargin = 3;
        layoutParams_view.topMargin = 3;
        base_linearLayout.addView(view);

        //target_id_不显示出来
        TextView textView_target_id = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams_target_id = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView_target_id.setLayoutParams(layoutParams_target_id);
        textView_target_id.setText(target_ID);
        textView_target_id.setVisibility(View.INVISIBLE);//不显示，但仍然站着内存
        linearLayout2.addView(textView_target_id);


        linearLayout_onclick(linearLayout2, textView_target_id);

    }

    //每一条的点击跳转事件和长按删除点击事件
    private void linearLayout_onclick(LinearLayout linearLayout2, final TextView tv) {
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target_ID = tv.getText().toString();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("owner", target_ID);
                startActivity(intent);
            }
        });
        linearLayout2.setLongClickable(true);
        linearLayout2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setPositiveButton("是否删除对话", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String target_ID = tv.getText().toString();
                                Toast.makeText(getContext(), "伤处对话框", Toast.LENGTH_SHORT).show();
                                Chat_data_Dao dao = new Chat_data_Dao(getContext());
                                dao.delete_by_targetID(target_ID);
                                hide_linearlayout();
                            }
                        })

                        .show();
                return false;
            }
        });
    }

    private void head_image_onclick(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入个人资料
            }
        });
    }

    @Override
    public void onStart() {
        get_data_from_chat_database();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        hide_linearlayout();
    }
}
