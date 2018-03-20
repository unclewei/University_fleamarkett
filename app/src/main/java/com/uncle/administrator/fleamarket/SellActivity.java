package com.uncle.administrator.fleamarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.uncle.administrator.fleamarket.Login_Activity.welcome_page;
import com.uncle.bomb.BOMBOpenHelper;
import com.uncle.administrator.fleamarket.DTO.User_account;
import com.uncle.administrator.fleamarket.DTO.shop_goods;
import com.uncle.Util.Turns;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;


/**
 * @author Administrator
 * @date 2016/12/11 0011
 */

public class SellActivity extends Activity {
    private EditText ed_title, ed_detail, ed_price;
    private Button close, bt_send;
    private ImageView img;
    private String title, detail, price;
    private String img_string;//图片的String格式

    private String[] str = new String[10];//存储图片本地地址，发送到云端
    private int picture_number = 0;//统计有多少张照片上传云端
    private String[] img_path;//云端上后返回的地址，云端地址；

    private GridView gridView1;                   //网格显示缩略图
    private Button buttonPublish;                //发布按钮
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private Bitmap add_bmp;
    private List<HashMap<String, Object>> imageItem;
    private List<HashMap<String, String>> bomb_imageItem;//把图片数据换成二进制数，存入云服务器
    private SimpleAdapter simpleAdapter;     //适配器
    private Turns turn = new Turns();
    private String objectID, college, organization, head_portrait, name;//自己的id,大学名字，学院，头像,名字，买东西的时候存入数据库


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        Bmob.initialize(this, "144dbb1fbca09ce5d3af201a05c54628");

        init();
        select_photos();
        send();
    }

    public void init() {
        ed_detail = (EditText) findViewById(R.id.sell_ed_detail);
        ed_title = (EditText) findViewById(R.id.sell_ed_title);
        ed_price = (EditText) findViewById(R.id.sell_ed_price);
        close = (Button) findViewById(R.id.sell_bt_close);
        bt_send = (Button) findViewById(R.id.sell_bt_send);
        gridView1 = (GridView) findViewById(R.id.gridView1);
        get_data_from_sharepreference();
    }

    private void get_data_from_sharepreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_WORLD_READABLE);
        if (sharedPreferences.getString("object_id", "没有id").equals("没有id")) {
            Intent intent = new Intent(SellActivity.this, welcome_page.class);
            startActivity(intent);
            finish();
        } else {
            objectID = sharedPreferences.getString("object_id", "没有id");
            college = sharedPreferences.getString("college", null);
            organization = sharedPreferences.getString("organization", null);
            name = sharedPreferences.getString("nick_name", null);
            BOMBOpenHelper bomb = new BOMBOpenHelper();
            bomb.findAccountDataAlone(objectID, new BOMBOpenHelper.FindAccountDataAloneCallback() {
                @Override
                public void onSuccess(User_account object) {
                    head_portrait = object.getAvatar();
                }
            });
        }


    }

    public void send() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = ed_title.getText().toString().trim();
                detail = ed_detail.getText().toString().trim();
                price = ed_price.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(SellActivity.this, "没有标题怎么卖呢~~", Toast.LENGTH_SHORT).show();
                } else if (detail.isEmpty()) {
                    Toast.makeText(SellActivity.this, "再描述点细节吧~~", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(SellActivity.this, "没有价格你要免费送给我吗~~", Toast.LENGTH_SHORT).show();
                } else if (imageItem.size() <= 3) {
                    Toast.makeText(SellActivity.this, "要有三张以上图片才能让别人了解你哟~~", Toast.LENGTH_SHORT).show();
                } else {//开启数据库
                    final shop_goods shopgoods = new shop_goods(str[0], str[1], str[2], str[3], str[4], str[5]
                            , str[6], str[7], str[8]
                            , title, detail, price, null, 0,
                            picture_number, objectID, college,
                            organization, head_portrait, name);
                    final BOMBOpenHelper bomb = new BOMBOpenHelper();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            bomb.uploadImg(shopgoods);
                        }
                    };
                    runnable.run();
                    Toast.makeText(SellActivity.this, "发布成功，一会儿就能看到你的信息咯~", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(SellActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 从图库中获取图片并显示在页面中
     */
    public void select_photos() {

         /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();//把图片数据存入队列,通过simpleadapter显现界面
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                        @Override
                                        public boolean setViewValue(View view, Object data,
                                                                    String textRepresentation) {
                                            // TODO Auto-generated method stub
                                            if (view instanceof ImageView && data instanceof Bitmap) {
                                                ImageView i = (ImageView) view;
                                                i.setImageBitmap((Bitmap) data);
                                                return true;
                                            }
                                            return false;
                                        }
                                    }
        );
        gridView1.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                 if (imageItem.size() == 10) { //第一张为默认图片
                                                     Toast.makeText(SellActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                                                 } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                                                     Toast.makeText(SellActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                                                     //选择图片
                                                     Intent intent = new Intent(Intent.ACTION_PICK,
                                                             MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//                                                     intent.setType("image/*");
//                                                     intent.putExtra("crop", true);
//                                                     intent.putExtra("return-data", true);
                                                     startActivityForResult(intent, IMAGE_OPEN);
                                                     //通过onResume()刷新数据
                                                 } else {
                                                     dialog(position);
                                                     //Toast.makeText(MainActivity.this, "点击第" + (position + 1) + " 号图片",
                                                     //		Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                                         }
        );
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            add_bmp = BitmapFactory.decodeFile(pathImage);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;//压缩图片质量为原来的1/2
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;//用ARBG_4444色彩模式加载图片
            add_bmp = BitmapFactory.decodeFile(pathImage, options);
            // 生成压缩的图片

            HashMap<String, Object> map = new HashMap<String, Object>();//把图片数据存入队列,通过simpleadapter显现界面
            map.put("itemImage", add_bmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//
//    获取图片路径 响应startActivityForResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                str[picture_number] = pathImage;
                picture_number++;

            }
        }  //end if 打开图片
    }


    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
