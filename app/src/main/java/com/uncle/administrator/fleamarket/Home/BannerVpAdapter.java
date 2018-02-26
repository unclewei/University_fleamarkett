package com.uncle.administrator.fleamarket.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author unclewei
 * @date 2017/12/4
 */

public class BannerVpAdapter extends PagerAdapter {

    private List<Object> list;
    private Context context;
    private ImageView[] listImageView;

    public BannerVpAdapter(Context context, List<Object> list) {
        this.list = list;
        this.context = context;
        initListImg();
    }

    private void initListImg() {
        listImageView = new ImageView[list.size()];
        for (int i = 0; i < list.size(); i++) {
            final int banner = (int) list.get(i);
            ImageView imageView = new ImageView(context);
            listImageView[i] = imageView;
            Glide.with(context).load(banner).into(listImageView[i]);
            listImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(listImageView[(position % listImageView.length)]);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(listImageView[(position % listImageView.length)], 0);
        return listImageView[(position % listImageView.length)];
    }

}
