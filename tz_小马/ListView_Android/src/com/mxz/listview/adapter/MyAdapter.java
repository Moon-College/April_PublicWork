package com.mxz.listview.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxz.listview.R;
import com.mxz.listview.adapter.entity.Hero;

public class MyAdapter extends BaseAdapter {
	private List<Hero> list;
	private Context context;
	private LayoutInflater inflater;
	public MyAdapter(Context context,List<Hero> list){
		this.list=list;
		this.context=context;
		this.inflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Hero hero=list.get(position);
		View v=inflater.inflate(R.layout.listview,null);
		ImageView face=(ImageView)v.findViewById(R.id.face);
		TextView name=(TextView)v.findViewById(R.id.name);
		TextView phone=(TextView)v.findViewById(R.id.phone);
		face.setImageResource(hero.getFace());
		name.setText(hero.getName());
		phone.setText(hero.getPhone());
		Log.i("INFO", "ss");
		return v;
	}

}
