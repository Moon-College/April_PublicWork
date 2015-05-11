package com.tz.viewpager.adapter;

import java.util.List;

import com.tz.viewpager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int[] images = new int[] { R.drawable.beauty1, R.drawable.beauty2,
			R.drawable.beauty3, R.drawable.beauty4, R.drawable.beauty5,
			R.drawable.beauty6, R.drawable.beauty7, R.drawable.beauty8 };

	public ImageAdapter(Context context) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return images.length;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.pic_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageDrawable(context.getResources().getDrawable(
				images[position]));
		return convertView;
	}

	class ViewHolder {
		private ImageView imageView;
	}

	public Object getItem(int position) {
		return null;
	}
}
