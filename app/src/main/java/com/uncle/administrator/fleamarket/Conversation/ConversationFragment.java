package com.uncle.administrator.fleamarket.Conversation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.DTO.ConversationDTO;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.chat.ChatActivity;
import com.uncle.administrator.fleamarket.databinding.ConversationFragmentBinding;
import com.uncle.database.ChatDataDao;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * @author Administrator
 * @date 2016/11/22 0022
 */

public class ConversationFragment extends BaseBindingFragment<ConversationFragmentBinding> implements BaseBindAdapter.OnItemClickListener<ConversationDTO> {
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

    private void navToChat(ConversationDTO user) {
        BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", conversationEntrance);
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("chat", bundle);
        startActivity(intent);
    }

    private void getConversationList() {
        List<BmobIMConversation> lists =  BmobIM.getInstance().loadAllConversation();
        List<ConversationDTO> list = dao.findConversationByDB();
        if (list == null || list.size() == 0) {
            return;
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(ConversationDTO data) {
        navToChat(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && dao != null) {
            getConversationList();
        }
    }
}
