package com.uncle.administrator.fleamarket.Conversation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.chat.ChatActivity;
import com.uncle.administrator.fleamarket.databinding.ConversationFragmentBinding;
import com.uncle.database.ChatDataDao;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * @author Administrator
 * @date 2016/11/22 0022
 */

public class ConversationFragment extends BaseBindingFragment<ConversationFragmentBinding> implements BaseBindAdapter.OnItemClickListener<BmobIMConversation> {
    private ChatDataDao dao;
    private ConversationAdapter adapter;

    @Override
    protected void bindData(ConversationFragmentBinding dataBinding) {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.conversation_fragment;
    }

    private void init() {
        dao = new ChatDataDao(getContext());
        adapter = new ConversationAdapter(getContext());
        adapter.setOnItemClickListener(this);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.conversation_empty_view, binding.list, false));
        binding.list.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));
        binding.list.setAdapter(adapter);

    }

    private void navToChat(BmobIMConversation bmobIMConversation) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", bmobIMConversation);
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("chat", bundle);
        startActivity(intent);
    }

    private void getConversationList() {
        List<BmobIMConversation> lists = BmobIM.getInstance().loadAllConversation();
//        List<ConversationDTO> list = dao.findConversationByDB();
//        if (list == null || list.size() == 0) {
//            return;
//        }
        adapter.setList(lists);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BmobIMConversation data) {
        navToChat(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && dao != null) {
            getConversationList();
        }
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        //重新刷新列表
        if (adapter != null && dao != null) {
            getConversationList();
        }
    }

    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        if (adapter != null && dao != null) {
            getConversationList();
        }
    }
}
