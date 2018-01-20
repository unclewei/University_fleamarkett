package com.uncle.administrator.university_fleamarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.uncle.administrator.university_fleamarket.Login_Activity.welcome_page;
import com.uncle.bomb.BOMB_openhelper;
import com.uncle.database.Chat_data_Dao;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 *
 * @author Administrator
 * @date 2017/4/12 0012
 */

public class ChatActivity extends Activity {

    private static final int CHAT_ACTIVITY_CHAT_DATA = 784847001;
    private static final int CHAT_ACTIVITY_CHAT_A_TELL_B = 784847002;
    private static final int CHAT_ACTIVITY_CHAT_B_TELL_A = 784847003;
    private static final int CHAT_ACTIVITY_CHAT_DATA_CHANGE = 784847004;
    private static final int CHAT_ACTIVITY_ACCOUNT_NAME = 784847005;
    private static final int CHAT_ACTIVITY_ACCOUNT_HEAD = 784847006;
    private Button send;
    private EditText chat_editText;
    private LinearLayout head_LinearLayout, chat_LinearLayout;
    private TextView head_name;
    private ScrollView scrollView;
    private String myobject, target_object;//对话双方的objecj_ID
    private BOMB_openhelper bomb = new BOMB_openhelper();
    private String b_tell_a, a_tell_b;
    private BmobRealTimeData rtd;//实时监控
    private Chat_data_Dao chat_data_dao;//本地聊天数据库
    private String target_name, target_head =null, head_portrait;//目的用户的头像和名字,头像


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_layout);
        init();
    }

    public void init() {
        send = (Button) findViewById(R.id.conversation_send);
        chat_editText = (EditText) findViewById(R.id.conversation_chat_data);
        head_LinearLayout = (LinearLayout) findViewById(R.id.conversation_head_LinearLayout);
        chat_LinearLayout = (LinearLayout) findViewById(R.id.conversation_chat_LinearLayout);
        scrollView = (ScrollView) findViewById(R.id.conversation_chat_ScrollView);
        head_name = (TextView) findViewById(R.id.conversation_name);
        chat_data_dao = new Chat_data_Dao(ChatActivity.this);



        get_data_from_sharepreference_and_Intent();
        find_chat_change_objectID(myobject, target_object);
        send();
        find_chat_data_from_bomb();
    }

    //开启sharedPreferences，并且把最后一句存入里面，并关闭
    private void open_sharedPreferences_add_last_Chat_and_close(String last_chat){
        SharedPreferences sharedPreferences = getSharedPreferences("chat_data",Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last_chat", last_chat);
        editor.commit();
    }



    //从sharepreference中获取myobject，从intent中获取target_object
    private void get_data_from_sharepreference_and_Intent() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        if (sharedPreferences.getString("object_id", "没有id").equals("没有id")) {
            Intent intent = new Intent(ChatActivity.this, welcome_page.class);
            startActivity(intent);
            finish();
        } else {
            myobject = sharedPreferences.getString("object_id", "没有id");
//            head_portrait = sharedPreferences.getString("head_portrait_adress", null);
//            Log.i("头像的地址是",head_portrait);
//            if (head_portrait !=null){
//                File file = new File(this.getCacheDir(),head_portrait);// 保存文件
//                drawable2 =  Drawable.createFromPath(file.toString());
//            }

        }
        Intent intent = getIntent();
        if (intent != null) {
            target_object = intent.getStringExtra("owner");
            find_account_data_from_bomb(target_object);
        }
    }

    //发送按钮，发送后更新数据库中的内容，更新a_tell_b的数字
    public void send() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat_context = chat_editText.getText().toString().trim();
                if (chat_context.isEmpty()) {
                    return;
                } else {
                    add_LinearLayout_Right(chat_context);
                    open_sharedPreferences_add_last_Chat_and_close(chat_context);
//                    if (a_tell_b == null) {
//                        //从数据库中找到该a_tell_b的值
//                        find_chat_change_objectID(myobject, target_object);
//                        bomb.update_name_B(a_tell_b);
//                    } else {
                    bomb.update_name_B(a_tell_b);
//                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    bomb.add_talk_data(myobject + target_object, chat_context, myobject, new BOMB_openhelper.Talk_Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFail() {
                        }
                    });
                }
                chat_editText.setText(null);
            }
        });
    }

    //监控，如果b_tell_a有变化，就更新ui，显示文字出来
    public void im_spy_in_time() {
        rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Log.i("bmob", "(" + data.optString("action") + ")" + "数据：" + data);
                Log.i("bmob", "拉拉阿里啦啦啦");
                Message message = new Message();
                message.what = CHAT_ACTIVITY_CHAT_DATA_CHANGE;
                handler.sendMessage(message);
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                Log.i("bmob", "连接成功:" + rtd.isConnected());
                Log.i("bmob", "objectId是成功:" + b_tell_a);
                if (rtd.isConnected()) {
                    rtd.subRowUpdate("IM_conversation", b_tell_a);
                }
            }
        });
    }

    //用objectID获取用户的数据
    public void find_account_data_from_bomb(String target_object) {
        bomb.find_account_data_alone(target_object, new BOMB_openhelper.Find_account_data_alone_callback() {
            @Override
            public void onSuccess(String name, String head) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_ACCOUNT_NAME;
                message.obj = name;
                handler.sendMessage(message);
                Message message2 = new Message();
                message2.what = CHAT_ACTIVITY_ACCOUNT_HEAD;
                message2.obj = head;
                handler.sendMessage(message2);

            }
        });
    }

    //找到关联聊天变化的objectID，获取，a_tell_b的objectID，和b_tell_a的objectID
    public void find_chat_change_objectID(String myobject, String target_object) {
        bomb.find_talk_data_change_object_id_alone(myobject + target_object, new BOMB_openhelper.Find_talk_data_change_object_id_callback() {
            @Override
            public void onSuccess(String object_id) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_CHAT_A_TELL_B;
                message.obj = object_id;
                handler.sendMessage(message);
            }
        });
        bomb.find_talk_data_change_object_id_alone(target_object + myobject, new BOMB_openhelper.Find_talk_data_change_object_id_callback() {
            @Override
            public void onSuccess(String object_id) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_CHAT_B_TELL_A;
                message.obj = object_id;
                handler.sendMessage(message);
            }
        });
    }

    //获取聊天的两个人聊天的内容，如果没有，就创建监听聊天的a_tell_b 和 b_tell_a 的ID
    public void find_chat_data_from_bomb() {
        bomb.find_talk_data(myobject, target_object, new BOMB_openhelper.Find_talk_callback() {
            @Override
            public void onSuccess(ArrayList<HashMap<String, String>> arrayList) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_CHAT_DATA;
                message.obj = arrayList;
                handler.sendMessage(message);
                Log.i("bmob_find_talk_data", "success");
            }

            @Override
            public void onFail() {
                Log.i("bmob_find_talk_data", "第一次失败");
                bomb.add_talk_data_change(myobject, target_object, new BOMB_openhelper.Add_talk_data_change_callback() {
                    @Override
                    public void onSueecss_b_tell_a(String object) {
                        Message message = new Message();
                        message.what = CHAT_ACTIVITY_CHAT_A_TELL_B;
                        message.obj = object;
                        handler.sendMessage(message);
                        Log.i("bmob_find_talk_data", "成功创建a_b");
                    }

                    @Override
                    public void onSueecss_a_tell_b(String object) {
                        Message message = new Message();
                        message.what = CHAT_ACTIVITY_CHAT_B_TELL_A;
                        message.obj = object;
                        handler.sendMessage(message);
                        Log.i("bmob_find_talk_data", "成功创建b_a");
                    }
                });
            }
        });
    }

    //把获取的数据通过左边加入一个框和右边加入一个框，完成文字的显示出来
    public void set_chat_data(ArrayList<HashMap<String, String>> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, String> hashMap = arrayList.get(i);
            if (hashMap.get("who_say").equals(myobject)) {
                add_LinearLayout_Right(hashMap.get("context"));
            } else if (hashMap.get("who_say").equals(target_object)) {
                add_LinearLayout_left(hashMap.get("context"));
            }
            if (i == arrayList.size()-1){//最后一句放入临时存储中
                open_sharedPreferences_add_last_Chat_and_close(hashMap.get("context"));
            }
        }
    }

    //增加一个右边的聊天条
    public void add_LinearLayout_Right(String context) {
        LinearLayout linearLayout = new LinearLayout(ChatActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams.gravity = Gravity.RIGHT;
        linearLayout.setLayoutParams(layoutParams);
        layoutParams.setMargins(0,3,3,3);
        chat_LinearLayout.addView(linearLayout);

        View view = new View(ChatActivity.this);
        LinearLayout.LayoutParams layoutParams_view = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_view.weight = 1;
        view.setLayoutParams(layoutParams_view);
        linearLayout.addView(view);

        TextView textView = new TextView(ChatActivity.this);
        LinearLayout.LayoutParams layoutParams_text = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams_text.weight = 1;
        layoutParams_text.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams_text);
        textView.setText(context);
        textView.setBackgroundResource(R.drawable.right_tall_min);
        textView.setPadding(5,3,20,3);
        linearLayout.addView(textView);
    }

    //增加一个左边的聊天条
    public void add_LinearLayout_left(String context) {
        LinearLayout linearLayout = new LinearLayout(ChatActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.setMargins(3,3,0,3);
        linearLayout.setLayoutParams(layoutParams);
        chat_LinearLayout.addView(linearLayout);

        TextView textView = new TextView(ChatActivity.this);
        LinearLayout.LayoutParams layoutParams_text = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams_text.weight = 1;
        layoutParams_text.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams_text);
        textView.setBackgroundResource(R.drawable.left_tall_min);
        textView.setText(context);
        textView.setPadding(20,3,5,3);
        linearLayout.addView(textView);
    }

    //从本地数据库中查找有无该对话用户的数据，有的话就更新时间和最后一句，没有的话就创建一条进入数据库中
    private void add_chat_data_to_chat_database() {
        chat_data_dao.find_data_from_Chat_database(target_object,new Chat_data_Dao.Find_data_from_Chat_database_callback() {
            @Override
            public void onFail() {
                String timing =get_system_time();
                String last_chat = get_last_chat();
                chat_data_dao.add_one_data_to_Chat_database(target_name, target_object, target_head, timing, last_chat);
            }

            @Override
            public void onSuccess() {
                //更新时间和最后一句
                String timing =get_system_time();
                String last_chat = get_last_chat();
                chat_data_dao.update_data_from_database(target_object,timing,target_head,  last_chat);
            }
        });
    }

    //获取最后一个句子
    private String get_last_chat(){
        SharedPreferences sharedPreferences = getSharedPreferences("chat_data", Context.MODE_PRIVATE);
        String last_chat = sharedPreferences.getString("last_chat", "...");
        return last_chat;
    }

    //获取系统时间
    private String get_system_time(){
        Time time = new Time();
        time.setToNow();// 取得系统时间。
        int hour = time.hour;
        int min = time.minute;
        int month = time.month+1;
        int day = time.monthDay;
        String timing = month +"/"+day+"  "+hour + ":" + min;
        Log.i("时间 是多少",timing);
        return timing;
    }

    //消息处理机制
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {//下拉刷新的消息处理
                case CHAT_ACTIVITY_CHAT_DATA:
                    ArrayList<HashMap<String, String>> arrayList = (ArrayList<HashMap<String, String>>) msg.obj;
                    if (arrayList != null) {
                        set_chat_data(arrayList);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                    break;
                case CHAT_ACTIVITY_CHAT_B_TELL_A:
                    b_tell_a = (String) msg.obj;
                    im_spy_in_time();
                    break;
                case CHAT_ACTIVITY_CHAT_A_TELL_B:
                    a_tell_b = (String) msg.obj;
                    break;
                case CHAT_ACTIVITY_CHAT_DATA_CHANGE:
                    find_chat_data_from_bomb();
                    break;
                case CHAT_ACTIVITY_ACCOUNT_NAME:
                    target_name = (String) msg.obj;
                    head_name.setText(target_name);
                    break;
                case CHAT_ACTIVITY_ACCOUNT_HEAD:
                    target_head = (String) msg.obj;
                    break;

                default:
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        add_chat_data_to_chat_database();
        rtd.unsubRowDelete("IM_conversation", b_tell_a);
    }
}
