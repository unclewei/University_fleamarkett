package com.lexing360.vehicle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.lexing360.comm.RouterConstant;
import com.lexing360.comm.UpLoadClickInfo;
import com.lexing360.storage.Banner;

import java.util.List;

/**
 * @author unclewei
 * @date 2017/12/4
 */

public class BannerVpAdapter extends PagerAdapter {

    private List<Banner> list;
    private Context context;
    private ImageView[] listImageView;

    public BannerVpAdapter(Context context, List<Banner> list) {
        this.list = list;
        this.context = context;
        initListImg();
    }

    private void initListImg() {
        listImageView = new ImageView[list.size()];
        for (int i = 0; i < list.size(); i++) {
            final Banner banner = list.get(i);
            ImageView imageView = new ImageView(context);
            listImageView[i] = imageView;
            Glide.with(context).load(banner.getImageUrl()).placeholder(R.mipmap.car).into(listImageView[i]);
            listImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpLoadClickInfo.upLoadClickInfo(context, "主页面 跳转到 广告");
                    ARouter.getInstance()
                            .build(RouterConstant.WEB)
                            .withString("url", banner.getUrl())
                            .navigation();
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
