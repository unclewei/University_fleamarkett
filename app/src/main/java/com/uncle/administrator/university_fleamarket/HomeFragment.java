package com.uncle.administrator.university_fleamarket;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.support.v4.widget.SwipeRefreshLayout;

import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.administrator.university_fleamarket.databinding.TheBaseButton1Binding;
import com.uncle.bomb.ShopGoods;
import com.uncle.method.MyAdapter.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Administrator
 * @date 2016/11/22 0022
 */
public class HomeFragment extends BaseBindingFragment<TheBaseButton1Binding> implements  BaseBindAdapter.OnItemClickListener<ShopGoods>, SwipeRefreshLayout.OnRefreshListener {

    private int setSkipNumber = 0;
    private MyListAdapter myListAdapter;


    @Override
    protected void bindData(TheBaseButton1Binding dataBinding) {
        Bmob.initialize(getContext(), "144dbb1fbca09ce5d3af201a05c54628");
        initAdapter();
        init();
        initViewPager();
        queryGoods(0, new QueryCallBack() {
            @Override
            public void onImageLoad(List<ShopGoods> list) {
                getDataFromSQL(list);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.the_base_button_1;
    }

    public void init() { //各类初始化
        binding.refresh.setOnRefreshListener(this);
    }

    public void queryGoods(int setSkip_number, final QueryCallBack queryCallBack) {
        BmobQuery<ShopGoods> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10 * setSkip_number); // 忽略前10条数据（即第一页数据结果）
        query.order("-updatedAt");//以时间来降序排列
        query.findObjects(new FindListener<ShopGoods>() {
            @Override
            public void done(List<ShopGoods> list, BmobException e) {
                if (e == null) {
                    queryCallBack.onImageLoad(list);
                }
            }
        });
    }

    public void initAdapter() {
        myListAdapter = new MyListAdapter(getActivity());
        myListAdapter.setOnItemClickListener(this);
        binding.recylerview.setAdapter(myListAdapter);
    }
    public void getDataFromSQL(List<ShopGoods> list) {
        myListAdapter.setList(list);
        myListAdapter.notifyDataSetChanged();
        setSkipNumber++;
    }


    private void intentToNullActivity() {
        Intent intent = new Intent(getContext(), NullActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(ShopGoods data) {
        Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
        intent.putExtra("objID", data.getObjectId());
        intent.putExtra("owner_id", data.getOwner());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        setSkipNumber = 0;
        queryGoods(setSkipNumber, new QueryCallBack() {
            @Override
            public void onImageLoad(List<ShopGoods> list) {
                getDataFromSQL(list);

            }
        });
    }


    public interface QueryCallBack {
        void onImageLoad(List<ShopGoods> list);
    }


//--------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
//---------------------------------------广告-------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
    private int scrollTime = 0;
    public static final int AUTO_BANNER_CODE = 0X1001;
    private List<Object> bannerList;
    private Handler bannerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            scrollTime++;
            if (scrollTime == 5) {
                if (bannerList != null) {
                    // 获取当前的位置
                    int mBannerPosition = binding.advPager.getCurrentItem();
                    // 更换轮播图
                    if (mBannerPosition != bannerList.size()) {
                        mBannerPosition = mBannerPosition + 1;
                    }
                    if (mBannerPosition == bannerList.size()) {
                        mBannerPosition = 0;
                    }
                    binding.advPager.setCurrentItem(mBannerPosition);
                }
                scrollTime = 0;
            }
            startBannerTimer();
            return true;
        }
    });

    private void initViewPager() {
        bannerList = new ArrayList<>();
        bannerList.add(R.drawable.a);
        bannerList.add(R.drawable.b);
        bannerList.add(R.drawable.c);
        bannerList.add(R.drawable.d);
        BannerVpAdapter adapter = new BannerVpAdapter(getContext(), bannerList);
        binding.advPager.setAdapter(adapter);
        if (adapter.getCount() > 1) {
            binding.indicator.setViewPager(binding.advPager);
        }

    }

    private void startBannerTimer() {
        bannerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                bannerHandler.sendEmptyMessage(AUTO_BANNER_CODE);
            }
        }, 1000);

    }

}
