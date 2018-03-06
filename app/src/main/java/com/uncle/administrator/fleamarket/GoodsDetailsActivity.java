package com.uncle.administrator.fleamarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.administrator.fleamarket.databinding.ActivityGoodsDetailBinding;
import com.uncle.bomb.BOMBOpenHelper;
import com.uncle.bomb.CommentZan;
import com.uncle.bomb.User_account;
import com.uncle.bomb.shop_goods;
import com.uncle.method.KeyboardUtil;
import com.uncle.method.SHandlerThread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/3/11 0011
 */

public class GoodsDetailsActivity extends BaseBindingActivity<ActivityGoodsDetailBinding> {
    private String replyUser = null;
    private Boolean isZan = false;
    private final int DEAL_WITH_IMAGE = 5;
    private final int CHAT_ACTIVITY_ACCOUNT_NAME = 7;
    private final int CHAT_ACTIVITY_ACCOUNT_HEAD = 8;
    private String objectID, new_objectID;//1.获取的数据库中的id。2.新生成的数据的objectid。
    private BOMBOpenHelper bomb = new BOMBOpenHelper();
    private String owner;//该条目的发出人objectid
    private String myobject, myorganization, head_portrait, myname;
    private ArrayList zanList = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void bindData(ActivityGoodsDetailBinding dataBinding) {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    public void init() {
        getDataFromShareperecence();
        getIntentMessage();
        findAccountDataFromBomb(owner);

        comMent();
        zanClick();
        headClick();
    }

    private void getDataFromShareperecence() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        myobject = sharedPreferences.getString("object_id", "没有id");
        myorganization = sharedPreferences.getString("organization", null);
        myname = sharedPreferences.getString("nick_name", "匿名用户");
        binding.tvOrganization.setText(myorganization);
    }

    /**
     * 用objectID获取用户的数据
     */
    public void findAccountDataFromBomb(String targetObject) {
        bomb.findAccountDataAlone(targetObject, new BOMBOpenHelper.FindAccountDataAloneCallback() {
            @Override
            public void onSuccess(User_account object) {
                Message message = new Message();
                message.what = CHAT_ACTIVITY_ACCOUNT_NAME;
                message.obj = object.getNick_name();
                handler.sendMessage(message);
                Message message2 = new Message();
                message2.what = CHAT_ACTIVITY_ACCOUNT_HEAD;
                message2.obj = object.getZanList();
                handler.sendMessage(message2);

            }
        });
    }


    private void headClick() {
        binding.imgHead.setOnClickListener(new clickToChat());
        binding.llImages.setOnClickListener(new clickToChat());
        binding.tvContact.setOnClickListener(new clickToChat());
    }

    private class clickToChat implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (owner.equals(myobject)) {
                Toast.makeText(GoodsDetailsActivity.this, "不能自己与自己聊天", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(GoodsDetailsActivity.this, ChatActivity.class);
                intent.putExtra("owner", owner);
                startActivity(intent);
            }
        }
    }


    public void zanClick() {
        binding.btZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isZan) {
                    zanList.remove(objectID);
                    int nub = Integer.parseInt(binding.tvZanNub.getText().toString()) - 1;
                    binding.tvZanNub.setText(nub);
                    binding.btZan.setBackgroundResource(R.drawable.love1);
                    bomb.updateZan(objectID, zanList, nub);
                    isZan = false;
                    return;
                }
                zanList.add(objectID);
                int nub = Integer.parseInt(binding.tvZanNub.getText().toString()) + 1;
                binding.tvZanNub.setText(nub);
                binding.btZan.setBackgroundResource(R.drawable.love2);
                bomb.updateZan(objectID, zanList, nub);
                isZan = true;

            }
        });
    }

    private void setImage(int imgNub, final List<String> list) {
        for (int i = 0; i < imgNub; i++) {
            final int a = i;
            ImageView imageView = (ImageView) binding.llImages.getChildAt(i);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(GoodsDetailsActivity.this)
                    .load(list.get(i))
                    .placeholder(R.drawable.img_loading)
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GoodsDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("nub", a);
                    intent.putStringArrayListExtra("imgList", (ArrayList<String>) list);
                    startActivity(intent);
                }
            });
        }
    }

    public void getIntentMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            objectID = intent.getStringExtra("objID");
            owner = intent.getStringExtra("owner_id");
            BOMBOpenHelper bomb = new BOMBOpenHelper();
            bomb.find_alone(objectID, new BOMBOpenHelper.ImageCallback() {
                @Override
                public void onImageLoad(shop_goods shopgoods) {
                    Glide.with(GoodsDetailsActivity.this)
                            .load(shopgoods.getHead_portrait())
                            .into(binding.imgHead);
                    binding.tvName.setText(shopgoods.getName());
                    binding.tvOrganization.setText(shopgoods.getOrganization());
                    binding.tvDescription.setText(shopgoods.getText());
                    binding.tvPrice.setText(shopgoods.getPrice());
//                    binding.tvZanNub.setText(shopgoods.getZan_nub());
                    setImage(shopgoods.getPictureNub(), shopgoods.ImageList());
                }

                @Override
                public void onError() {
                }
            });
            getComment();
        }
    }

    public void comMent() {
        binding.btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textContext = binding.etCommentInput.getText().toString();
                if (!binding.etCommentInput.getText().toString().isEmpty() && replyUser == null) {
                    binding.etCommentInput.setHint("快点就下你的评论吧");
                    textContext = myname + " ：“" + textContext + "”";
                    getNewTextView(textContext);
                    KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                    binding.etCommentInput.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    final CommentZan commentZan = new CommentZan(textContext, objectID, "name1", "name2", 0);
                    SHandlerThread.postToWorker(new Runnable() {
                        @Override
                        public void run() {
                            bomb.addCommentZan(commentZan);
                        }
                    });
                    return;
                }
                if (!binding.etCommentInput.getText().toString().isEmpty() && replyUser != null) {
                    binding.etCommentInput.setHint("回复用户");
                    textContext = "   " + myname + "  回复了" + "上面的用户" + ":“" + textContext + "”";
                    getNewTextView(textContext);
                    KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                    binding.etCommentInput.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    final CommentZan commentZan = new CommentZan(textContext, objectID, "name1", "name2", 1);
                    SHandlerThread.postToWorker(new Runnable() {
                        @Override
                        public void run() {
                            bomb.addCommentZan(commentZan);
                        }
                    });
                    replyUser = null;
                    return;
                }
                Toast.makeText(GoodsDetailsActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getComment() {
        bomb.find_comment(objectID, new BOMBOpenHelper.getCommentCallback() {
            @Override
            public void onCommentLoad(List<CommentZan> arrayList) {
                if (arrayList.size() != 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        CommentZan commentZan = arrayList.get(i);
                        String comment = commentZan.getComment();
                        getNewTextView(comment);
                    }
                } else {
                    getNewTextView("还没有评论哦，快来做第一个！");
                }
            }

        });
    }

    public void getNewTextView(String context) {
        TextView textView = new TextView(GoodsDetailsActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setText(context);
        binding.llComment.addView(textView);
        textView.setOnClickListener(new MyButtonListener());
    }

    private class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            binding.etCommentInput.setFocusable(true);
            binding.etCommentInput.setFocusableInTouchMode(true);
            binding.etCommentInput.requestFocus();
            KeyboardUtil.openKeyBoard(GoodsDetailsActivity.this);
            replyUser = textView.getText().toString();
        }
    }


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHAT_ACTIVITY_ACCOUNT_NAME:
                    binding.tvName.setText((String) msg.obj);
                    break;
                case CHAT_ACTIVITY_ACCOUNT_HEAD:
                    zanList = (ArrayList) msg.obj;
                    if (zanList == null) {
                        zanList = new ArrayList();
                        isZan = false;
                        return;
                    }
                    isZan = zanList.contains(objectID);
                    break;
                default:
                    break;
            }
        }

    };


}
