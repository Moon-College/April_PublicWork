package com.mxz.sqlitepro;

import java.util.Iterator;
import java.util.List;

import com.mxz.sqlitepro.bean.Emp;
import com.mxz.sqlitepro.service.DBService;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    private SQLiteDatabase db;
	private ContentValues values;
	private Button but;
	private LinearLayout linear;
	private DBService service;
	private EditText edit;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.but=(Button) findViewById(R.id.but);
        this.but.setOnClickListener(this);
        this.linear=(LinearLayout) findViewById(R.id.linear);
        this.edit=(EditText) findViewById(R.id.edit);
        
        initDatabase("demo.db");
        service=new DBService();
    }
    
	private void initDatabase(String dbname) {
		db = this.openOrCreateDatabase(dbname, this.MODE_PRIVATE, null);
		try{
			db.execSQL("create table emp(_id integer primary key autoincrement,name text)");	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	} 

	public void onClick(View v) {
		values = new ContentValues();
		values.put("name", "zhangsan");
		db.insert("emp", null, values);
		values = new ContentValues();
		values.put("name", "lisi");
		db.insert("emp", null, values);
		values = new ContentValues();
		values.put("name", "wangwu");
		db.insert("emp", null, values);
		values = new ContentValues();
		values.put("name", "zhaoliu");
		db.insert("emp", null, values);
	}
	
	public void queryEmpData(View v){
		List<Emp> emps=service.getAll(db);
		Iterator<Emp> iter=emps.iterator();
		while(iter.hasNext()){
			Emp emp=iter.next();
			TextView text=new TextView(this);
			text.setText(emp.getName());
			this.linear.addView(text);
		}
	}
	
	public void queryId(View v){
		Emp emp=service.getEmpid(db, Integer.parseInt(this.edit.getText().toString().trim()));
		TextView text=new TextView(this);
		text.setText(emp.getId()+"-----"+emp.getName());
		this.linear.addView(text);
	}
	
	public void queryName(View v){
		List<Emp> emps=service.getEmpName(db, this.edit.getText().toString().trim());
		Iterator<Emp> iter=emps.iterator();
		while(iter.hasNext()){
			Emp emp=iter.next();
			TextView text=new TextView(this);
			text.setText(emp.getId()+"-*****-"+emp.getName());
			this.linear.addView(text);
		}
	}
	
	public void updateAll(View v){
		int count = service.updateAll(db, this.edit.getText().toString().trim());
		TextView text=new TextView(this);
		text.setText("更新成功: 数量-  "+count);
		this.linear.addView(text);
	}
	
	public void updateId(View v){
		int count = service.updateName(db, 1, this.edit.getText().toString().trim());
		TextView text=new TextView(this);
		text.setText("更新成功: 数量-  "+count);
		this.linear.addView(text);
	}
    
	public void deleteAll(View v){
		int count = service.deleteAll(db);
		TextView text=new TextView(this);
		text.setText("删除成功: 数量-  "+count);
		this.linear.addView(text);
	}

	public void deleteId(View v) {
		int count = service.deleteId(db, Integer.parseInt(this.edit.getText().toString().trim()));
		TextView text=new TextView(this);
		text.setText("删除成功: 数量-  "+count);
		this.linear.addView(text);
	}

	public void deleteName(View v) {
		int count = service.deleteName(db, this.edit.getText().toString().trim());
		TextView text=new TextView(this);
		text.setText("删除成功: 数量-  "+count);
		this.linear.addView(text);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db!=null){
			db.close();
		}
			
	}
    
}