package com.uncle.administrator.fleamarket.Mine;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.DTO.shopGoods;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.GoodsDetails.GoodsDetailsActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ActivityMineDataBinding;
import com.uncle.bomb.BOMBOpenHelper;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/3/19 0019
 */

public class MineDataActivity extends BaseBindingActivity<ActivityMineDataBinding> implements BaseBindAdapter.OnItemClickListener<shopGoods>, BaseBindAdapter.OnLoadListener {

    public static final String MY_PUBLIC = "myPublic";
    public static final String MY_SCAN = "myScan";
    public static final String MY_ZAN = "myZan";
    public static final String TYPE = "type";
    private MineDataAdapter adapter;
    private BOMBOpenHelper bomb;
    private int page = 1;
    private boolean isLast;
    private String type;

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
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);
        if (profile.getObjectId() == null || type == null) {
            ToastUtil.show(MineDataActivity.this, "数据出错");
            return;
        }
        getList(type, new FirstGoodsCallBack());
    }

    private void getList(String type, BOMBOpenHelper.OnGoodsListCallBack callBack) {
        switch (type) {
            case MY_PUBLIC:
                bomb.findMyPubGoods(profile.getObjectId(), page, callBack);
                break;
            case MY_SCAN:
                bomb.findMyGoods(profile.getObjectId(), MY_SCAN, page, callBack);
                break;
            case MY_ZAN:
                bomb.findMyGoods(profile.getObjectId(), MY_ZAN, page, callBack);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(shopGoods data) {
        Intent intent = new Intent(MineDataActivity.this, GoodsDetailsActivity.class);
        intent.putExtra("pageGoodsId", data.getObjectId());
        intent.putExtra("goodsOwnerObjectId", data.getOwner());
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        getList(type, new MoreGoodsCallBack());
    }

    private class FirstGoodsCallBack implements BOMBOpenHelper.OnGoodsListCallBack {
        @Override
        public void onDone(List<shopGoods> list) {
            page++;
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    private class MoreGoodsCallBack implements BOMBOpenHelper.OnGoodsListCallBack {
        @Override
        public void onDone(List<shopGoods> list) {
            adapter.addAll(list);
            page++;
            adapter.isLoading = false;
            adapter.notifyDataSetChanged();
            if (list.size() < 10) {
                adapter.removeLoadingView();
                isLast = true;
                ToastUtil.show(MineDataActivity.this, "没有更多货物啦");
            }
            adapter.setCanLoadMore(!isLast);
        }
    }
}
