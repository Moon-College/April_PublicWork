package com.tz.database.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OperateTable {
       
	  public void addData(SQLiteDatabase db){
		  String sql="insert into teacher values(null,?,?,?)";
		  db.execSQL(sql,new String[]{"Danny",String.valueOf(27),"programing"});
		  db.execSQL(sql,new String[]{"Devid",String.valueOf(29),"swimming"});
		  db.execSQL(sql,new String[]{"Jason",String.valueOf(32),"gitar"});
		  db.execSQL(sql,new String[]{"Grace",String.valueOf(20),"singing"});
	  }
	  
	  public void deleteDataById(SQLiteDatabase db,int id){
		 // String sql="delete from teacher where _id=?";
		  db.delete("teacher", "_id=?", new String[]{String.valueOf(id)});
	  }
	  
	  public void deleteAllData(SQLiteDatabase db){
		  String sql="delete from teacher";
		  //清空数据后，id自动归0
		  String sql1="update sqlite_sequence SET seq=0 where name='teacher'";
		  db.execSQL(sql);
		  db.execSQL(sql1);
		  Log.i("DELETE", "清空所有数据");
	  }
	  
	  public void updateDataById(SQLiteDatabase db,int id){
		  ContentValues values=new ContentValues();
		  values.put("name", "Jackie");
		  values.put("age", "26");
		  values.put("hobby", "dance");
		  db.update("teacher", values, "_id=?", new String[]{String.valueOf(id)});
		  Log.i("UPDATE", "修改数据");
	  }
	  
	  public void quaryDataById(SQLiteDatabase db,int id){
		  String sql="select * from teacher where _id=?";
		  db.rawQuery(sql, new String[]{"id"});
	  }
	  
	  public void quaryAllData(SQLiteDatabase db){
		  String sql="select * from teacher ";
		  Cursor cursor=db.rawQuery(sql, null);
		  while(cursor.moveToNext()){
			  //每查到一条符合条件的记录就打印
			  String  name=cursor.getString(cursor.getColumnIndex("name"));
			  int  age=cursor.getInt(cursor.getColumnIndex("age"));
			  String  hobby=cursor.getString(cursor.getColumnIndex("hobby"));
			  Log.i("INFO", "name:"+name+"-age:"+age+"-hobby:"+hobby);
		  }
	  }
}
