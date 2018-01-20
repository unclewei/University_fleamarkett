package com.uncle.administrator.university_fleamarket;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uncle.bomb.shop_goods;
import com.uncle.method.MyAdapter.MyListAdapter;
import com.uncle.method.view.RefreshableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 *
 * @author Administrator
 * @date 2016/11/22 0022
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private Button b1,b2,b3,b4,b5,b6;//上面的控件
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;//上面的都是广告中的设置的文件
    private ListView listview;//成哥界面是以listview组成的
    private int setSkip_number = 0;
    private View headView;//界面中的头部界面
    private Thread mThread;//新线程
    private MyListAdapter adapter;//adapter的条目
    private int lastVisibleIndex;//listview最后的位置
    private boolean is_off = false;
    private LinearLayout loadingLayout, loadingLayout_off;
    //下拉刷新的加载界面
    private ProgressBar progressBar;//加载的progressbar
    private LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(//设置布局显示属性
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams FFlayoutParams = new LinearLayout.LayoutParams( //设置布局显示目标最大化属性
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.FILL_PARENT);
    private ArrayList<HashMap<String, String>> al = new ArrayList<>();//bomb中下载下来的数据
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RefreshableView refreshableView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.the_base_button_1, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bmob.initialize(getContext(), "144dbb1fbca09ce5d3af201a05c54628");
        init();
        initViewPager();
        query_goods_and_do(0, new Query_callback() {
            @Override
            public void onImageLoad(ArrayList<HashMap<String, String>> al) {
                getData_From_SQL(al);
            }
        });
        listview.setOnScrollListener(this);//设置滚动监听事件
    }


    public void init() { //各类初始化

        ready_for_fresh();
        ready_for_fresh_off();
        listview = (ListView) getActivity().findViewById(R.id.bt1_listview);
        headView = getActivity().getLayoutInflater().inflate(R.layout.the_base_button_1_headview, null);//增加HeadView
        refreshableView = (RefreshableView) getActivity().findViewById(R.id.refreshable_view);
        listview.addHeaderView(headView);//添加头部界面
        listview.addFooterView(loadingLayout);   // 添加到脚页显示

        b1 = (Button) headView.findViewById(R.id.bt1_1);
        b2 = (Button) headView.findViewById(R.id.bt1_2);
        b3 = (Button) headView.findViewById(R.id.bt1_3);
        b4 = (Button) headView.findViewById(R.id.bt1_4);
        b5 = (Button) headView.findViewById(R.id.bt1_5);
        b6 = (Button) headView.findViewById(R.id.bt1_6);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);



        listview_onclick();//项目点击事件
        refreshable();
    }

    private void refreshable() {

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                setSkip_number = 0;
//                这里要把listview的东西清空,只需要轻松arraylist里面的内容，然后刷新即可

                al.clear();
                setSkip_number = 0;
                query_goods_and_do(setSkip_number, new Query_callback() {
                    @Override
                    public void onImageLoad(ArrayList<HashMap<String, String>> al) {
                        getData_From_SQL(al);
                        Message message = new Message();
                        message.what = 2;
                        viewHandler.sendMessage(message);

                    }
                });
            }
        });

    }


    public void query_goods_and_do(int setSkip_number, final Query_callback query_callback) {
        BmobQuery<shop_goods> query = new BmobQuery<>();

        query.setLimit(10);
        query.setSkip(10 * setSkip_number); // 忽略前10条数据（即第一页数据结果）
        query.order("-updatedAt");//以时间来降序排列
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
//        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天);


        query.findObjects(new FindListener<shop_goods>() {
            @Override
            public void done(List<shop_goods> list, BmobException e) {
                if (e == null) {
                    for (shop_goods goods : list) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("image1", goods.getImage1());
                        map.put("image2", goods.getImage2());
                        map.put("image3", goods.getImage3());
                        map.put("image4", goods.getImage4());
                        map.put("image5", goods.getImage5());
                        map.put("image6", goods.getImage6());
                        map.put("title", goods.getTitle());
                        map.put("TextActivity", goods.getText());
                        map.put("price", goods.getPrice());
                        map.put("variety", goods.getVariety());
                        map.put("objectID", goods.getObjectId());
                        map.put("zan_nub", goods.getZan_nub() + "");
                        map.put("owner", goods.getOwner());
                        map.put("college", goods.getCollege());
                        map.put("organization", goods.getOrganization());
                        map.put("head_portrait_url",goods.getHead_portrait());
                        map.put("name",goods.getName());

                        al.add(map);
                    }
                    query_callback.onImageLoad(al);
                }
            }
        });
    }//查询数据，把数据以arraylist的形式回调回来


    public void getData_From_SQL(ArrayList<HashMap<String, String>> al) {

        adapter = new MyListAdapter(getActivity(), al, listview);
        listview.setAdapter(adapter);
        setSkip_number++;
    }//设置轮询器把数据放入控件中

    private void listview_onclick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == -1) {
                    return; // 点击的是headerView或者footerView
                }
                Adapter adapter = parent.getAdapter();
                HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(position);
                String objID = map.get("objectID");
                String owner_id = map.get("owner");
                Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
                intent.putExtra("objID", objID);
                intent.putExtra("owner_id", owner_id);

                startActivity(intent);

            }
        });
    }//项目点击事件

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.bt1_1:
               intent_to_null_activity();
               break;
           case R.id.bt1_2:
               intent_to_null_activity();
               break;
           case R.id.bt1_3:
               intent_to_null_activity();
               break;
           case R.id.bt1_4:
               intent_to_null_activity();
               break;
           case R.id.bt1_5:
               intent_to_null_activity();
               break;
           case R.id.bt1_6:
               intent_to_null_activity();
               break;

       }
    }
    private void intent_to_null_activity(){
        Intent intent = new Intent(getContext(),NullActivity.class);
        startActivity(intent);
    }


    public interface Query_callback {

        public void onImageLoad(ArrayList<HashMap<String, String>> al);
    }//查找数据的回调函数

    private void ready_for_fresh() {

        // 线性布局
        LinearLayout layout = new LinearLayout(getContext());
        // 设置布局 水平方向
        layout.setOrientation(LinearLayout.HORIZONTAL);
        // 进度条
        progressBar = new ProgressBar(getContext());
        // 进度条显示位置
        progressBar.setPadding(0, 0, 15, 0);
        // 把进度条加入到layout中
        layout.addView(progressBar, mLayoutParams);
        // 文本内容
        TextView textView = new TextView(getContext());
        textView.setText("加载中...");
        textView.setGravity(Gravity.CENTER_VERTICAL);
        // 把文本加入到layout中
        layout.addView(textView, FFlayoutParams);
        // 设置layout的重力方向，即对齐方式是
        layout.setGravity(Gravity.CENTER);
        // 设置ListView的页脚layout
        loadingLayout = new LinearLayout(getContext());
        loadingLayout.addView(layout, mLayoutParams);
        loadingLayout.setGravity(Gravity.CENTER);
    }//设置下拉刷新的一些布局准备,下拉加载转转转

    private void ready_for_fresh_off() {

        // 线性布局
        LinearLayout layout = new LinearLayout(getContext());
        // 设置布局 水平方向
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setText("~内容已经全部被你看完啦~");
        textView.setGravity(Gravity.CENTER_VERTICAL);
        // 把文本加入到layout中
        layout.addView(textView, FFlayoutParams);
        // 设置layout的重力方向，即对齐方式是
        layout.setGravity(Gravity.CENTER);
        // 设置ListView的页脚layout
        loadingLayout_off = new LinearLayout(getContext());
        loadingLayout_off.addView(layout, mLayoutParams);
        loadingLayout_off.setGravity(Gravity.CENTER);
    }//设置下拉刷新的一些布局准备,下拉加载结束，内容已经没有

    @Override
    public void onScroll(AbsListView view, final int firstVisibleItem,//刷新的的时候，内部开启子线程下载，并传送消息机制
                         int visibleItemCount, int totalItemCount) {

        lastVisibleIndex = firstVisibleItem + visibleItemCount - 2;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == adapter.getCount()) {
            //开线程去下载网络数据
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            //这里放你网络数据请求的方法
                            query_goods_and_do(setSkip_number, new Query_callback() {
                                @Override
                                public void onImageLoad(ArrayList<HashMap<String, String>> al) {
                                    Message message = new Message();
                                    message.what = 1;
                                    message.obj = al;
                                    viewHandler.sendMessage(message);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();
            }
        }

        // TODO Auto-generated method stub
    }

    //消息处理机制
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);//广告的消息处理
            super.handleMessage(msg);
            switch (msg.what) {//下拉刷新的消息处理
                case 1:

                    ArrayList<HashMap<String, String>> al = (ArrayList<HashMap<String, String>>) msg.obj;

                    if (al != null && al != arrayList) {
                        System.out.println("adapter````````````````` :" + adapter);
                        arrayList = al;
                        adapter.updateView(al);
//                        Toast.makeText(getContext(), "加载更多", Toast.LENGTH_LONG).show();
//                            listview.setSelection(listview.getCount() - 1);
                        Log.i("来点数字33333````", String.valueOf(setSkip_number));
                        setSkip_number++;
                    } else if (al != null && al.equals(arrayList)) {
                        listview.removeFooterView(loadingLayout);
                        listview.addFooterView(loadingLayout_off);   // 添加到脚页显示
                        is_off = true;
                    } else {
//                        listview.removeFooterView(loadingLayout);
                    }
                    break;

                case 2:
                    if (is_off) {
                        listview.removeFooterView(loadingLayout_off);
                        listview.addFooterView(loadingLayout);   // 添加到脚页显示
                        is_off = false;
                        refreshableView.finishRefreshing();

                    }else {
                        refreshableView.finishRefreshing();
                    }

                    break;

                default:
                    break;
            }
        }

    };


    //--------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
//---------------------------------------广告-------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
    private void initViewPager() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        advPager = (ViewPager) headView.findViewById(R.id.adv_pager);
        ViewGroup group = (ViewGroup) headView.findViewById(R.id.viewGroup);


        //      这里存放的是四张广告背景
        List<View> advPics = new ArrayList<View>();

        final ImageView img1 = new ImageView(headView.getContext());
        img1.setBackgroundResource(R.drawable.a);
        advPics.add(img1);

        ImageView img2 = new ImageView(headView.getContext());
        img2.setBackgroundResource(R.drawable.b);
        advPics.add(img2);

        ImageView img3 = new ImageView(headView.getContext());
        img3.setBackgroundResource(R.drawable.c);
        advPics.add(img3);

        ImageView img4 = new ImageView(headView.getContext());
        img4.setBackgroundResource(R.drawable.d);
        advPics.add(img4);


        // 对广告设置点击事件
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_advantage(1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_advantage(2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_advantage(3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_advantage(4);
            }
        });


        //      对imageviews进行填充
        imageViews = new ImageView[advPics.size()];
        //小图标
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(headView.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(5, 5, 5, 5);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.point);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.point2);
            }
            group.addView(imageViews[i]);
        }

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
        advPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "888", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void send_advantage(int i) {
        Intent intent = new Intent(getActivity(), TextActivity.class);
        intent.putExtra("image", i);
        startActivity(intent);

    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }


    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.point);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.point2);
                }
            }

        }

    }

    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

}
