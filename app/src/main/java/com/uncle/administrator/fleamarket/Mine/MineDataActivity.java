package com.uncle.administrator.fleamarket.Mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.administrator.fleamarket.DTO.shop_goods;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ActivityMineDataBinding;
import com.uncle.bomb.BOMBOpenHelper;

import java.util.List;

import static com.uncle.administrator.fleamarket.chat.ChatActivity.TargetObjectID;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class MineDataActivity extends BaseBindingActivity<ActivityMineDataBinding> implements BaseBindAdapter.OnItemClickListener<shop_goods>,BaseBindAdapter.OnLoadListener {
    private MineDataAdapter adapter;
    private String object;
    private BOMBOpenHelper bomb;
    private int page = 1;
    private boolean isLast;

    @Override
    protected void bindData(ActivityMineDataBinding dataBinding) {
        bomb = new BOMBOpenHelper();
        initAdapter();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_data;
    }

    private void initAdapter() {
        adapter = new MineDataAdapter(MineDataActivity.this);
        adapter.setOnItemClickListener(this);
        adapter.setLoadingView(LayoutInflater.from(this).inflate(R.layout.load_more_view, null));
        adapter.setOpenLoadMore(true);
        adapter.setOnLoadListener(this);
        binding.list.setLayoutManager(new LinearLayoutManager(MineDataActivity.this));
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.conversation_empty_view, binding.list, false));
        binding.list.setAdapter(adapter);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        object = sharedPreferences.getString(TargetObjectID, null);
        if (object == null){
            Toast.makeText(MineDataActivity.this,"数据出错",Toast.LENGTH_SHORT).show();
            return;
        }
        bomb.findGoods(object, page, new BOMBOpenHelper.OnGoodsListCallBack() {
            @Override
            public void onDone(List<shop_goods> list) {
                page++;
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(shop_goods data) {

    }

    @Override
    public void onLoadMore() {
        bomb.findGoods(object, page, new BOMBOpenHelper.OnGoodsListCallBack() {
            @Override
            public void onDone(List<shop_goods> list) {
                adapter.addAll(list);
                page++;
                adapter.isLoading = false;
                adapter.notifyDataSetChanged();
                if (list.size() < 10) {
                    adapter.removeLoadingView();
                    isLast = true;
                    Toast.makeText(MineDataActivity.this,"没有更多货物啦",Toast.LENGTH_SHORT).show();
                }
                adapter.setCanLoadMore(!isLast);
            }
        });
    }
}
