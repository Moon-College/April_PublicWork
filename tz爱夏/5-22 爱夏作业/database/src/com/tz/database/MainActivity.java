package com.tz.database;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tz.database.service.MyDatabase;
import com.tz.database.service.OperateTable;

public class MainActivity extends Activity implements OnClickListener {
	private Button database,add,delete,update,query,deleteAll;
	private MyDatabase myDatabase;
	private OperateTable operrate;
	private SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myDatabase=new MyDatabase();
        operrate=new OperateTable();
        initView();
        
    }
    
    /**
     * @author jackie
     */
	private void initView() {
		database=(Button) findViewById(R.id.database);
		add=(Button) findViewById(R.id.add);
		delete=(Button) findViewById(R.id.delete);
		deleteAll=(Button) findViewById(R.id.deleteAll);
		update=(Button) findViewById(R.id.update);
		query=(Button) findViewById(R.id.query);
		database.setOnClickListener(this);
		add.setOnClickListener(this);
		delete.setOnClickListener(this);
		deleteAll.setOnClickListener(this);
		update.setOnClickListener(this);
		query.setOnClickListener(this);
	}

	public void onClick(View v) {
           switch (v.getId()) {
		case R.id.database:
			db=myDatabase.createOrOpenDatabase(this);
			try{
			myDatabase.createTable();
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		case R.id.add:
			operrate.addData(db);
			break;
		case R.id.delete:
			operrate.deleteDataById(db, 2);
			break;
		case R.id.deleteAll:
			operrate.deleteAllData(db);
			break;
		case R.id.update:
			operrate.updateDataById(db, 3);
			break;
		case R.id.query:
			operrate.quaryAllData(db);
			break;

		default:
			break;
		}		
	}
}