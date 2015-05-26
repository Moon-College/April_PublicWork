package com.lwh.search.service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lwh.search.bean.KeyWord;
import com.lwh.search.database.MyDatabaseOpenHelper;

public class SearchService {
	private MyDatabaseOpenHelper helper;
	
	public SearchService(Context context){
		this.helper = new MyDatabaseOpenHelper(context);
	}
	/**
	 * 查找
	 * @param context
	 * @param name
	 */
	public int find(String name){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor =  db.rawQuery("select * from keyword where name=?",new String[]{name});
		cursor.moveToFirst();
		return cursor.getColumnIndex("count");
	}
	/**
	 * 模糊查询
	 */
	public ArrayList<KeyWord> findListKeyword(String rearcherFilter){
		ArrayList<KeyWord> keywords = new ArrayList<KeyWord>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from keyword order by count desc where name like ? limit(0,10)", new String[]{rearcherFilter});
		while(cursor.moveToNext()){
			keywords.add(new KeyWord(cursor.getString(cursor.getColumnIndex("name")),cursor.getInt(cursor.getColumnIndex("count"))));
		}
		cursor.close();
		return keywords;
	}
	/**
	 * 保存
	 * @param context
	 * @param name
	 */
	public void save(String name){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into keyword values(?,?)",new String[]{name,"1"});
	}
	/**
	 * 更新
	 * @param name
	 */
	public void update(String name){
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = find(name);
		db.execSQL("update keyword set count=?",new String[]{String.valueOf(count+1)});
	}
}
