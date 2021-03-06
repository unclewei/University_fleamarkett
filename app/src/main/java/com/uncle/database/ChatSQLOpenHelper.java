package com.uncle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class ChatSQLOpenHelper extends SQLiteOpenHelper {
    public ChatSQLOpenHelper(Context context) {
        super(context, "Chat_DataBase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chat_data (id_number integer PRIMARY KEY autoincrement,"
                + " name varchar,"
                + " targetObjectID varchar,"
                + " avatar varchar,"
                + " lastTime long,"
                + " lastWord varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
