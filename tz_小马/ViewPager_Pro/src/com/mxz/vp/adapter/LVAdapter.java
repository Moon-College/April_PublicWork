package com.mxz.vp.adapter;

import java.util.List;

import com.mxz.vp.R;
import com.mxz.vp.bean.City;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LVAdapter extends BaseAdapter {
	private List<City> data;
	private Context context;
	private LayoutInflater inflater;
	public LVAdapter(Context context,List<City> data){
		this.context=context;
		this.data=data;
		this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		return this.data.size();
	}

	public Object getItem(int position) {
		return this.data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		City city=data.get(position);
		Log.i("INFO", "44444444444444");
		ViewHolper vh=null;
		if(convertView==null){
			vh=new ViewHolper();
			convertView=this.inflater.inflate(R.layout.mylvlayout, null);
			vh.img=(ImageView) convertView.findViewById(R.id.img1);
			vh.name=(TextView) convertView.findViewById(R.id.name1);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolper) convertView.getTag();
		}
		vh.img.setImageBitmap(city.getBitmap().get());
		vh.name.setText(city.getName());
		return convertView;
	}
	private class ViewHolper{
		private ImageView img;
		private TextView name;
	}

}
