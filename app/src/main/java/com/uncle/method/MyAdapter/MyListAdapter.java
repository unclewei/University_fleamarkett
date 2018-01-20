package com.uncle.method.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uncle.administrator.university_fleamarket.R;

/**
 * @author unclewei
 */
public class MyListAdapter extends BaseAdapter  {

    private ListView listView;
    private Context context;
    private List<HashMap<String,String>> dataArray=new ArrayList<>();



    public MyListAdapter(Context context, List<HashMap<String,String>> imageAndTexts, ListView listView) {

        this.listView = listView;
        this.context = context;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder;
        if (convertView == null) {
            holder =new Holder();
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



        View view = listView.findViewWithTag(position);

        Glide.with(context).load(imageUrl1).into(holder.iv1);
        Glide.with(context).load(imageUrl2).into(holder.iv2);
        Glide.with(context).load(imageUrl3).into(holder.iv3);

        position++;
        return convertView;
    }

    public void updateView(ArrayList<HashMap<String, String>> nowList)
    {
        this.dataArray = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }



}