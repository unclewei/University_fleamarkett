package com.uncle.administrator.fleamarket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uncle.bomb.BOMBOpenHelper;
import com.uncle.bomb.CommentZan;
import com.uncle.bomb.shop_goods;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2017/3/11 0011
 */

public class GoodsDetailsActivity extends Activity {
    private TextView title, price, zan_nub,organization,name;//标题，价格，点赞数，学院
    private ImageView img1, img2, img3, img4 = null, img5 = null, img6 = null,icon,call;
    private LinearLayout linearLayout;//获取图片的LinearLayout
    private LinearLayout linearLayout_comment;//获取评论的LinearLayout
    private LinearLayout head_LinearLayout;//点击头部的LinearLayout，进入聊天页面
    private Button commemt, zan;//评论的按钮,聊天的按钮
    private EditText comment_text;//评论内容
    private Boolean comment_reply = true;//评论和回复的判断，true是评论，false是回复。
    private Boolean zan_ro_not_zan = true;//判断有没有赞。true是没有赞， false是已经赞了
    private final int DEAL_WITH_IMAGE = 5;
    private final int CHAT_ACTIVITY_ACCOUNT_NAME = 7;
    private final int CHAT_ACTIVITY_ACCOUNT_HEAD = 8;
    private String objectID, new_objectID;//1.获取的数据库中的id。2.新生成的数据的objectid。
    private BOMBOpenHelper bomb = new BOMBOpenHelper();
    private String owner;//该条目的发出人objectid
    private String myobject,myorganization,head_portrait,myname;//本地存取的自己的objectid,学院

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt1_listview_intent_to);
        init();

    }

    public void init() {
        comment_text = (EditText) findViewById(R.id.bt1_listview_intent_comment_text);
        commemt = (Button) findViewById(R.id.bt1_listview_intent_comment_bt);
        zan = (Button) findViewById(R.id.bt1_listview_intent_zan_bt);
        call = (ImageView) findViewById(R.id.bt1_listview_intent_call);
        zan_nub = (TextView) findViewById(R.id.bt1_listView_intent_zan_nub);
        title = (TextView) findViewById(R.id.bt1_listview_intent_title);
        price = (TextView) findViewById(R.id.bt1_listview_intent_price);
        name = (TextView) findViewById(R.id.bt1_listview_intent_name);
        organization = (TextView) findViewById(R.id.bt1_listview_intent_organization);
        img1 = (ImageView) findViewById(R.id.bt1_listview_intent_bt1);
        img2 = (ImageView) findViewById(R.id.bt1_listview_intent_bt2);
        img3 = (ImageView) findViewById(R.id.bt1_listview_intent_bt3);
        icon = (ImageView) findViewById(R.id.bt1_listview_intent_icon);
        linearLayout = (LinearLayout) findViewById(R.id.bt1_listview_intent_img_LinearLayout);
        head_LinearLayout = (LinearLayout) findViewById(R.id.bt1_listview_intent_name_LinearLayout);
        linearLayout_comment = (LinearLayout) findViewById(R.id.bt1_listview_intent_Linear_for_txet);

        getDataFromShareperecence();
        getIntentMessage();
        findAccountDataFromBomb(owner);

        img_click();
        comMent();
        zanClick();
        headClick();
    }

    private void getDataFromShareperecence(){
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        myobject = sharedPreferences.getString("object_id", "没有id");
        myorganization = sharedPreferences.getString("organization",null);
        myname = sharedPreferences.getString("nick_name","匿名用户");
        organization.setText(myorganization);


    }

    //用objectID获取用户的数据
    public void findAccountDataFromBomb(String target_object) {
        Log.i("又来试验了target_object",target_object);
        bomb.findAccountDataAlone(target_object, new BOMBOpenHelper.FindAccountDataAloneCallback() {
            @Override
            public void onSuccess(String name, String head) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_ACCOUNT_NAME;
                message.obj = name;
                Log.i("又来试验了name",name);
                handler.sendMessage(message);
                Message message2 = new Message();
                message2.what = CHAT_ACTIVITY_ACCOUNT_HEAD;
                message2.obj = head;
                Log.i("又来试验了head",head);
                handler.sendMessage(message2);

            }
        });
    }


    //获取头像
    private void getHeadProtrait(String headPortrait){
        if (headPortrait.length() ==0){
            icon.setImageResource(R.drawable.head);
        }else {
            Glide.with(this).load(headPortrait).into(icon);

        }


    }

    private void headClick(){
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(GoodsDetailsActivity.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(GoodsDetailsActivity.this, ChatActivity.class);
                    intent.putExtra("owner", owner);
                    startActivity(intent);
                }
            }
        });
        head_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(GoodsDetailsActivity.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                Intent intent = new Intent(GoodsDetailsActivity.this,ChatActivity.class);
                intent.putExtra("owner",owner);
                startActivity(intent);
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(GoodsDetailsActivity.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(GoodsDetailsActivity.this,ChatActivity.class);
                    intent.putExtra("owner",owner);
                    startActivity(intent);
                }
            }
        });
    }


    private void path_for_click(Intent intent, int a) {
//        intent.putExtra("img1_path", img1_path);
//        intent.putExtra("img2_path", img2_path);
//        intent.putExtra("img3_path", img3_path);
        intent.putExtra("nub", a);
    }//缩减代码，点击中的一部分

    public void img_click() {

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this, ViewPagerActivity.class);
                path_for_click(intent, 0);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this, ViewPagerActivity.class);
                path_for_click(intent, 1);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailsActivity.this, ViewPagerActivity.class);
                path_for_click(intent, 2);
                startActivity(intent);
            }
        });
    }//图片的点击方法，跳转到viewpagerActivity

    public void zanClick() {
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zan_ro_not_zan ==true) {
                    Log.i("aaa00000", zan_nub.getText().toString());
                    Log.i("aaa00000", "这里没有赞");
                    int nub = Integer.parseInt(zan_nub.getText().toString()) + 1;
                    zan_nub.setText( nub+"");
                    zan.setBackgroundResource(R.drawable.love2);
                    bomb.updateZan(objectID, nub);
                    zan_ro_not_zan = false;
                }else if (zan_ro_not_zan == false) {
                    Log.i("aaa", zan_nub.getText().toString());
                    int nub = Integer.parseInt(zan_nub.getText().toString()) - 1;
                    zan_nub.setText(nub + "");
                    zan.setBackgroundResource(R.drawable.love1);
                    bomb.updateZan(objectID, nub);

                    zan_ro_not_zan = true;
                }
            }
        });
    }//点赞，加一


    //从上一个页面获得信息，intent
    public void getIntentMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            objectID = intent.getStringExtra("objID");
            owner = intent.getStringExtra("owner_id");
            BOMBOpenHelper bomb = new BOMBOpenHelper();
            bomb.find_alone(objectID, new BOMBOpenHelper.ImageCallback() {
                @Override
                public void onImageLoad(shop_goods shopGoods) {
                    title.setText(shopGoods.getText());
                    price.setText(shopGoods.getTitle());
                    zan_nub.setText(shopGoods.getZan_nub());
                    Glide.with(GoodsDetailsActivity.this).load(shopGoods.getImage1()).into(img1);
                    Glide.with(GoodsDetailsActivity.this).load(shopGoods.getImage2()).into(img2);
                    Glide.with(GoodsDetailsActivity.this).load(shopGoods.getImage3()).into(img3);
                }

                @Override
                public void onError() {
                }
            });
            getComment();//获取评论
        }
    }//从互联网中获取图片，评论。

    public void comMent() {
        commemt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_text.getText().toString().isEmpty() && comment_reply) {
                    comment_text.setHint("快点就下你的评论吧");
                    String text_context = comment_text.getText().toString();
                    text_context = myname+" ：“" + text_context + "”";
                    getNewTextView(text_context);//生成一个新的TextView，并设置点击事件
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0); // 关闭软键盘
                    comment_text.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    final String finalTextContext = text_context;
                    final CommentZan commentZan = new CommentZan(finalTextContext, objectID, "name1", "name2", 0);
                    new Thread() {
                        @Override
                        public void run() {
                            bomb.addCommentZan(commentZan);
                            super.run();
                        }
                    }.start();

                } else if (!comment_text.getText().toString().isEmpty() && !comment_reply) {
                    comment_reply = true;
                    comment_text.setHint("回复用户");
                    String text_context = comment_text.getText().toString();
                    text_context = "   " +myname+ "  回复了" + "上面的用户" + ":“" + text_context + "”";

                    getNewTextView(text_context);//生成一个新的TextView，并设置点击事件
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0); // 关闭软键盘
                    comment_text.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    final String finalText_context = text_context;
                    final CommentZan commentZan = new CommentZan(finalText_context, objectID, "name1", "name2", 1);
                    new Thread() {
                        @Override
                        public void run() {
                            bomb.addCommentZan(commentZan);
                            super.run();
                        }
                    }.start();

                } else {
                    Toast.makeText(GoodsDetailsActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }    //评论commemt

    public void getComment() {
        bomb.find_comment(objectID, new BOMBOpenHelper.getCommentCallback() {
            @Override
            public void onCommentLoad(List<CommentZan> arrayList) {
                if (arrayList.size() != 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        CommentZan commentZan = arrayList.get(i);
                        String comment = commentZan.getComment();
                        getNewTextView(comment);//生成一个新的TextView，并设置点击事件
                    }
                } else {

                    getNewTextView("还没有评论哦，快来做第一个！");
                }
            }

        });
    }//获取评论呢

    public void getNewTextView(String context) {
        TextView textView = new TextView(GoodsDetailsActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText(context);
        linearLayout_comment.addView(textView);
        textView.setOnClickListener(new MyButtonListener());
    }//生成一个TextView并设置内容和点击事件

    private class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//让编辑框弹出来，并显示对谁进行评论
            comment_text.setFocusable(true);
            comment_text.setFocusableInTouchMode(true);
            comment_text.requestFocus();
            //打开软键盘
            InputMethodManager imm = (InputMethodManager) GoodsDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            comment_reply = false;
        }
    }//让编辑框弹出来，并显示对谁进行评论


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHAT_ACTIVITY_ACCOUNT_NAME:
                    name.setText((String) msg.obj);
                    break;
                case CHAT_ACTIVITY_ACCOUNT_HEAD:
                    head_portrait = (String) msg.obj;
                    getHeadProtrait(head_portrait);
                    break;
                default:
                    break;
            }
        }

    };


}
