package com.tz.sqlite;

import java.util.List;

import com.tz.sqlite.dao.StudentService;
import com.tz.sqlite.entity.Student;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private SQLiteDatabase db;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = this.openOrCreateDatabase("tz_vip.db", Context.MODE_PRIVATE, null);
        
        
        
        StudentService ss=new StudentService(db);
        
       
        
        Student s=new Student(2,"111111");
        try {
        //	 ss.createTable();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        
        try {
			// ss.save(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
    
       List<Student> list= ss.qureyAll();
		for (Student student : list) {
			Log.i("info",student.get_name());
		}
    }
    
    
}