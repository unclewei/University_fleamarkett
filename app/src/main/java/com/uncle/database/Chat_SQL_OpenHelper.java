package com.uncle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class Chat_SQL_OpenHelper extends SQLiteOpenHelper {
    public Chat_SQL_OpenHelper(Context context) {
        super(context, "Chat_DataBase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chat_data (id_number integer PRIMARY KEY autoincrement,"
                + " name varchar,"
                + " target_objectID varchar,"
                + " head_portrait varchar,"
                + " time varchar,"
                + " last_sentence varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
