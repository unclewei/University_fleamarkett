package com.uncle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class goods_SQL_OpenHelper extends SQLiteOpenHelper {

	public goods_SQL_OpenHelper(Context context) {
		super(context,"University_Shopping.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table shop_goods (id_number integer PRIMARY KEY autoincrement,"
				+ " sell_title varchar,"
				+ " sell_detail varchar,"
				+ " sell_price varchar,"
				+ " sell_img1 varchar,"
				+ " sell_img2 varchar,"
				+ " sell_img3 varchar,"
				+ " sell_img4 varchar,"
				+ " sell_img5 varchar,"
				+ " sell_img6 varchar,"
				+ " sell_img7 varchar,"
				+ " sell_img8 varchar,"
				+ " sell_img9 varchar,"
				+ "img_nub integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
