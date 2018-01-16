package com.uncle.administrator.university_fleamarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.uncle.bomb.BOMB_openhelper;
import com.uncle.method.Info;
import com.uncle.method.MyAdapter.AsyncImageLoader;
import com.uncle.method.MyAdapter.NoScrollListView;
import com.uncle.method.PhotoView;
import com.uncle.method.get_internet_image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/11 0011.
 */

public class bt1_intent_to_context extends Activity {
    private TextView title, price, zan_nub,organization,name;//标题，价格，点赞数，学院
    private ImageView img1, img2, img3, img4 = null, img5 = null, img6 = null,icon,call;
    private get_internet_image internew_image;//获取网络图片的方法
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
    private String img1_path, img2_path, img3_path, img4_path = null, img5_path = null, img6_path = null;
    private String objectID, new_objectID;//1.获取的数据库中的id。2.新生成的数据的objectid。
    private BOMB_openhelper bomb = new BOMB_openhelper();
    private String owner;//该条目的发出人objectid
    private String myobject,myorganization,head_portrait,myname;//本地存取的自己的objectid,学院
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

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
        internew_image = new get_internet_image();



        initImageLoader();

        get_data_from_shareperecence();
        get_intent_message();
        find_account_data_from_bomb(owner);

        img_click();
        ComMent();
        zan_click();
        head_click();
    }

    private void get_data_from_shareperecence(){
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        myobject = sharedPreferences.getString("object_id", "没有id");
        myorganization = sharedPreferences.getString("organization",null);
        myname = sharedPreferences.getString("nick_name","匿名用户");
        organization.setText(myorganization);


    }

    //用objectID获取用户的数据
    public void find_account_data_from_bomb(String target_object) {
        Log.i("又来试验了target_object",target_object);
        bomb.find_account_data_alone(target_object, new BOMB_openhelper.Find_account_data_alone_callback() {
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
    private void get_head_protrait( String head_portrait){
        if (head_portrait.length() ==0){
            icon.setImageResource(R.drawable.head);
        }else {
            AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
            asyncImageLoader.loadDrawable(this, 1, head_portrait, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoad(Integer t, Drawable drawable) {
                   icon.setImageDrawable(drawable);
                }

                @Override
                public void onError(Integer t) {
                    icon.setImageResource(R.drawable.head);
                }
            });
        }


    }

    private void head_click(){
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(bt1_intent_to_context.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(bt1_intent_to_context.this, chat_activity.class);
                    intent.putExtra("owner", owner);
                    startActivity(intent);
                }
            }
        });
        head_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(bt1_intent_to_context.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                Intent intent = new Intent(bt1_intent_to_context.this,chat_activity.class);
                intent.putExtra("owner",owner);
                startActivity(intent);
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.equals(myobject)){
                    Toast.makeText(bt1_intent_to_context.this,"不能自己与自己聊天",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(bt1_intent_to_context.this,chat_activity.class);
                    intent.putExtra("owner",owner);
                    startActivity(intent);
                }
            }
        });
    }


    private void path_for_click(Intent intent, int a) {
        intent.putExtra("img1_path", img1_path);
        intent.putExtra("img2_path", img2_path);
        intent.putExtra("img3_path", img3_path);
        intent.putExtra("nub", a);
    }//缩减代码，点击中的一部分

    public void img_click() {

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bt1_intent_to_context.this, bt1_ViewPagerActivity.class);
                path_for_click(intent, 0);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bt1_intent_to_context.this, bt1_ViewPagerActivity.class);
                path_for_click(intent, 1);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bt1_intent_to_context.this, bt1_ViewPagerActivity.class);
                path_for_click(intent, 2);
                startActivity(intent);
            }
        });
        if (img4 != null) {
            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(bt1_intent_to_context.this, bt1_ViewPagerActivity.class);
                    path_for_click(intent, 4);
                    intent.putExtra("img4_path", img4_path);
                    startActivity(intent);

                }
            });
        }
    }//图片的点击方法，跳转到viewpagerActivity

    public void zan_click() {
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zan_ro_not_zan ==true) {
                    Log.i("aaa00000", zan_nub.getText().toString());
                    Log.i("aaa00000", "这里没有赞");
                    int nub = Integer.parseInt(zan_nub.getText().toString()) + 1;
                    zan_nub.setText( nub+"");
                    zan.setBackgroundResource(R.drawable.love2);
                    bomb.update_zan(objectID, nub);
                    zan_ro_not_zan = false;
                }else if (zan_ro_not_zan == false) {
                    Log.i("aaa", zan_nub.getText().toString());
                    int nub = Integer.parseInt(zan_nub.getText().toString()) - 1;
                    zan_nub.setText(nub + "");
                    zan.setBackgroundResource(R.drawable.love1);
                    bomb.update_zan(objectID, nub);

                    zan_ro_not_zan = true;
                }
            }
        });
    }//点赞，加一

    private void initImageLoader(){
          imageLoader  = ImageLoader.getInstance();//初始化
          options =  new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_loading) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.img_loading) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.img_loading) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                    .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build(); // 构建完成
    }

    //从上一个页面获得信息，intent
    public void get_intent_message() {
        Intent intent = getIntent();
        if (intent != null) {
            objectID = intent.getStringExtra("objID");
            owner = intent.getStringExtra("owner_id");
            BOMB_openhelper bomb = new BOMB_openhelper();

            bomb.find_alone(objectID, new BOMB_openhelper.ImageCallback() {
                @Override
                public void onImageLoad(final HashMap<String, String> map) {
                    title.setText(map.get("title"));
                    price.setText(map.get("price"));
                    zan_nub.setText(map.get("zan_nub"));
                    imageLoader.displayImage(map.get("image1"),img1, options);
                    img1_path =  imageLoader.getDiscCache().get(map.get("image1")).getPath();
                    imageLoader.displayImage(map.get("image2"),img2, options);
                    img2_path =  imageLoader.getDiscCache().get(map.get("image2")).getPath();
                    imageLoader.displayImage(map.get("image3"),img3, options);
                    img3_path =  imageLoader.getDiscCache().get(map.get("image3")).getPath();
//                    img1.setImageDrawable(internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image1")));
//                    img1_path = internew_image.get_file_path().toString();
//                    img2.setImageDrawable(internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image2")));
//                    img2_path = internew_image.get_file_path().toString();
//                    img3.setImageDrawable(internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image3")));
//                    img3_path = internew_image.get_file_path().toString();

                    if (map.get("image4") != null) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (map.get("image4").contains(".jpg")||map.get("image4").contains(".png")) {


//                                    Drawable d = internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image4"));
//                                    String img4_path = internew_image.get_file_path().toString();
                                    Bundle boundle = new Bundle();
                                    boundle.putString("path", map.get("image4"));
                                    Message message = new Message();
                                    message.setData(boundle);
//                                    message.obj = d;
                                    message.what = DEAL_WITH_IMAGE;
                                    handler.sendMessage(message);

                                }
                                super.run();
                            }
                        }.start();
                    }
                    if (map.get("image5") != null) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (map.get("image5").contains(".jpg")||map.get("image5").contains(".png")) {
//                                    imageLoader.displayImage(map.get("image5"),img5, options);
//                                    Drawable d = internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image5"));
                                    String img4_path = internew_image.get_file_path().toString();
                                    Bundle boundle = new Bundle();
                                    boundle.putString("path", img4_path);
                                    Message message = new Message();
                                    message.setData(boundle);
//                                    message.obj = d;
                                    message.what = DEAL_WITH_IMAGE;
                                    handler.sendMessage(message);
                                }
                                super.run();
                            }
                        }.start();
                    }
                    if (map.get("image6") != null) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (map.get("image5").contains(".jpg")||map.get("image5").contains(".png")) {
                                    imageLoader.displayImage(map.get("image6"),img6, options);
//                                    Drawable d = internew_image.loadImageFromNetworkmini(bt1_intent_to_context.this, map.get("image6"));
                                    String img4_path = internew_image.get_file_path().toString();
                                    Bundle boundle = new Bundle();
                                    boundle.putString("path", img4_path);
                                    Message message = new Message();
                                    message.setData(boundle);
//                                    message.obj = d;
                                    message.what = DEAL_WITH_IMAGE;
                                    handler.sendMessage(message);
                                }
                                super.run();
                            }
                        }.start();
                    }
                }

                @Override
                public void onError() {

                }
            });
            get_comment();//获取评论
        }
    }//从互联网中获取图片，评论。

    public void ComMent() {
        commemt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_text.getText().toString().isEmpty() && comment_reply) {
                    comment_text.setHint("快点就下你的评论吧");
                    String text_context = comment_text.getText().toString();
                    text_context = myname+" ：“" + text_context + "”";
                    get_new_textview(text_context);//生成一个新的TextView，并设置点击事件
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0); // 关闭软键盘
                    comment_text.setText(null);
                    Toast.makeText(bt1_intent_to_context.this, "评论成功", Toast.LENGTH_SHORT).show();
                    final String finalText_context = text_context;
                    new Thread() {
                        @Override
                        public void run() {
                            bomb.add_comment_zan(finalText_context, "name1", "name2", 0, objectID);
                            super.run();
                        }
                    }.start();

                } else if (!comment_text.getText().toString().isEmpty() && !comment_reply) {
                    comment_reply = true;
                    comment_text.setHint("回复用户");
                    String text_context = comment_text.getText().toString();
                    text_context = "   " +myname+ "  回复了" + "上面的用户" + ":“" + text_context + "”";

                    get_new_textview(text_context);//生成一个新的TextView，并设置点击事件
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0); // 关闭软键盘
                    comment_text.setText(null);
                    Toast.makeText(bt1_intent_to_context.this, "回复成功", Toast.LENGTH_SHORT).show();
                    final String finalText_context = text_context;
                    new Thread() {
                        @Override
                        public void run() {
                            bomb.add_comment_zan(finalText_context, "name1", "name2", 1, objectID);
                            super.run();
                        }
                    }.start();

                } else {
                    Toast.makeText(bt1_intent_to_context.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }    //评论commemt

    public void get_comment() {
        bomb.find_comment(objectID, new BOMB_openhelper.getCommentCallback() {
            @Override
            public void oncommentLoad(ArrayList<HashMap<String, String>> arrayList) {
                if (arrayList.size() != 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        HashMap<String, String> hashMap = arrayList.get(i);
                        String comment = hashMap.get("comment");
                        get_new_textview(comment);//生成一个新的TextView，并设置点击事件
                    }
                } else {

                    get_new_textview("还没有评论哦，快来做第一个！");
                }
            }
        });
    }//获取评论呢

    public void get_new_textview(String context) {
        TextView textView = new TextView(bt1_intent_to_context.this);
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
            InputMethodManager imm = (InputMethodManager) bt1_intent_to_context.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            comment_reply = false;
        }
    }//让编辑框弹出来，并显示对谁进行评论


    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEAL_WITH_IMAGE://如果数据库中存在第四幅图，那么开启新线程下载图片，然后就传送消息，在消息机制中更新界面。
                    ImageView  img4 = new ImageView(bt1_intent_to_context.this);
                    Bundle bundle = msg.getData();
                    String img4_path = (String) bundle.get("path");
                    imageLoader.displayImage(img4_path,img4, options);
                    img4_path =  imageLoader.getDiscCache().get(img4_path).getPath();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        img4.setLayoutParams(layoutParams);  //image的布局方式
                        layoutParams.bottomMargin = 8;
                        img4.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        img4.setAdjustViewBounds(true);
                        linearLayout.addView(img4, layoutParams);

                    break;

                case CHAT_ACTIVITY_ACCOUNT_NAME:
                    name.setText((String) msg.obj);
                    break;
                case CHAT_ACTIVITY_ACCOUNT_HEAD:
                    head_portrait = (String) msg.obj;
                    get_head_protrait(head_portrait);
                    break;
                default:
                    break;
            }
        }

    };


}
