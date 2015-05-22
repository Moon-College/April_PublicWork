package com.mxz.ayncs;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Ayns_ProActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private Button start;
	private ProgressBar pro;
	private TextView text;
	private ImageView img;
	private final String IMG_PATH="http://a.hiphotos.baidu.com/image/pic/item/2cf5e0fe9925bc319c57e3325ddf8db1cb137016.jpg";
	private final String APK_PATH="http://qr.liantu.com/api.php?m=0&w=100&text=http://xl1.dl.520apk.com:1110/apk/370/魔王听书.apk";
	private final String SD_PATH=Environment.getExternalStorageDirectory().getAbsolutePath().toString();
	private int contentLength;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        AlertDialog.Builder pd=new AlertDialog.Builder(this);
        pd.setTitle("提示");
        pd.setMessage("检测到新版本，是否下载");
        pd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				APKAsync async=new APKAsync();
				async.execute(APK_PATH);
			}
		});
        pd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
        pd.show();
    }
	private void initView() {
		// TODO Auto-generated method stub
		this.start=(Button) findViewById(R.id.start);
		this.pro=(ProgressBar) findViewById(R.id.pro);
		this.text=(TextView) findViewById(R.id.text);
		this.img=(ImageView)findViewById(R.id.img);
		this.start.setOnClickListener(this);
		
	}
	public void onClick(View v) {
		MyAsync asyn=new MyAsync();
		asyn.execute(IMG_PATH);
	}
	private class MyAsync extends AsyncTask<String, Integer, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap=null;
			try {
				bitmap=loadImg(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		public Bitmap loadImg(String path) throws Exception {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			String name=path.substring(path.lastIndexOf("/"));
			if(conn.getResponseCode()==200){
				contentLength=conn.getContentLength();
				Ayns_ProActivity.this.pro.setMax(contentLength);
				InputStream input=conn.getInputStream();
				FileOutputStream output=new FileOutputStream(SD_PATH+name);
				int len=0;
				byte[] buffer=new byte[1024];
				while((len=input.read(buffer))!=-1){
					this.publishProgress(len);
					output.write(buffer, 0, len);
				}
				input.close();
				output.close();
				Bitmap bitmap=BitmapFactory.decodeFile(SD_PATH+name);
				if(bitmap!=null){
					return bitmap;
				}
			}
			return null;
			
		}
		//执行之前的方法，预加载,可以做一些初始化
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		//加载完毕，可刷新UI，属于主线程执行的
		@Override
		protected void onPostExecute(Bitmap result) {
			Log.i("INFO", "下载完毕："+result);
			Ayns_ProActivity.this.img.setImageBitmap(result);
			Ayns_ProActivity.this.text.setText("下载完毕");
			super.onPostExecute(result);
		}
		//用来更新进度条，主线程
		@Override
		protected void onProgressUpdate(Integer... values) {
			Ayns_ProActivity.this.pro.setProgress(pro.getProgress()+values[0]);
			Ayns_ProActivity.this.text.setText(100*Ayns_ProActivity.this.pro.getProgress()/Ayns_ProActivity.this.pro.getMax()+"%");
			super.onProgressUpdate(values);
		}
		
	}
	
	private class APKAsync extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			String result=null;
			try {
				result=loadAPK(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public String loadAPK(String path) throws Exception{
			String result=null;
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			String name=path.substring(path.lastIndexOf("/"));
			if(conn.getResponseCode()==200){
				InputStream input=conn.getInputStream();
				FileOutputStream output=new FileOutputStream(SD_PATH+name);
				int len=0;
				byte buffer[]=new byte[528];
				while((len=input.read(buffer))!=-1){
					output.write(buffer, 0, len);
				}
				result=SD_PATH+name;
			}
			return result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Ayns_ProActivity.this, "等待开发", 3000).show();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		
	}
	
	
	
}