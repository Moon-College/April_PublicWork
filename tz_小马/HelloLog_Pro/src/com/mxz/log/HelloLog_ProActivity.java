package com.mxz.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HelloLog_ProActivity extends Activity {
	private Button start;
	private Button send;
	private EditText message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.start=(Button) findViewById(R.id.start);
        this.send=(Button) findViewById(R.id.send);
        this.message=(EditText) findViewById(R.id.message);
    }
    public void start(View v){
    	try {
			printLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void printLog() throws IOException {
    	Log.i("info", "----------中文中文----------");
    	StringBuffer sbf=new StringBuffer();
    	ArrayList<String> cmdLine=new ArrayList<String>();
    	cmdLine.add("logcat");
    	cmdLine.add("-d");
    	cmdLine.add("-s");
    	cmdLine.add("info");
    	Process exec = Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));
		InputStream inputStream = exec.getInputStream();
		InputStreamReader isReader = new InputStreamReader(inputStream);//装饰器模式
		BufferedReader reader = new BufferedReader(isReader);//缓存reader
		String str = null;
		while((str = reader.readLine())!=null){
    		
    		sbf.append(str);
    		sbf.append("\n");
    	}
    	Log.i("info", sbf.toString());
    	Toast.makeText(this, sbf.toString(), 1000).show();
    }
    public void sendSMS(View v){
    	String content=this.message.getText().toString();
    	Log.i("info", content);
    	Intent it=new Intent();
    	it.setAction(Intent.ACTION_SENDTO);
    	it.setData(Uri.parse("smsto:"+110));
    	it.putExtra("sms_body", content);
    	this.startActivity(it);
    }
}











