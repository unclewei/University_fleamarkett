package com.uncle.administrator.fleamarket;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.repacked.google.common.eventbus.EventBus;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uncle.Util.IMMLeaks;
import com.uncle.administrator.fleamarket.Conversation.ConversationFragment;
import com.uncle.administrator.fleamarket.DTO.User_account;
import com.uncle.administrator.fleamarket.Home.HomeFragment;
import com.uncle.administrator.fleamarket.Mine.MineFragment;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout knowLayout, iWantKnowLayout, meLayout, sell;

    private Fragment the_first;
    private Fragment the_secend;
    private Fragment the_thirs;
    private Fragment currentFragment;
    private ImageView knowImg, iWantKnowImg, meImg;
    protected User_account myAccount;
    private long exitTime = 0;//点击两次退出程序的计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getMyAccountFromSharePerFences();
        connectBmob();
        initUI();
        initTab();
    }

    private void connectBmob() {
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        if (!TextUtils.isEmpty(myAccount.getObjectId())) {
            BmobIM.connect(myAccount.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //TODO 连接成功后再进行修改本地用户信息的操作，并查询本地用户信息
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：3.6、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(myAccount.getObjectId(),
                                        myAccount.getName(), myAccount.getAvatar()));
                        Log.e("william   ", "链接成功");
                    } else {
                        Log.e("william   ", e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    Log.i("MainActivity.class", BmobIM.getInstance().getCurrentStatus().getMsg());
                    Log.e("william   ", BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
            //解决leancanary提示InputMethodManager内存泄露的问题
            IMMLeaks.fixFocusedViewLeak(getApplication());
        }
    }

    private void getMyAccountFromSharePerFences() {
        SharedPreferences sp = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        String string = sp.getString("myAccount", null);
        if (string != null) {
            myAccount = new Gson().fromJson(string, User_account.class);
            return;
        }
        myAccount = new User_account("http://bmob-cdn-8783.b0.upaiyun.com/2017/10/19/a9b5ff14ae814db1abe69f24eebaf01b.jpg",
                "威", "五邑大学", "计算机学院", null, null, null);
        myAccount.setObjectId("a646d91303");
        saveMyAccountFromSharePerFences(this, myAccount);
    }

    public void saveMyAccountFromSharePerFences(Context context, User_account myAccount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myAccount", new Gson().toJson(myAccount));
        editor.commit();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        knowLayout = (RelativeLayout) findViewById(R.id.rl_know);
        iWantKnowLayout = (RelativeLayout) findViewById(R.id.rl_want_know);
        meLayout = (RelativeLayout) findViewById(R.id.rl_me);
        sell = (RelativeLayout) findViewById(R.id.rl_sell);
        sell.setOnClickListener(this);
        knowLayout.setOnClickListener(this);
        iWantKnowLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        knowImg = (ImageView) findViewById(R.id.iv_know);
        iWantKnowImg = (ImageView) findViewById(R.id.iv_i_want_know);
        meImg = (ImageView) findViewById(R.id.iv_me);

    }

    /**
     * 初始化底部标签
     */
    private void initTab() {

        if (the_first == null) {
            the_first = new HomeFragment();
        }

        if (!the_first.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout, the_first).commit();

            // 记录当前Fragment
            currentFragment = the_first;
            // 设置图片文本的变化
            knowImg.setImageResource(R.drawable.home);
            iWantKnowImg.setImageResource(R.drawable.news);
            meImg.setImageResource(R.drawable.person);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_know: // 知道
                clickTab1Layout();
                break;
            case R.id.rl_want_know: // 我想知道
                clickTab2Layout();
                break;
            case R.id.rl_me: // 我的
                clickTab3Layout();
                break;
            case R.id.rl_sell://SellActivity
                Intent intent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }//各个点击事件

    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {

        if (the_first == null) {
            the_first = new HomeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), the_first);
        // 设置底部tab变化

        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setRepeatCount(0);//动画的重复次数
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        knowImg.startAnimation(anim);


    }

    /**
     * 点击第二个tab
     */
    private void clickTab2Layout() {
        if (the_secend == null) {
            the_secend = new ConversationFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), the_secend);


        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setRepeatCount(0);//动画的重复次数
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        iWantKnowImg.startAnimation(anim);


    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        if (the_thirs == null) {
            the_thirs = new MineFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(), the_thirs);
        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setRepeatCount(0);//动画的重复次数
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        meImg.startAnimation(anim);

    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
