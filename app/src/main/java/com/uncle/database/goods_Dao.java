package com.uncle.database;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class goods_Dao {
    private goods_SQL_OpenHelper helper;

    public goods_Dao(Context context) {
        helper = new goods_SQL_OpenHelper(context);

    }

    /**
     * 增加一条数据
     * ，只有一张照片
     */
    public void add(String title, String detail, String price, String img1) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sell_title", title);
        values.put("sell_detail", detail);
        values.put("sell_price", price);
        values.put("sell_img1", img1);
        db.insert("shop_goods", null, values);
        db.close();
    }

    /**
     * 查找数据 直接把全部数据加载进hashmap中
     *
     * @return
     */
    public ArrayList<HashMap<String, Object>> find() {
        ArrayList<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("shop_goods", null, null, null, null, null, null);
        int count = cursor.getCount();
        if (count == 0) {
            return null;
        } else {
            System.out.println("找到的条数：" + count);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashmap = new HashMap<>();
                hashmap.put("id_number", cursor.getInt(0));
                hashmap.put("title", cursor.getString(1));
                hashmap.put("detail", cursor.getString(2));
                hashmap.put("price", cursor.getString(3));
                hashmap.put("img1", cursor.getString(4));
                ret.add(hashmap);
            }
            cursor.close();
            db.close();
            return ret;
        }
    }

    /**
     * 删除所有数据
     */
    public void delete() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("shop_goods", null, null);
        db.close();

    }
}
