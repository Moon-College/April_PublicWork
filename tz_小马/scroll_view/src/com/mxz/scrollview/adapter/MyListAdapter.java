package com.mxz.scrollview.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mxz.scrollview.R;
import com.mxz.scrollview.bean.MyMenu;

public class MyListAdapter extends BaseAdapter {
	private ListView lv;
	private List<MyMenu> data;
	private Context context;
	private LayoutInflater inflater;
	public MyListAdapter(Context context, List<MyMenu> data){
		this.data=data;
		this.context=context;
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
		MyMenu myMenu =data.get(position);
		ViewHelper vh=null;
		if(convertView==null){
			vh=new ViewHelper();
			convertView=this.inflater.inflate(R.layout.lvmenu, null);
			vh.img=(ImageView) convertView.findViewById(R.id.lv_img);
			vh.text=(TextView) convertView.findViewById(R.id.lv_tv);
			convertView.setTag(vh);
		}else{
			vh=(ViewHelper) convertView.getTag();
		}
		if(myMenu.getBitmap().get()==null){
			vh.img.setImageResource(R.color.white);
			MyAsync tasl=new MyAsync();
			tasl.execute(myMenu.getImgId(),position);
		}
		vh.img.setImageBitmap(myMenu.getBitmap().get());
		vh.text.setText(myMenu.getText());
		return convertView;
		
	}
	private class ViewHelper{
		private ImageView img;
		private TextView text;
	}
	private class MyAsync extends AsyncTask<Integer, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int img=params[0];
			int position=params[1];
			Bitmap bitmap=BitmapFactory.decodeResource(MyListAdapter.this.context.getResources(), img);
			data.get(position).setBitmap(new SoftReference<Bitmap>(bitmap));
			Log.i("INFO", bitmap+"-----------------");
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i("INFO", "---66666666--------------");
			MyListAdapter.this.notifyDataSetChanged();
		}
		
	}
}
