package com.uncle.method;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/15 0015.
 */


/**
 *   一、String类型转换为int类型：
 定义：String str;
 a)  int i = Integer.parseInt(str);

 b)  int i = Integer.valueOf(str).intValue();


 二、int类型转换为String类型：
 定义：int intvalue；
 a)  String str = String.valueOf ( intvalue );

 b)  String str = Integer.toString (  intvalue );

 c)  String s = "" + intvalue ;
 */
public class turns {

    /**
     * 02.     * 图片转成string
     * 03.     *
     * 04.     * @param bitmap
     * 05.     * @return
     * 06.
     */
    public  String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * 17.     * string转成bitmap
     * 18.     *
     * 19.     * @param st
     * 20.
     */
    public  Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * bitmap转换成二进制位
     * @param bitmap
     * @return
     */
    public byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    /**
     * 二进制位转换成bitmap
     * @param temp
     * @return
     */
    public Bitmap getBitmapFromByte(byte[] temp){
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

    //    3、Drawable → Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {



        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
    //  .Bitmap---->Drawable
    public static Drawable BitmapTodrawable(Bitmap bmp) {
        Drawable drawable = new BitmapDrawable(bmp);
        return drawable;
    }
}
