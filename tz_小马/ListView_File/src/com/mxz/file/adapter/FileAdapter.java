package com.mxz.file.adapter;

import java.lang.ref.SoftReference;
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
import android.widget.TextView;

import com.mxz.file.R;
import com.mxz.file.bean.MyFile;

public class FileAdapter extends BaseAdapter {
	private List<MyFile> data;
	private Context context;
	private LayoutInflater inflater;
	public FileAdapter(Context context,List<MyFile> data){
		this.context=context;
		this.data=data;
		this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return this.data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MyFile file=data.get(position);
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.file_layout, null);
			vh.img=(ImageView) convertView.findViewById(R.id.img);
			vh.file_name=(TextView) convertView.findViewById(R.id.file_name);
			convertView.setTag(vh);
			Log.i("INFO","´´½¨");
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(file.isPic()){
			if(file.getBitmap().get()==null){
				vh.img.setImageResource(R.color.white);
				MyTask tasl=new MyTask();
				tasl.execute(file.getFile_path(),String.valueOf(position));
			}else{
				vh.img.setImageBitmap(file.getBitmap().get());
			}
		}else{
			vh.img.setImageBitmap(file.getBitmap().get());
		}
		
		vh.file_name.setText(file.getFile_name());
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView file_name;
	}
	private class MyTask extends AsyncTask<String,Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			String path=params[0];
			int position=Integer.valueOf(params[1]);
			Bitmap bitmap=BitmapFactory.decodeFile(path);
			data.get(position).setBitmap(new SoftReference<Bitmap>(bitmap));
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			FileAdapter.this.notifyDataSetChanged();
		}
		
		
		
	}

}
