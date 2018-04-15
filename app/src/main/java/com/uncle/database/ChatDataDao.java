package com.uncle.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uncle.DTO.ConversationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/4/25 0025
 */

public class ChatDataDao {

    private ChatSQLOpenHelper helper;
    private static ChatDataDao chatHelper;

    public ChatDataDao(Context context) {
        helper = new ChatSQLOpenHelper(context);
        helper.getWritableDatabase();
    }

    public static ChatDataDao getInstance(Context context) {
        if (chatHelper == null) {
            synchronized (ChatDataDao.class) {
                if (chatHelper == null) {
                    chatHelper = new ChatDataDao(context);
                }
            }
        }
        return chatHelper;
    }

    /**
     * 增加一条数据
     */
    public void addChatDB(String name, String targetObjectID, long lastTime, String avatar, String lastWord) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("targetObjectID", targetObjectID);
        values.put("avatar", avatar);
        values.put("lastTime", lastTime);
        values.put("lastWord", lastWord);
        db.insert("chat_data", null, values);
        db.close();
    }

    /**
     * 查找数据 直接把全部数据加载进hashmap中
     */
    public boolean isUserExist(String objectId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("chat_data", null, "targetObjectID = ?", new String[]{objectId}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }


    public List<ConversationDTO> findConversationByDB() {
        List<ConversationDTO> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("chat_data", null, null, null, null, null, null);
        int count = cursor.getCount();
        if (count == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setUsername(cursor.getString(cursor.getColumnIndex("name")));
            conversationDTO.setObjectId(cursor.getString(cursor.getColumnIndex("targetObjectID")));
            conversationDTO.setLastTime(cursor.getLong(cursor.getColumnIndex("lastTime")));
            conversationDTO.setLastWord(cursor.getString(cursor.getColumnIndex("lastWord")));
            conversationDTO.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            list.add(conversationDTO);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 更新时间和最后一句
     */
    public void updateLastWord(String targetObjectID, String name, long lastTime, String avatar, String lastWord) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("lastTime", lastTime);
        values.put("lastWord", lastWord);
        values.put("avatar", avatar);
        String whereClause = "targetObjectID=?";
        String[] whereArgs = {targetObjectID};
        db.update("chat_data", values, whereClause, whereArgs);
    }

    public void delete(String targetObjectID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("chat_data", "targetObjectID=?", new String[]{targetObjectID});
        db.close();
    }


    /**
     * 删除所有数据
     */
    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("chat_data", null, null);
        db.close();

    }

}
