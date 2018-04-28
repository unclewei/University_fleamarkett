package com.uncle.administrator.fleamarket.GoodsDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.uncle.administrator.fleamarket.R;

import java.util.List;


/**
 * @author Administrator
 * @date 2017/3/16 0016
 * 点击图片，进入的viewpager中左右拉
 */

public class ViewPagerActivity extends Activity {
    private ViewPager mPager;
    private List imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt1_viewpageractivity);

        Intent intent = getIntent();
        imgList = intent.getStringArrayListExtra("imgList");
        int page = intent.getIntExtra("nub", 0);

        mPager = findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));

        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(ViewPagerActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(ViewPagerActivity.this)
                        .load(imgList.get(position)).
                        into(view);
                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        if (page != -1) {
            mPager.setCurrentItem(page);
        }
    }
}
