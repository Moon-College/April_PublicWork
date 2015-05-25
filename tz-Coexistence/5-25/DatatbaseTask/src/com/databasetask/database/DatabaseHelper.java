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
	 * ���´����ѧ��ID����ѯָ��ѧ��
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
	 * ��ѯ���е�ѧ����Ϣ
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
	 * ����ѧ��������ӵ����ݿ�
	 * @param student
	 */
	public void addStudent(Student student){
		String sql = "insert into student values(null,?,?)";
		db.execSQL(sql,new Object[]{student.get_name(),student.get_age()});
	}
	
	/**
	 * ����ѧ����������������
	 * �����������ݴ����ѧ������Id
	 * @param student
	 */
	public void updateStudent(Student student){
		String sql = "update student set _name = ?,_age = ? where _id = ?";
		db.execSQL(sql, new Object[]{student.get_name(),student.get_age(),student.get_id()});
	}
}
