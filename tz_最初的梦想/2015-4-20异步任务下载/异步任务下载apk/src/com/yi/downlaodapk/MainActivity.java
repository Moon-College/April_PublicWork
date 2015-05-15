package com.yi.downlaodapk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
/**
 * 异步任务下载apk
 * @author yk
 *
 */
public class MainActivity extends Activity {
	/**下载按钮*/
	private Button btn_download;
	/**进度条*/
	private ProgressBar progressBar;
	/**图片路径*/
	private final String IMGPATH = "http://gdown.baidu.com/data/wisegame/94930b559e0af6d7/1haodian_85.apk";
	/**sdcard目录*/
	private final String SDCARDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	/**文件大小*/
	private int totalSize=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		initViews();
	}

	private void findViews() {
		btn_download = (Button) findViewById(R.id.btn_download);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	private void initViews() {
		btn_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyAsyncTask task = new MyAsyncTask();
				task.execute(IMGPATH);
			}
		});
	}

	public class MyAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground (String... params) {
			HttpURLConnection conn = null;
			try {
				URL url = new URL(params[0]);
				conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				if(conn.getResponseCode()==200){
					totalSize=conn.getContentLength();
					progressBar.setMax(totalSize);
					InputStream in=new BufferedInputStream(conn.getInputStream());
					String path=readStream(in, params);
					return path;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				conn.disconnect();
			}
			return null;
		}

		private String readStream(InputStream in, String... params) throws FileNotFoundException, IOException {
			String fileName=params[0].substring(params[0].lastIndexOf("/")+1);
			String path=SDCARDPATH+"/"+fileName;
			//通过路径构建输出流
			FileOutputStream out=new FileOutputStream(path);
			int len=0;
			byte[] buffer=new byte[1024];
			while((len=in.read(buffer))!=-1){//in读到buffer中的数据不一的就是满的
				out.write(buffer,0,len);
				publishProgress(len);
			}
			out.flush();
			out.close();
			in.close();
			return path;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result!=null){
				btn_download.setText("下载完成");
				Intent intent=new Intent();
				intent.setDataAndType(Uri.parse("file://"+result),"application/vnd.android.package-archive");
				startActivity(intent);
			}else{
				btn_download.setText("下载失败");
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			//progressBar.setProgress(progressBar.getProgress()+values[0]);
			progressBar.incrementProgressBy(values[0]);
			Log.i("progress",(int)(progressBar.getProgress()*1.0/totalSize*100)+"%");
			btn_download.setText((int)(progressBar.getProgress()*1.0/totalSize*100)+"%");
		}

	}
}
