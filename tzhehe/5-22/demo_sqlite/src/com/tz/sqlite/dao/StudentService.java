package com.tz.sqlite.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tz.sqlite.entity.Student;

public class StudentService extends BaseDao<Student> {
	public StudentService(SQLiteDatabase db) {
		super(db,"Student");
	}
	
	
	
	 public void createTable(){
			String sb="create table Student ( _id int ,_name varchar(20))";
			db.execSQL(sb);
	 }
	 
	//查询所有学生
		public List<Student> qureyAll(){
			List<Student> students = new ArrayList<Student>();
			Cursor cursor = db.rawQuery("select * from student", null);
			Student student;
			while(cursor.moveToNext()){
				//每查到一个学生
				student = new Student();
				String name = cursor.getString(cursor.getColumnIndex("_name"));
				student.set_name(name);
				int _id = cursor.getInt(cursor.getColumnIndex("_id"));
				student.set_id(_id);
				students.add(student);
			}
			return students;
		}
		
	

}
