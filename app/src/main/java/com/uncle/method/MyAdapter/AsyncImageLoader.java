package com.uncle.method.MyAdapter;

/**
 * Created by Administrator on 2017/2/10 0010.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

public class AsyncImageLoader {
    static final Handler handler = new Handler();
    private int i = 0;
    private static HashMap<String, SoftReference<Drawable>> imageCache;
    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();//图片缓存
    }



    // 回调函数
    public interface ImageCallback {
        public void onImageLoad(Integer t, Drawable drawable);
        public void onError(Integer t);
    }
    public interface ImageCallback2 {
        public void onImageLoad2(Drawable drawable);
        public void onError2();
    }

    public Drawable loadDrawable(final Context c,final Integer pos, final String imageUrl,final ImageCallback imageCallback) {

        new Thread() {
            @Override
            public void run() {
                   LoadImg(c,pos, imageUrl, imageCallback);
            }
        }.start();
        return null;
    }// loadDrawable---end


    public static Drawable loadDrawable(final Context c, final String imageUrl, final ImageCallback2 imageCallback2) {

        new Thread() {
            @Override
            public void run() {
                LoadImg(c, imageUrl, imageCallback2);
            }
        }.start();
        return null;
    }// loadDrawable---end



    public static void LoadImg(final Context c, final String imageUrl,
                               final ImageCallback2 imageCallback2) {
        // 首先判断是否在缓存中
        // 但有个问题是：ImageCache可能会越来越大，以至用户内存用光，所以要用SoftReference（弱引用）来实现
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            final Drawable drawable = softReference.get();

            if (drawable != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageCallback2.onImageLoad2(drawable);
                    }
                });
                return;
            }
        }


        // 尝试从URL中加载
        try {
            final Drawable drawable = loadImageFromUrl(c,imageUrl);
            if (drawable != null) {
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
            }
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 imageCallback2.onImageLoad2(drawable);
                             }
                         }
            );
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageCallback2.onError2();
                }
            });
            e.printStackTrace();
        }
    }



    public void LoadImg(final Context c,final Integer pos, final String imageUrl,
                        final ImageCallback imageCallback) {
        // 首先判断是否在缓存中
        // 但有个问题是：ImageCache可能会越来越大，以至用户内存用光，所以要用SoftReference（弱引用）来实现
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            final Drawable drawable = softReference.get();

            if (drawable != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageCallback.onImageLoad(pos, drawable);
                    }
                });
                return;
            }
        }


        // 尝试从URL中加载
        try {
            final Drawable drawable = loadImageFromUrl(c,imageUrl);
            if (drawable != null) {
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageCallback.onImageLoad(pos, drawable);
                }
            }
            );
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageCallback.onError(pos);
                }
            });
            e.printStackTrace();
        }
    }


    // 根据URL加载图片,如果出现错误throws IOException式的错误，以便在LoadImg中捕获，执行OnError（）函数
    public static Drawable loadImageFromUrl(Context c, String url) throws IOException {
        Drawable drawable = loadImageFromNetwork(c,url);
        return drawable;


    }

    // 如果缓存里面有就从缓存获取，否则网络获取图片，返回Drawable对象
    public static Drawable loadImageFromNetwork(Context context, String imageUrl)
    {
        Drawable drawable = null;
        if(imageUrl == null )
            return null;
        String imagePath = "";
        String fileName   = "";

        // 获取url中图片的文件名与后缀
        if(imageUrl!=null&&imageUrl.length()!=0){
            fileName  = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        }
        // 图片在手机本地的存放路径,注意：fileName为空的情况
        imagePath = context.getCacheDir() + "/" +"mini."+ fileName;
        File file = new File(context.getCacheDir(),fileName);// 保存文件
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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;//压缩图片质量为原来的1/8
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;//用ARBG_4444色彩模式加载图片
                Bitmap add_bmp = BitmapFactory.decodeFile(file.toString(),options);
                drawable =  new BitmapDrawable(add_bmp);
                // 生成压缩的图片

//                drawable = Drawable.createFromPath(file.toString());
//                Log.i("test", "file.exists()不文件存在，网上下载:" + drawable.toString());

//                try {
//                    InputStream temp = context.getAssets().open(imageUrl);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeStream(temp, null, options);
//                    // 关闭流
//                    temp.close();
//                    int i = 0;
//                    int size = 150;
//                    while (true) {
//                        // 这一步是根据要设置的大小，使宽和高都能满足
//                        if ((options.outWidth >> i <= size)
//                                && (options.outHeight >> i <= size)) {
//                            // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
//                            temp = context.getAssets().open(imageUrl);
//                            // 这个参数表示 新生成的图片为原始图片的几分之一。
//                            options.inSampleSize = (int) Math.pow(2.0D, i);
//                            // 这里之前设置为了true，所以要改为false，否则就创建不出图片
//                            options.inPreferredConfig = Bitmap.Config.ARGB_4444;//用ARBG_4444色彩模式加载图片
//                            options.inJustDecodeBounds = false;
//                            Bitmap add_bmp = BitmapFactory.decodeStream(temp, null, options);
//                            drawable =  new BitmapDrawable(add_bmp);
//                            break;
//                        }
//                        i += 1;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//上面方法是通过标准缩放降低图片质量

            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
        }else
        {
//            drawable = Drawable.createFromPath(file.toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;//压缩图片质量为原来的1/10
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;//用ARBG_4444色彩模式加载图片
            Bitmap add_bmp = BitmapFactory.decodeFile(file.toString(),options);
            drawable =  new BitmapDrawable(add_bmp);
            Log.i("test", "file.exists()文件存在，本地获取");
        }

        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }
}