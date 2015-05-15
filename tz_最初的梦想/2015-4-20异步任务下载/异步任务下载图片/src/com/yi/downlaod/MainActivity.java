package com.yi.downlaod;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * 异步任务下载图片
 * @author yk
 *
 */
public class MainActivity extends Activity {
	/**下载按钮*/
	private Button btn_download;
	/**进度条*/
	private ProgressBar progressBar;
	private ImageView img;
	/**图片路径*/
	private final String IMGPATH = "http://e.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494eea4e904102ff5e0fe98257eeb.jpg";
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
		img = (ImageView) findViewById(R.id.img);
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

	public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground (String... params) {
			Bitmap bitmap=null;
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
					bitmap=readStream(in, params);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				conn.disconnect();
			}
			
			return bitmap;
		}

		private Bitmap readStream(InputStream in, String... params) throws FileNotFoundException, IOException {
			String fileName=params[0].substring(params[0].lastIndexOf("/")+1);
			//File file=new File(SDCARDPATH,fileName);
			//通过文件构建输出流
			//FileOutputStream out=new FileOutputStream(file);
			//通过路径构建输出流
			FileOutputStream out=new FileOutputStream(SDCARDPATH+"/"+fileName);
			int len=0;
			byte[] buffer=new byte[1024];
			while((len=in.read(buffer))!=-1){//in读到buffer中的数据不一的就是满的
				out.write(buffer,0,len);
				publishProgress(len);
			}
			out.flush();
			out.close();
			in.close();
			Bitmap bitmap=BitmapFactory.decodeFile(SDCARDPATH+"/"+fileName);
			return bitmap;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			btn_download.setText("下载完成");
			img.setImageBitmap(result);
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
