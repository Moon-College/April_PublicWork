package com.example.tz_sqlite;

import java.util.List;

import com.example.bean.Student;
import com.example.openHelper.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private String TAG = "MainActivity";
	private Button mdb, mQuery, mQueryId, mQueryName, mUpdate, mDelId, mDel;
	private DataBaseHelper db;
	private Student stu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setListener();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mdb = (Button) findViewById(R.id.mdb);
		mQuery = (Button) findViewById(R.id.mquery);
		mQueryId = (Button) findViewById(R.id.mqueryid);
		mQueryName = (Button) findViewById(R.id.mqueryname);
		mUpdate = (Button) findViewById(R.id.mupdate);
		mDelId = (Button) findViewById(R.id.mdelid);
		mDel = (Button) findViewById(R.id.mdel);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		mdb.setOnClickListener(this);
		mQuery.setOnClickListener(this);
		mQueryId.setOnClickListener(this);
		mQueryName.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
		mDelId.setOnClickListener(this);
		mDel.setOnClickListener(this);
	}

	private void initData() {
		stu = new Student(5, "张三", "12");
		db = new DataBaseHelper(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mdb:
			db.Insert(stu);
			break;
		case R.id.mquery:
			List<Student> list = db.Query();
			for (Student stu : list) {
				Log.i(TAG, "[_id=" + stu.get_id() + ",name=" + stu.getName()
						+ ",grade=" + stu.getGrade() + "]");
			}
			break;
		case R.id.mqueryid:
			Student stu = db.QueryById(8);
			Log.i(TAG, stu.toString());

			break;
		case R.id.mqueryname:
			List<Student> li = db.QueryByName("张三");
			for (Student st : li) {
				Log.i(TAG, st.toString());
			}
			break;
		case R.id.mupdate:
			db.UpdateByName(8);
			break;
		case R.id.mdelid:
            db.DelById(8);
			break;
		case R.id.mdel:
            db.DelAll();
			break;

		default:
			Log.i(TAG, "误操作");
			break;
		}
	}
}
