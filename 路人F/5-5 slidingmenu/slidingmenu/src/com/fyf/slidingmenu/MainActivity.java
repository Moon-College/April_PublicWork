package com.fyf.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import com.fyf.heraAdapter.bean.Heros;
import com.fyf.heroAdapter.HeroAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnClickListener, OnItemSelectedListener{
    /** Called when the activity is first created. */
	private SlidingMenu mMenu;
	private ListView heroListView;
	private Button btnButton;
	private HeroAdapter adapter;
	private List<Heros> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initData();
        initView();
    }
    /**
     * 初始化数据
     */
	private void initData() {
		// TODO Auto-generated method stub
		data = new ArrayList<Heros>();
		String [] nameStrings = new String[]{
			"张飞",
			"曹操",
			"孙权",
			"吕布",
			"诸葛亮",
			"貂蝉",
			"关羽",
			"刘备"
		};
		int [] faces = new int[]{
			R.drawable.face1,	
			R.drawable.face2,
			R.drawable.face3,
			R.drawable.face4,
			R.drawable.face5,
			R.drawable.face6,
			R.drawable.face7,
			R.drawable.face8,
		};
		for(int i = 0;i<8;i++){
			Heros heros = new Heros();
			heros.setNameString(nameStrings[i]);
			heros.setNumberString("123456789");
			heros.setFaceImg(faces[i]);
			data.add(heros);
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		mMenu = (SlidingMenu) findViewById(R.id.id_sldingmenu);
		heroListView = (ListView) findViewById(R.id.herolistview);
		
		adapter = new HeroAdapter(this, data);
		heroListView.setAdapter(adapter);
		heroListView.setOnItemClickListener(this);
		heroListView.setOnItemSelectedListener(this);
		
		btnButton = (Button) findViewById(R.id.button1);
		btnButton.setOnClickListener(this);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.i("info", "dianji");
		TextView textView = (TextView) view.findViewById(R.id.nametext);
		Toast.makeText(MainActivity.this, textView.getText(), 2000).show();
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Toast.makeText(MainActivity.this, "11111", 2000).show();
			break;

		default:
			break;
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		TextView textView = (TextView) view.findViewById(R.id.nametext);
		Toast.makeText(MainActivity.this, textView.getText(), 2000).show();
		// TODO Auto-generated method stub
		
	}
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}