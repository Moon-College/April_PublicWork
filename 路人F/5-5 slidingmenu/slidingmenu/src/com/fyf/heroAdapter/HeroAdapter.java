package com.fyf.heroAdapter;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.fyf.heraAdapter.bean.Heros;
import com.fyf.slidingmenu.R;
import com.fyf.slidingmenu.SlidingMenu;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeroAdapter extends BaseAdapter{
	//数据
	private List<Heros> data ;
	//资源加载器
	private Context context;
	private LayoutInflater inflater;
	public HeroAdapter(Context context,List<Heros> data) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.data=data;
		//创建布局加载器
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//view指的是该布局文件最外层的容器
		Heros heros = data.get(position);
		View view = inflater.inflate(R.layout.list_item, null);
		ImageView face = (ImageView) view.findViewById(R.id.face_img);
		TextView nameTextView = (TextView) view.findViewById(R.id.nametext);
		TextView numberTextView = (TextView) view.findViewById(R.id.numbertext);
		face.setImageResource(heros.getFaceImg());
		nameTextView.setText(heros.getNameString());
		numberTextView.setText(heros.getNumberString());
		return view;
	}

}
