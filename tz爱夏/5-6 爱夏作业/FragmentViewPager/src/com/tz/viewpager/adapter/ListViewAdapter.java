package com.tz.viewpager.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tz.viewpager.R;

public class ListViewAdapter extends BaseAdapter {
	private List<Map<String,Object>> datas;
	private Context context;
	private LayoutInflater mInflater;

	public ListViewAdapter(Context context, List<Map<String,Object>> datas) {
		this.context = context;
		this.datas = datas;
		this.mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return datas.size();
	}

	public Object getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("INFO", position+"position");
		ViewHolder holder = null;
		if (convertView == null) {
			holder=new ViewHolder();
			convertView = mInflater.inflate(R.layout.item,
					null);
			holder.imageView=(ImageView) convertView.findViewById(R.id.imageView);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map=datas.get(position);
		holder.imageView.setImageResource((Integer) map.get("img"));
		holder.title.setText(map.get("title").toString());
		holder.content.setText(map.get("content").toString());
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView content;
	}
}
