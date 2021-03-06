package com.uncle.administrator.fleamarket.Home;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingFragment;
import com.uncle.DTO.shopGoods;
import com.uncle.administrator.fleamarket.GoodsDetails.GoodsDetailsActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.GoodsFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Administrator
 * @date 2016/11/22 0022
 */
public class HomeFragment extends BaseBindingFragment<GoodsFragmentBinding> implements
        BaseBindAdapter.OnItemClickListener<shopGoods>,
        SwipeRefreshLayout.OnRefreshListener,
        BaseBindAdapter.OnLoadListener {

    private int setSkipNumber = 0;
    private HomeListAdapter homeListAdapter;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void bindData(GoodsFragmentBinding dataBinding) {
        initWidget();
        initViewPager();
        queryGoods(0, new QueryCallBack() {
            @Override
            public void onImageLoad(List<shopGoods> list) {
                getDataFromSQL(list);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.goods_fragment;
    }

    public void queryGoods(int setSkipNumber, final QueryCallBack queryCallBack) {
        BmobQuery<shopGoods> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10 * setSkipNumber);
        query.order("-updatedAt");
        query.findObjects(new FindListener<shopGoods>() {
            @Override
            public void done(List<shopGoods> list, BmobException e) {
                if (e == null) {
                    queryCallBack.onImageLoad(list);
                    Log.e("william", list.toString());
                }
            }
        });
    }

    public void initWidget() {
        homeListAdapter = new HomeListAdapter(getActivity());
        homeListAdapter.setOnItemClickListener(this);
        homeListAdapter.setLoadingView(LayoutInflater.from(getContext()).inflate(R.layout.load_more_view, null));
        homeListAdapter.setOpenLoadMore(true);
        homeListAdapter.setOnLoadListener(this);
        binding.recylerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        skeletonScreen = Skeleton.bind(binding.recylerview)
                .adapter(homeListAdapter)
                .load(R.layout.adapter_home_sklele)
                .color(R.color.white_transparent)
                .show();
        binding.refresh.setOnRefreshListener(this);
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                binding.refresh.setEnabled(Math.abs(verticalOffset) == 0);
                if (binding.refresh.isRefreshing() && Math.abs(verticalOffset) > 3) {
                    binding.refresh.setRefreshing(false);
                }
            }
        });
    }

    public void getDataFromSQL(List<shopGoods> list) {
        homeListAdapter.setList(list);
        homeListAdapter.notifyDataSetChanged();
        setSkipNumber++;
        skeletonScreen.hide();
        if (binding.refresh.isRefreshing()) {
            binding.refresh.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(shopGoods data) {
        Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
        intent.putExtra("pageGoodsId", data.getObjectId());
        intent.putExtra("goodsOwnerObjectId", data.getOwner());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        setSkipNumber = 0;
        queryGoods(setSkipNumber, new QueryCallBack() {
            @Override
            public void onImageLoad(List<shopGoods> list) {
                getDataFromSQL(list);
                homeListAdapter.setLoadingView(LayoutInflater.from(getContext()).inflate(R.layout.load_more_view, null));
                homeListAdapter.setOpenLoadMore(true);
                homeListAdapter.setCanLoadMore(true);

            }
        });
    }

    @Override
    public void onLoadMore() {
        Log.e("william", "加载更多");
        queryGoods(setSkipNumber, new QueryCallBack() {
            @Override
            public void onImageLoad(List<shopGoods> list) {

            }
        });
    }


    public interface QueryCallBack {
        void onImageLoad(List<shopGoods> list);
    }


//---------------------------------------广告-------------------------------------------------------------

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

        binding.refresh.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

            }
        });
        startBannerTimer();
        binding.advPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollTime = 0;
                return false;
            }
        });
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
