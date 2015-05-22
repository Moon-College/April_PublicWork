package com.mxz.listview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mxz.listview.adapter.MyAdapter;
import com.mxz.listview.adapter.entity.Hero;

public class ListView_AndroidActivity extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */
	private ListView lv;
	private MyAdapter adapter;
	private List<Hero> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        initData();
        initView();
    }

	private void initData() {
		data=new ArrayList<Hero>();
		String name[]=new String[]{"aa","aa","aa","aa","aa","aa","aa","aa"};
		int img[]=new int[]{R.drawable.face1,R.drawable.face2,R.drawable.face3,R.drawable.face4,R.drawable.face5,R.drawable.face6,R.drawable.face7,R.drawable.face8};
		for (int i = 0; i <8; i++) {
			Hero hero=new Hero();
			hero.setFace(img[i]);
			hero.setName(name[i]);
			hero.setPhone(i+"");
			data.add(hero);
		}
	}

	private void initView() {
		this.lv=(ListView)this.findViewById(R.id.lv);
		adapter=new MyAdapter(this,data);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView name=(TextView)view.findViewById(R.id.name);
		Toast.makeText(this, ""+name.getText(), 1000).show();
	}
}