package com.uncle.method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.uncle.administrator.university_fleamarket.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class get_internet_image {
    private Bitmap bitmap;
    private static final int CHANG_UI = 1;
    private Handler hander;
    private  File file;

    // 如果缓存里面有就从缓存获取，否则网络获取图片，返回Drawable对象
    public  Drawable loadImageFromNetwork(Context context, String imageUrl)
    {
        Drawable drawable = null;
        if(imageUrl == null )
            return null;
        String imagePath = "";
        String   fileName   = "";

        // 获取url中图片的文件名与后缀
        if(imageUrl!=null&&imageUrl.length()!=0){
            fileName  = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        }

        // 图片在手机本地的存放路径,注意：fileName为空的情况
        imagePath = context.getCacheDir() + "/" + fileName;

        Log.i("test","imagePath = " + imagePath);
         file = new File(context.getCacheDir(),fileName);// 保存文件
        Log.i("test","file.toString()=" + file.toString());
        if(!file.exists()&&!file.isDirectory())
        {
            try {
                // 可以在这里通过文件名来判断，是否本地有此图片

                FileOutputStream fos=new   FileOutputStream( file );
                InputStream is = new URL(imageUrl).openStream();
                int   data = is.read();
                while(data!=-1){
                    fos.write(data);
                    data=is.read();
                }
                fos.close();
                is.close();
//				drawable = Drawable.createFromStream(
//						new URL(imageUrl).openStream(), file.toString() ); // (InputStream) new URL(imageUrl).getContent();
                drawable = Drawable.createFromPath(file.toString());
                Log.i("test", "file.exists()不文件存在，网上下载:" + drawable.toString());
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
        }else
        {
            drawable = Drawable.createFromPath(file.toString());
            Log.i("test", "file.exists()文件存在，本地获取");
        }

        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }
    public  Drawable loadImageFromNetworkmini(Context context, String imageUrl)
    {
        Drawable drawable = null;
        if(imageUrl == null )
            return null;
        String imagePath = "";
        String   fileName   = "";

        // 获取url中图片的文件名与后缀
        if(imageUrl!=null&&imageUrl.length()!=0){
            fileName  = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        }
        // 图片在手机本地的存放路径,注意：fileName为空的情况
        imagePath = context.getCacheDir() + "/" + fileName;
        Log.i("test","imagePath = " + imagePath);
        file = new File(context.getCacheDir(),fileName);// 保存文件
        Log.i("test","file.toString()=" + file.toString());
        if(!file.exists()&&!file.isDirectory())
        {
            try {
                // 可以在这里通过文件名来判断，是否本地有此图片
                FileOutputStream fos=new   FileOutputStream( file );
                InputStream is = new URL(imageUrl).openStream();
                int   data = is.read();
                while(data!=-1){
                    fos.write(data);
                    data=is.read();
                }
                fos.close();
                is.close();
//				drawable = Drawable.createFromStream(
//						new URL(imageUrl).openStream(), file.toString() ); // (InputStream) new URL(imageUrl).getContent();
                drawable = Drawable.createFromPath(file.toString());
                Bitmap bitmap = turns.drawableToBitmap(drawable);
                drawable =ratio(bitmap,720,1024);

//                Log.i("test", "file.exists()不文件存在，网上下载:" + drawable.toString());
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
        }else {
            if (file.toString().contains(".jpg")||file.toString().contains(".png")) {
            drawable = Drawable.createFromPath(file.toString());
            Log.i("避孕套", file.toString());

            Log.i("避孕套", drawable.toString());

                Bitmap bitmap = turns.drawableToBitmap(drawable);
                drawable = ratio(bitmap, 720, 1024);
//            Log.i("test", "file.exists()文件存在，本地获取");
            }else {
                drawable = context.getResources().getDrawable(R.drawable.bg_button_love);
            }
        }

        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }


    public File get_file_path(){

        if (file.exists()){
             return file;
        }else {
            return null;
        }
    }

//压缩图片
    public Drawable ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        Drawable drawable =  new BitmapDrawable(bitmap);
        return drawable;
    }

}
