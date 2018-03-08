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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/3/11 0011
 */

public class GoodsDetailsActivity extends BaseBindingActivity<ActivityGoodsDetailBinding> {
    private String replyUserName = null;
    private Boolean isZan = false;
    private final int CHAT_ACTIVITY_ACCOUNT_NAME = 7;
    private final int CHAT_ACTIVITY_ACCOUNT_HEAD = 8;
    private String pageObjectID;
    private BOMBOpenHelper bomb = new BOMBOpenHelper();
    private String ownerObjectId;
    private String myObjectId;
    private String myName;
    private ArrayList zanList = null;
    private ArrayList commentList = null;

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
        findAccountDataFromBomb(ownerObjectId);

        comment();
        zanClick();
        headClick();
    }

    private void getDataFromShareperecence() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        myObjectId = sharedPreferences.getString("object_id", "没有id");
        String myOrganization = sharedPreferences.getString("organization", null);
        myName = sharedPreferences.getString("nick_name", "匿名用户");
        binding.tvOrganization.setText(myOrganization);
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
                message.obj = object;
                handler.sendMessage(message);
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
            if (ownerObjectId.equals(myObjectId)) {
                Toast.makeText(GoodsDetailsActivity.this, "不能自己与自己聊天", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(GoodsDetailsActivity.this, ChatActivity.class);
                intent.putExtra("ownerObjectId", ownerObjectId);
                startActivity(intent);
            }
        }
    }


    public void zanClick() {
        binding.btZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isZan) {
                    zanList.remove(pageObjectID);
                    final int nub = Integer.parseInt(binding.tvZanCount.getText().toString()) - 1;
                    bomb.updateZan(pageObjectID, myObjectId, zanList, nub);
                    binding.tvZanCount.setText(String.valueOf(nub));
                    binding.btZan.setBackgroundResource(R.drawable.love1);
                    isZan = false;
                    return;
                }
                zanList.add(pageObjectID);
                final int nub = Integer.parseInt(binding.tvZanCount.getText().toString()) + 1;
                bomb.updateZan(pageObjectID, myObjectId, zanList, nub);
                binding.tvZanCount.setText(String.valueOf(nub));
                binding.btZan.setBackgroundResource(R.drawable.love2);
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
            pageObjectID = intent.getStringExtra("objID");
            ownerObjectId = intent.getStringExtra("owner_id");
            BOMBOpenHelper bomb = new BOMBOpenHelper();
            bomb.find_alone(pageObjectID, new BOMBOpenHelper.ImageCallback() {
                @Override
                public void onImageLoad(shop_goods shopgoods) {
                    Glide.with(GoodsDetailsActivity.this)
                            .load(shopgoods.getHead_portrait())
                            .into(binding.imgHead);
                    binding.tvName.setText(shopgoods.getName());
                    binding.tvOrganization.setText(shopgoods.getOrganization());
                    binding.tvDescription.setText(shopgoods.getText());
                    binding.tvPrice.setText(shopgoods.getPrice());
                    binding.tvZanCount.setText(String.valueOf(shopgoods.getZan_nub()));
                    setImage(shopgoods.getPictureNub(), shopgoods.ImageList());
                }

                @Override
                public void onError() {
                }
            });
            getComment();
        }
    }

    public void comment() {
        binding.btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textContext = binding.etCommentInput.getText().toString();
                if (!binding.etCommentInput.getText().toString().isEmpty() && replyUserName == null) {
                    binding.etCommentInput.setHint("快点留下你的评论吧");
                    textContext = myName + " ：“" + textContext + "”";
                    getNewTextView(textContext);
                    KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                    binding.etCommentInput.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    final CommentZan commentZan = new CommentZan(textContext, pageObjectID, myName, "name2", 0);
                    bomb.addCommentZan(commentZan, myObjectId, commentList);
                    return;
                }
                if (!binding.etCommentInput.getText().toString().isEmpty() && replyUserName != null) {
                    textContext = "   " + myName + "  回复了" + replyUserName + "：“" + textContext + "”";
                    getNewTextView(textContext);
                    KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                    binding.etCommentInput.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    final CommentZan commentZan = new CommentZan(textContext, pageObjectID, myName, "name2", 1);
                    bomb.addCommentZan(commentZan, myObjectId, commentList);
                    replyUserName = null;
                    return;
                }
                Toast.makeText(GoodsDetailsActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCommentUserName(String name) {
        if (name.contains("回复")) {
            return name.substring(0, name.indexOf("回复"));
        } else if (name.contains("：")) {
            return name.substring(0, name.indexOf("："));
        }
        return null;
    }

    public void getComment() {
        bomb.findComment(pageObjectID, new BOMBOpenHelper.getCommentCallback() {
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
            replyUserName = getCommentUserName(textView.getText().toString().trim());
            if (replyUserName != null) {
                binding.etCommentInput.setHint("回复用户 " + replyUserName);
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHAT_ACTIVITY_ACCOUNT_NAME:
                    User_account userAccount = (User_account) msg.obj;
                    binding.tvName.setText(userAccount.getNick_name());
                    zanList = userAccount.getZanList();
                    if (zanList == null) {
                        zanList = new ArrayList();
                        isZan = false;
                        return;
                    }
                    isZan = zanList.contains(pageObjectID);
                    binding.btZan.setBackgroundResource(isZan ? R.drawable.love2 : R.drawable.love1);

                    commentList = userAccount.getCommentList();
                    if (commentList.contains(pageObjectID)) {
                        commentList = null;
                    } else {
                        commentList.add(pageObjectID);
                    }
                    break;
                case CHAT_ACTIVITY_ACCOUNT_HEAD:

                    break;
                default:
                    break;
            }
        }

    };


}
