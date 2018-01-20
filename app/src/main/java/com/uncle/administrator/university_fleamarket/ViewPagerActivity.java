package com.uncle.administrator.university_fleamarket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uncle.method.PhotoView;

/**
 *
 * @author Administrator
 * @date 2017/3/16 0016
 * 点击图片，进入的viewpager中左右拉
 */

public class ViewPagerActivity extends Activity {
    private ViewPager mPager;
    private String[] imgsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt1_viewpageractivity);

        Intent intent = getIntent();
        String img1_path = intent.getStringExtra("img1_path");
        String img2_path = intent.getStringExtra("img2_path");
        String img3_path = intent.getStringExtra("img3_path");

        imgsId = new String[]{img1_path, img2_path, img3_path};
        int page = intent.getIntExtra("nub", -1);//获取是点击的是第几张图片，然后从这里开始

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));

        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgsId.length;
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

                view.setImageBitmap(turn_low_image(imgsId[position]));
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        if (page != -1) {
            mPager.setCurrentItem(page);//从这里开始浏览图片
        }
    }


    private Bitmap turn_low_image(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//压缩图片质量为原来的1/8
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;//用ARBG_4444色彩模式加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);

        return bitmap;
    }
}
