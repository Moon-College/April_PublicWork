package com.example.openHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bean.Student;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "tz.db";
	private static final int SCHEMA_VERSION = 2;// 版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		// 创建一个student表,_id,name
		String exSQL = "create table student(_id integer primary key autoincrement,name varchar(20),grade varchar(50))";
		try {
			db.execSQL(exSQL);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void Insert(Student stu) {
		String sql = "insert into student values(null,?,?)";
		getWritableDatabase().execSQL(sql,
				new String[] { stu.getName(), stu.getGrade() });
	}

	public List<Student> Query() {
		List<Student> list = new ArrayList<Student>();
		Student stu = null;
		String sql = "select * from student";
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			stu = new Student();
			stu.set_id(Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("_id"))));
			stu.setName(cursor.getString(cursor.getColumnIndex("name")));
			stu.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
			list.add(stu);

		}
		cursor.close();
		return list;
	}

	public Student QueryById(int id) {

		Student stu = new Student();
		String sql = "select * from student where _id=" + id;
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			stu = new Student();
			stu.set_id(Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("_id"))));
			stu.setName(cursor.getString(cursor.getColumnIndex("name")));
			stu.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
		}
		cursor.close();
		return stu;
	}

	public List<Student> QueryByName(String name) {
		List<Student> list = new ArrayList<Student>();
		Student stu = null;
		String sql = "select * from student where name=?";
		Cursor cursor = getReadableDatabase().rawQuery(sql,
				new String[] { name });
		while (cursor.moveToNext()) {
			stu = new Student();
			stu.set_id(Integer.valueOf(cursor.getString(cursor
					.getColumnIndex("_id"))));
			stu.setName(cursor.getString(cursor.getColumnIndex("name")));
			stu.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
			list.add(stu);
		}
		cursor.close();
		return list;
	}

	public void UpdateByName(int id) {
		String sql = "update student set name='arror' where _id=" + id;
        getWritableDatabase().execSQL(sql);
		this.Query();
	}

	public void DelAll() {
		String sql = "delete from student";
		getWritableDatabase().execSQL(sql);

		this.Query();
	}

	public void DelById(int id) {
		String sql = "delete from student where _id=" + id;
         getWritableDatabase().execSQL(sql);
		this.Query();
	}

}
