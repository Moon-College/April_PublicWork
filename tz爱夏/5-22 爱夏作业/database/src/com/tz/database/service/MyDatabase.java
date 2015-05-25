package com.tz.database.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MyDatabase {
	private SQLiteDatabase database;

	// 创建数据库
	public SQLiteDatabase createOrOpenDatabase(Context context) {
		database = context.openOrCreateDatabase("school", context.MODE_PRIVATE,
				null);
		return database;
	}
	
	//创建Teacher表
	public void createTable(){
		database.execSQL("create table teacher(_id Integer primary key autoincrement," +
				"name varchar(20),age Integer,hobby varchar(10))");
	}
}
