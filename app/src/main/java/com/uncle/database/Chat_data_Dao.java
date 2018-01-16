package com.uncle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class Chat_data_Dao {

    private Chat_SQL_OpenHelper helper;

    public Chat_data_Dao(Context context) {
        helper = new Chat_SQL_OpenHelper(context);
        helper.getWritableDatabase();
    }

    /**
     * 增加一条数据
     */
    public void add_one_data_to_Chat_database(String name, String target_objectID, String head_portrait, String time, String last_sentence) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("target_objectID", target_objectID);
        values.put("head_portrait", head_portrait);
        values.put("time", time);
        values.put("last_sentence", last_sentence);
        db.insert("chat_data", null, values);
        db.close();
    }

    public interface Find_data_from_Chat_database_callback {
        public void onFail();

        public void onSuccess();
    }

    /**
     * 查找数据 直接把全部数据加载进hashmap中
     *
     * @return
     */
    public void find_data_from_Chat_database(String objectid, Find_data_from_Chat_database_callback find_data_from_chat_database_callback) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("chat_data", null, "target_objectID = ?", new String[]{objectid}, null, null, null);
        int count = cursor.getCount();
        if (count == 0) {
            find_data_from_chat_database_callback.onFail();
        } else {
            System.out.println("找到的条数：" + count);
            find_data_from_chat_database_callback.onSuccess();
        }
        cursor.close();
        db.close();

    }


    public ArrayList<HashMap<String, Object>> find_and_get_data_from_Chat_database() {
        ArrayList<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("chat_data", null, null, null, null, null, null);
        int count = cursor.getCount();
        if (count == 0) {
            return null;
        } else {
            System.out.println("找到的条数：" + count);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashmap = new HashMap<>();
                hashmap.put("id_number", cursor.getInt(0));
                hashmap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                hashmap.put("target_objectID", cursor.getString(cursor.getColumnIndex("target_objectID")));
                hashmap.put("head_portrait", cursor.getString(cursor.getColumnIndex("head_portrait")));
                hashmap.put("time", cursor.getString(cursor.getColumnIndex("time")));
                hashmap.put("last_sentence", cursor.getString(cursor.getColumnIndex("last_sentence")));
                ret.add(hashmap);
            }
            cursor.close();
            db.close();
            return ret;
        }
    }

    /**
     * 更新时间和最后一句
     */
    public void update_data_from_database(String target_objectID, String time,String head, String last_sentence) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
//实例化内容值 ContentValues values = new ContentValues();
//在values中添加内容
        values.put("time", time);
        values.put("last_sentence", last_sentence);
        values.put("head_portrait", head);
//修改条件
        String whereClause = "target_objectID=?";
//修改添加参数
        String[] whereArgs = {target_objectID};
//修改
        db.update("chat_data", values, whereClause, whereArgs);
    }

    //删除指定id的数据
    public void delete_by_targetID(String targetID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("chat_data", "target_objectID=?", new String[]{targetID});
        db.close();
    }


    /**
     * 删除所有数据
     */
    public void delete() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("chat_data", null, null);
        db.close();

    }

}
