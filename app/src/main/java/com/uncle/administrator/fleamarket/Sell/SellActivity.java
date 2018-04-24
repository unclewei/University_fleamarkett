package com.uncle.administrator.fleamarket.Sell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.uncle.Base.BaseBindAdapter;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.Util.GlideImageLoader;
import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.ActivitySellBinding;
import com.uncle.bomb.BOMBOpenHelper;
import com.uncle.DTO.shopGoods;

import java.util.ArrayList;


/**
 * @author Administrator
 * @date 2016/12/11 0011
 */

public class SellActivity extends BaseBindingActivity<ActivitySellBinding> implements BaseBindAdapter.OnItemLongClickListener<ImageItem>, BaseBindAdapter.OnItemClickListener<ImageItem> {
    private static final int IMAGE_PICKER = 66666;
    private String title, detail, price;
    private ArrayList<String> imgFileList = new ArrayList<>();
    private ArrayList<ImageItem> images;
    private SellImageAdapter adapter;

    @Override
    protected void bindData(ActivitySellBinding dataBinding) {
        initAdapter();
        initImagePicker();
        addImg();
        send();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell;
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        //不允许裁剪
        imagePicker.setCrop(false);
        //矩形裁剪
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //设置是否多选
        imagePicker.setMultiMode(true);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //不显示拍照按钮
        imagePicker.setShowCamera(false);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(640);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(640);
    }

    private void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(SellActivity.this, 4);
        binding.list.setLayoutManager(layoutManager);
        adapter = new SellImageAdapter(SellActivity.this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        binding.list.setAdapter(adapter);
    }

    private void addImg() {
        binding.btAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    public void send() {
        binding.sellBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.sellEdTitle.getText().toString().trim();
                detail = binding.sellEdDetail.getText().toString().trim();
                price = binding.sellEdPrice.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(SellActivity.this, "没有标题怎么卖呢~~", Toast.LENGTH_SHORT).show();
                } else if (detail.isEmpty()) {
                    Toast.makeText(SellActivity.this, "再描述点细节吧~~", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(SellActivity.this, "没有价格你要免费送给我吗~~", Toast.LENGTH_SHORT).show();
                } else if (imgFileList.size() < 3) {
                    Toast.makeText(SellActivity.this, "要有三张以上图片才能让别人了解你哟~~", Toast.LENGTH_SHORT).show();
                } else {//开启数据库
                    final shopGoods shopgoods = new shopGoods(imgFileList
                            , title, detail, price, null, 0,
                            imgFileList.size(), profile.getObjectId(), profile.getCollege(),
                            profile.getOrganization(), profile.getAvatar(), profile.getName());
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


        binding.sellBtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getImageFileList(ArrayList<ImageItem> images) {
        imgFileList.clear();
        for (int i = 0; i < images.size(); i++) {
            imgFileList.add(images.get(i).path);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                getImageFileList(images);
                adapter.setList(images);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onItemLongClick(final ImageItem data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                images.remove(data);
                adapter.setList(images);
                adapter.notifyDataSetChanged();

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

    @Override
    public void onItemClick(ImageItem data) {

    }
}
