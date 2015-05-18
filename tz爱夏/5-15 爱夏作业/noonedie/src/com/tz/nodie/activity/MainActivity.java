package com.tz.nodie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tz.nodie.R;
import com.tz.nodie.thread.MyThread;

public class MainActivity extends Activity implements OnClickListener, Callback {
	private ImageView common, nightmare, hell, purgatory;
	private int index;
	private SurfaceView surfaceView;
	private MyThread myThread;
	private int imageType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMenuView();
	}

	/**
	 * @author jackie
	 */
	private void initMenuView() {
		setContentView(R.layout.main);
		common = (ImageView) findViewById(R.id.common);
		nightmare = (ImageView) findViewById(R.id.nightmare);
		hell = (ImageView) findViewById(R.id.hell);
		purgatory = (ImageView) findViewById(R.id.purgatory);
		common.setOnClickListener(this);
		nightmare.setOnClickListener(this);
		hell.setOnClickListener(this);
		purgatory.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common:
			imageType = 2;
			break;
		case R.id.nightmare:
			imageType = 3;
			break;
		case R.id.hell:
			imageType = 4;
			break;
		case R.id.purgatory:
			imageType = 5;
			break;
		default:
			break;
		}
		initGameView();
	}

	/**
	 * init game view
	 * 
	 * @author jackie
	 * @param index2
	 */
	private void initGameView() {
		surfaceView = new SurfaceView(this);
		surfaceView.getHolder().addCallback(this);
		setContentView(surfaceView);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 画板准备好，开始画图的线程
		myThread = new MyThread(holder, this, surfaceView.getWidth(),
				surfaceView.getHeight(), imageType);
		myThread.start();
	}

	public void surfaceCreated(SurfaceHolder holder) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// 画板销毁，线程需要停止
		myThread.setStart(false);
	}
}