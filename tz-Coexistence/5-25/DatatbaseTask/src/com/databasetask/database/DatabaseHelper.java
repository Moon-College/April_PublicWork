package com.databasetask.database;

import java.util.ArrayList;
import java.util.List;

import com.databasetask.constant.Constant;
import com.databasetask.entity.Student;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {
	private SQLiteDatabase db;

	public DatabaseHelper() {
		
	}
	public DatabaseHelper(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * 更新传入的学生ID来查询指定学生
	 * @param id
	 * @return
	 */
	public Student showStudent(int id){
		Student student = new Student();
		String sql = "select * from student where _id = ?";
		db.execSQL(sql,new Object[]{id});
		return student;
	}
	
	/**
	 * 查询所有的学生信息
	 * @return
	 */
	public List<Student> displayAll(){
		List<Student> students = new ArrayList<Student>();
		Student student;
		Cursor cursor = db.rawQuery(Constant.Database.DISPLAY_ALL_STUDENT, null);
		while(cursor.moveToNext()){
			student = new Student();
			student.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			student.set_age(cursor.getInt(cursor.getColumnIndex("_age")));
			student.set_name(cursor.getString(cursor.getColumnIndex("_name")));
			students.add(student);
		}
		return students;
	}
	
	/**
	 * 传入学生对象添加到数据库
	 * @param student
	 */
	public void addStudent(Student student){
		String sql = "insert into student values(null,?,?)";
		db.execSQL(sql,new Object[]{student.get_name(),student.get_age()});
	}
	
	/**
	 * 传入学生对象来更新数据
	 * 更新条件根据传入的学生对象Id
	 * @param student
	 */
	public void updateStudent(Student student){
		String sql = "update student set _name = ?,_age = ? where _id = ?";
		db.execSQL(sql, new Object[]{student.get_name(),student.get_age(),student.get_id()});
	}
}
