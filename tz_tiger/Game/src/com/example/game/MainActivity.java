package com.example.game;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener, Callback {
	ImageView normal, nightmare, hell, pur;
	private int gameType;
	private SurfaceView mSurfaceView;
	private MyThread thread;
	private int w, h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		normal = (ImageView) findViewById(R.id.normal);
		nightmare = (ImageView) findViewById(R.id.nightmare);
		hell = (ImageView) findViewById(R.id.hell);
		pur = (ImageView) findViewById(R.id.pur);
		normal.setOnClickListener(this);
		nightmare.setOnClickListener(this);
		hell.setOnClickListener(this);
		pur.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal:
			// ÆÕÍ¨Ä£Ê½
			gameType = 2;
			break;
		case R.id.nightmare:
			gameType = 3;
			break;
		case R.id.hell:
			gameType = 4;
			break;
		case R.id.pur:
			gameType = 5;
			break;
		default:
			break;
		}
		startGameView();
	}

	private void startGameView() {
		
		mSurfaceView = new SurfaceView(MainActivity.this);
		mSurfaceView.getHolder().addCallback(this);
		setContentView(mSurfaceView);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		w=mSurfaceView.getWidth();
		h=mSurfaceView.getHeight();
		MyThread myThread=new MyThread(this, holder, w, h, gameType);
				myThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setStart(false);
	}

}