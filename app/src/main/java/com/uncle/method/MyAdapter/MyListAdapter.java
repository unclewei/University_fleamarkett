package com.uncle.method.MyAdapter;

/**
 * Created by Administrator on 2017/2/9 0009.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.uncle.administrator.university_fleamarket.R;
import com.uncle.administrator.university_fleamarket.bt1_ViewPagerActivity;
import com.uncle.method.PhotoView;

public class MyListAdapter extends BaseAdapter  {

    private LayoutInflater inflater;
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    private List<HashMap<String,String>> dataArray=new ArrayList<>();



    public MyListAdapter(Activity activity, List<HashMap<String,String>> imageAndTexts, ListView listView) {

        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
        inflater = activity.getLayoutInflater();
        dataArray=imageAndTexts;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataArray.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(position >= getCount()){
            return null;
        }
        return dataArray.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    class Holder{
        TextView title;
        TextView price;
        TextView zan;
        TextView organization;
        TextView name;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        ImageView head_portrait;
    }
    //不需要ViewHolder版，直接将ImageAndText与XML资源关联起来
    public View getView( int position, View convertView, ViewGroup parent) {

        final Holder holder;
        if (convertView == null) {
            holder =new Holder();
            convertView = inflater.inflate(R.layout.adapter_base, null);
        }else{
            convertView.setTag(position);
//            holder = (Holder) convertView.getTag();
            holder =new Holder();
        }

        HashMap<String,String> imageAndText = (HashMap<String,String>) getItem(position);
        String imageUrl1 = imageAndText.get("image1");
        String imageUrl2 = imageAndText.get("image2");
        String imageUrl3 = imageAndText.get("image3");
        String head_portrait_url = imageAndText.get("head_portrait_url");

        holder.title =  (TextView) convertView.findViewById(R.id.adapter_title);
        holder.price =  (TextView) convertView.findViewById(R.id.adapter_price);
        holder.zan= (TextView) convertView.findViewById(R.id.adapter_base_zan);
        holder.organization = (TextView) convertView.findViewById(R.id.adapter_base_organization);
        holder.name = (TextView) convertView.findViewById(R.id.adapter_name);

        // 将XML视图项与用户输入的URL和文本在绑定
        holder.title.setText(imageAndText.get("title"));//加载title
        holder.price.setText(imageAndText.get("price"));//加载price
        holder.zan.setText(imageAndText.get("zan_nub"));//加载赞的数量zan
        holder.organization.setText(imageAndText.get("organization"));//加载学院的名字
        holder.name.setText(imageAndText.get("name"));//名字

        holder.iv1 = (ImageView) convertView.findViewById(R.id.adapter_img1);
        holder.iv2 = (ImageView) convertView.findViewById(R.id.adapter_img2);
        holder.iv3 = (ImageView) convertView.findViewById(R.id.adapter_img3);
        holder.head_portrait = (ImageView) convertView.findViewById(R.id.adapter_head_portrait);
        holder.iv1.setBackgroundResource(R.drawable.img_loading);//在初始化时，先把背景图片设置成默认背景，
        holder.iv2.setBackgroundResource(R.drawable.img_loading);//在初始化时，先把背景图片设置成默认背景，
        holder.iv3.setBackgroundResource(R.drawable.img_loading);//在初始化时，先把背景图片设置成默认背景，
        holder.head_portrait.setBackgroundResource(R.drawable.person);
        //否则在下拉时会随机匹配背景，不美观


        ImageLoader imageLoader  = ImageLoader.getInstance();//初始化
        DisplayImageOptions options =  new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_loading) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.img_loading) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.img_loading) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build(); // 构建完成

        View view = listView.findViewWithTag(position);
//        ImageView iv= (ImageView) view.findViewById(R.id.adapter_img1);
//        holder.iv2= (ImageView) view.findViewById(R.id.adapter_img2);
//        holder.iv3= (ImageView) view.findViewById(R.id.adapter_img3);
        imageLoader.displayImage(imageUrl1, holder.iv1, options);
        imageLoader.displayImage(imageUrl2, holder.iv2, options);
        imageLoader.displayImage(imageUrl3, holder.iv3, options);
//
//        asyncImageLoader.loadDrawable(inflater.getContext(),position,imageUrl1, new AsyncImageLoader.ImageCallback() {
//            @Override
//            public void onImageLoad(Integer pos, Drawable drawable) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img1);
//                    iv.setBackgroundDrawable(drawable);
//
//
//                }
//
//           }
//            //加载不成功的图片处理
//            @Override
//            public void onError(Integer pos) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img1);
//                    iv.setBackgroundResource(R.drawable.img_loading);
//                }
//            }
//
//        });
//        asyncImageLoader.loadDrawable(inflater.getContext(),position,imageUrl2, new AsyncImageLoader.ImageCallback() {
//            @Override
//            public void onImageLoad(Integer pos, Drawable drawable) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img2);
//                    iv.setBackgroundDrawable(drawable);
//
//                }
//            }
//            //加载不成功的图片处理
//            @Override
//            public void onError(Integer pos) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img2);
//                    iv.setBackgroundResource(R.drawable.img_loading);
//                }
//            }
//
//        });
//        asyncImageLoader.loadDrawable(inflater.getContext(),position,imageUrl3, new AsyncImageLoader.ImageCallback() {
//            @Override
//            public void onImageLoad(Integer pos, Drawable drawable) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img3);
//                    iv.setBackgroundDrawable(drawable);
//                    Message message = new Message();
//
//                }
//            }
//            //加载不成功的图片处理
//            @Override
//            public void onError(Integer pos) {
//                View view = listView.findViewWithTag(pos);
//                if(view != null){
//                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_img3);
//                    iv.setBackgroundResource(R.drawable.img_loading);
//                }
//            }
//
//        });
        asyncImageLoader.loadDrawable(inflater.getContext(),position,head_portrait_url, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoad(Integer pos, Drawable drawable) {
                View view = listView.findViewWithTag(pos);
                if(view != null){
                    if (drawable !=null){
                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_head_portrait);
                    iv.setBackgroundDrawable(drawable);
                }else {
                        ImageView iv = (ImageView) view.findViewById(R.id.adapter_head_portrait);
                        iv.setBackgroundResource(R.drawable.head);
                    }
                }
            }
            //加载不成功的图片处理
            @Override
            public void onError(Integer pos) {
                View view = listView.findViewWithTag(pos);
                if(view != null){
                    ImageView iv = (ImageView) view.findViewById(R.id.adapter_head_portrait);
                    iv.setBackgroundResource(R.drawable.person);
                }
            }

        });







        position++;
        return convertView;
    }

    public void updateView(ArrayList<HashMap<String, String>> nowList)
    {
        this.dataArray = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }



}