package com.mxz.sqlitepro.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mxz.sqlitepro.bean.Emp;

public class DBService {
	private Emp emp;
	private List<Emp> emps;

	public List<Emp> getAll(SQLiteDatabase db){
		emps = new ArrayList<Emp>();
		Cursor cursor=db.rawQuery("select * from emp", null);
		while(cursor.moveToNext()){
			emp=new Emp();
			String name=cursor.getString(cursor.getColumnIndex("name"));
			emp.setName(name);
			emps.add(emp);
		}
		cursor.close();
		return emps;
	}
	
	public Emp getEmpid(SQLiteDatabase db,int id){
		Cursor cursor=db.rawQuery("select * from emp where _id = ?", new String[]{String.valueOf(id)});
		while(cursor.moveToNext()){
			emp=new Emp();
			String name=cursor.getString(cursor.getColumnIndex("name"));
			emp.setName(name);
			emp.setId(id);
		}
		cursor.close();
		return emp;
	}
	
	public List<Emp> getEmpName(SQLiteDatabase db,String name){
		emps = new ArrayList<Emp>();
		Cursor cursor=db.rawQuery("select * from emp where name like ?",new String[]{"%"+name+"%"});
		while(cursor.moveToNext()){
			emp=new Emp();
			String _id=cursor.getString(cursor.getColumnIndex("_id"));
			String _name=cursor.getString(cursor.getColumnIndex("name"));
			emp.setId(Integer.parseInt(_id));
			emp.setName(_name);
			emps.add(emp);
		}
		cursor.close();
		return emps;
	}
	
	public int updateAll(SQLiteDatabase db,String name){
		int count=0;
		ContentValues values=new ContentValues();
		values.put("name", name);
		count=db.update("emp", values, null, null);
		return count;
	}
	
	public int updateName(SQLiteDatabase db,int id,String name){
		int count=0;
		ContentValues values=new ContentValues();
		values.put("name", name);
		count=db.update("emp", values, "_id=?", new String[]{String.valueOf(id)});
		return count;
	}
	
	public int deleteAll(SQLiteDatabase db){
		int count=0;
		count=db.delete("emp", null, null);
		return count;
	}
	
	public int deleteName(SQLiteDatabase db,String name){
		int count=0;
		count=db.delete("emp", "name=?", new String[]{name});
		return count;
	}
	
	public int deleteId(SQLiteDatabase db,int id){
		int count=0;
		count=db.delete("emp", "_id=?", new String[]{String.valueOf(id)});
		return count;
	}
	
	
}
