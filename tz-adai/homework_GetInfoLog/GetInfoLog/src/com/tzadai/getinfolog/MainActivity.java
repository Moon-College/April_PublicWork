package com.tzadai.getinfolog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button btnGetInfoLog;
	private TextView tvSummary;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		Log.i("INFO", "被捕获的日志！");
	}

	private void initView() {
		btnGetInfoLog = (Button) findViewById(R.id.btn_get_info_log);
		btnGetInfoLog.setOnClickListener(this);
		tvSummary = (TextView) findViewById(R.id.tv_summary);
	}

	@Override
	public void onClick(View v) {
		try {
			readLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readLog() throws IOException {
		List<String> cmdComms = new ArrayList<String>();
		cmdComms.add("logcat");
		cmdComms.add("-d");
		cmdComms.add("-s");
		cmdComms.add("INFO");
		Process exec = Runtime.getRuntime().exec(cmdComms.toArray(new String[cmdComms.size()]));
		InputStream is = exec.getInputStream();
		InputStreamReader isreader = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isreader);
		String str = null;
		StringBuffer strBuffer = new StringBuffer();
		while((str = reader.readLine())!=null){
			strBuffer.append(str);
			strBuffer.append("\t\n");
		};
		Toast.makeText(this, strBuffer.toString(), Toast.LENGTH_SHORT).show();
		String strTemp = "作业--获取日志信息 \t\n要点:\t\n 1.该作业使用命令'logcat -d -s INFO',要在android系统内执行该命令，需要JAVA运行时环境，如下:\t\n Runtime.getRuntime().exec(命令集) \t\n \t\n 2.读取日志信息还需要添加相关权限，如下：\t\n 'android.permission.READ_LOGS'";
		tvSummary.setText(strTemp);
	}
}
