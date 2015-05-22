package com.tz.died;



import com.tz.died.runable.MyThread;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,Callback{

	private String Tag = "MainActivity";
	private ImageView mNormal, mHell, mNightmare, mPurgatary;
	private int gameType;
    private int w,h;
	private SurfaceView mSurfaceView;
	private MyThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		strateMenuView();

	}

	private void strateMenuView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		initView();
		setListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mNormal = (ImageView) findViewById(R.id.normal);
		mHell = (ImageView) findViewById(R.id.hell);
		mNightmare = (ImageView) findViewById(R.id.nightmare);
		mPurgatary = (ImageView) findViewById(R.id.purgatary);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		mNormal.setOnClickListener(this);
		mHell.setOnClickListener(this);
		mNightmare.setOnClickListener(this);
		mPurgatary.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.normal:// 普通
			gameType = 2;
			break;
		case R.id.hell:// 地狱
			gameType = 3;
			break;
		case R.id.nightmare:// 噩梦
			gameType = 4;
			break;
		case R.id.purgatary:// 炼狱
			gameType = 5;
			break;

		default:
			Log.i(Tag, "误操作");
			break;

		}
		// 根据游戏难度玩游戏
		startGameView();
	}

	private void startGameView() {
	   mSurfaceView = new SurfaceView(this);
       mSurfaceView.getHolder().addCallback(this);
       setContentView(mSurfaceView);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		w = mSurfaceView.getWidth();//画板宽
		h = mSurfaceView.getHeight();//画板高
		thread = new MyThread(this, holder, w, h, gameType);
		thread.start();//开始画画
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		//画板改变
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//画板销毁
		thread.setStart(false);
	}



}
