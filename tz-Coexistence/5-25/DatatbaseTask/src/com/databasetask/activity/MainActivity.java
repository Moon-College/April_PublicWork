package com.databasetask.activity;

import java.util.List;

import com.databasetask.R;
import com.databasetask.constant.Constant;
import com.databasetask.database.DatabaseHelper;
import com.databasetask.entity.Student;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView result;
	SQLiteDatabase db;
	DatabaseHelper databaseHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createDatabase();
		
		result = (TextView) findViewById(R.id.result);
		databaseHelper = new DatabaseHelper(db);
		//databaseHelper.addStudent(new Student(18,"jack"));//添加
		//databaseHelper.addStudent(new Student(20,"rose"));
		//databaseHelper.updateStudent(new Student(1, 25, "xiaoming"));//更新
		//databaseHelper.showStudent(1);//更新ID查询
		
		List<Student> students = databaseHelper.displayAll();
		for (Student s : students) {
			result.append("id = "+s.get_id());
			result.append("\nname = "+s.get_name());
			result.append("\nage = "+s.get_age()+"\n\n");
		}
	}

	private void createDatabase() {
		db = openOrCreateDatabase(Constant.Database.DATABASE_NAME, Context.MODE_PRIVATE, null);
		db.execSQL(Constant.Database.CREATE_TABLE_SQL);// 创建表
	}

}
