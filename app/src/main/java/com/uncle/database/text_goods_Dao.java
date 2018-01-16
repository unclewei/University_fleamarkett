package com.uncle.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.test.AndroidTestCase;

public class text_goods_Dao extends AndroidTestCase {
	private goods_SQL_OpenHelper helper;
	public void textCreatDB() throws Exception{
	helper = new goods_SQL_OpenHelper(getContext());
	helper.getWritableDatabase();
	}
	
	
	public void textadd() throws Exception{
		goods_Dao dao = new goods_Dao(getContext());
		dao.add("苹果7八成新面交","这个月刚刚买了苹果7，但是因为开发的原因，要使用安卓机，8成新，只能面交","7000",null);
		System.out.println("it work");
		
	}
	
	public void textfind() throws Exception{
		goods_Dao dao = new goods_Dao(getContext());
		ArrayList<HashMap<String, Object>> result =dao.find();
		System.out.println(result);
	}
	
	public void delete()  throws Exception{
		goods_Dao dao = new goods_Dao(getContext());
		dao.delete();
	}
	
}
