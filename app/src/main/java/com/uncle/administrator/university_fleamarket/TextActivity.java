package com.uncle.administrator.university_fleamarket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

/**
 *
 * @author Administrator
 * @date 2016/11/25 0025
 * 这个是首页广告的内容
 */

public class TextActivity extends Activity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        init();
        get_advantage(iv);
    }
    public void init(){
        iv = (ImageView) findViewById(R.id.text_image);
    }

    public void get_advantage(ImageView iv){
        int i = getIntent().getIntExtra("image",0);
       if(i == 1){
            iv.setImageResource(R.drawable.a);
        }
        if(i == 2){
            iv.setImageResource(R.drawable.b);
        }
        if(i == 3){
            iv.setImageResource(R.drawable.c);
        }
        if(i == 4){
            iv.setImageResource(R.drawable.d);
        }



    }
}
