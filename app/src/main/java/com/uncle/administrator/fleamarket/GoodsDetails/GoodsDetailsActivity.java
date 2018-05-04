package com.uncle.administrator.fleamarket.GoodsDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.Util.KeyboardUtil;
import com.uncle.DTO.CommentZan;
import com.uncle.DTO.Profile;
import com.uncle.DTO.shopGoods;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.chat.ChatActivity;
import com.uncle.administrator.fleamarket.databinding.ActivityGoodsDetailBinding;
import com.uncle.bomb.BOMBOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * @author Administrator
 * @date 2017/3/11 0011
 */

public class GoodsDetailsActivity extends BaseBindingActivity<ActivityGoodsDetailBinding> {
    private String replyUserName = null;
    private Boolean isZan = false;
    private final int GET_GOODS_OWNER_ACCOUNT = 7;
    private final int GET_MY_ACCOUNT = 8;
    private String pageGoodsID;
    private BOMBOpenHelper bomb = BOMBOpenHelper.getInstance();
    private String goodsOwnerObjectId;
    private ArrayList zanList = null;
    private ArrayList commentList = null;
    private Profile objectProfile;

    @Override
    protected void bindData(ActivityGoodsDetailBinding dataBinding) {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    public void init() {
        getIntentMessage();
        getDataFromSharedPerfences();
        findAccountDataFromBomb(GET_GOODS_OWNER_ACCOUNT, goodsOwnerObjectId);
        comment();
        zanClick();
        headClick();
    }

    private void getDataFromSharedPerfences() {
        binding.tvOrganization.setText(profile.getOrganization());
        zanList = profile.getZanList();
        commentList = profile.getCommentList();
        if (zanList == null) {
            zanList = new ArrayList();
            isZan = false;
        }
        if (commentList == null) {
            commentList = new ArrayList();
        }
        isZan = zanList.contains(pageGoodsID);
        binding.btZan.setBackgroundResource(isZan ? R.drawable.love2 : R.drawable.love1);
        if (commentList == null || !commentList.contains(pageGoodsID)) {
            commentList.add(pageGoodsID);
        }
    }

    /**
     * 用objectID获取用户的数据
     */
    public void findAccountDataFromBomb(final int messageType, String targetObject) {
        bomb.findAccountDataAlone(targetObject, new BOMBOpenHelper.FindAccountDataAloneCallback() {
            @Override
            public void onSuccess(Profile object) {
                Message message = new Message();
                message.what = messageType;
                message.obj = object;
                handler.sendMessage(message);
            }
        });
    }

    private void headClick() {
        binding.imgHead.setOnClickListener(new NavToChat());
        binding.llImages.setOnClickListener(new NavToChat());
        binding.tvContact.setOnClickListener(new NavToChat());
    }

    private class NavToChat implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (goodsOwnerObjectId.equals(profile.getObjectId())) {
                ToastUtil.show(GoodsDetailsActivity.this, "不能自己与自己聊天");
                return;
            }
            if (objectProfile == null) {
                findAccountDataFromBomb(GET_GOODS_OWNER_ACCOUNT, goodsOwnerObjectId);
                ToastUtil.show(GoodsDetailsActivity.this, "系统正繁忙，请稍候");
                return;
            }
            navToChat(objectProfile.getObjectId(), objectProfile.getName(), objectProfile.getAvatar());

        }
    }

    private void navToChat(String targetObject, String name, String avatar) {
        BmobIMUserInfo info = new BmobIMUserInfo(targetObject, name, avatar);
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", conversationEntrance);
        Intent intent = new Intent(GoodsDetailsActivity.this, ChatActivity.class);
        intent.putExtra("chat", bundle);
        startActivity(intent);
    }

    public void zanClick() {
        binding.btZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isZan) {
                    zanList.remove(pageGoodsID);
                    int nub = Integer.parseInt(binding.tvZanCount.getText().toString()) - 1;
                    bomb.substractZan(pageGoodsID);
                    binding.tvZanCount.setText(String.valueOf(nub));
                    binding.btZan.setBackgroundResource(R.drawable.love1);
                    isZan = false;
                    return;
                }
                zanList.add(pageGoodsID);
                int nub = Integer.parseInt(binding.tvZanCount.getText().toString()) + 1;
                bomb.addZan(pageGoodsID);
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
            pageGoodsID = intent.getStringExtra("pageGoodsId");
            goodsOwnerObjectId = intent.getStringExtra("goodsOwnerObjectId");
            bomb.find_alone(pageGoodsID, new BOMBOpenHelper.ImageCallback() {
                @Override
                public void onImageLoad(shopGoods shopgoods) {
                    Glide.with(GoodsDetailsActivity.this)
                            .load(shopgoods.getAvatar())
                            .placeholder(R.drawable.head_yard)
                            .error(R.drawable.head_black)
                            .into(binding.imgHead);
                    binding.tvName.setText(shopgoods.getName());
                    binding.tvOrganization.setText(shopgoods.getOrganization());
                    binding.tvDescription.setText(shopgoods.getText());
                    binding.tvPrice.setText(shopgoods.getPrice());
                    binding.tvZanCount.setText(String.valueOf(shopgoods.getZanNub()));
                    setImage(shopgoods.getPictureNub(), shopgoods.getImgFileList());
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
                if (binding.etCommentInput.getText().toString().isEmpty()) {
                    Toast.makeText(GoodsDetailsActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                if (!commentList.contains(pageGoodsID)) {
                    commentList.add(pageGoodsID);
                }
                if (replyUserName == null) {
                    binding.etCommentInput.setHint("快点留下你的评论吧");
                    textContext = profile.getName() + " ：“" + textContext + "”";
                    getNewTextView(textContext);
                    KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                    binding.etCommentInput.setText(null);
                    Toast.makeText(GoodsDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    CommentZan commentZan = new CommentZan(textContext, pageGoodsID, profile.getName(), "name2", 0);
                    bomb.addCommentZan(commentZan);
                    return;
                }
                textContext = "   " + profile.getName() + "  回复了" + replyUserName + "：“" + textContext + "”";
                getNewTextView(textContext);
                KeyboardUtil.closeKeyBoard(GoodsDetailsActivity.this, binding.etCommentInput);
                binding.etCommentInput.setText(null);
                Toast.makeText(GoodsDetailsActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                CommentZan commentZan = new CommentZan(textContext, pageGoodsID, profile.getName(), "name2", 1);
                bomb.addCommentZan(commentZan);
                replyUserName = null;

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
        bomb.findComment(pageGoodsID, new BOMBOpenHelper.getCommentCallback() {
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
                case GET_GOODS_OWNER_ACCOUNT:
                    objectProfile = (Profile) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profile.setZanList(zanList);
        profile.setCommentList(commentList);
        saveMyAccountFromSharePerFences(profile);
    }
}
