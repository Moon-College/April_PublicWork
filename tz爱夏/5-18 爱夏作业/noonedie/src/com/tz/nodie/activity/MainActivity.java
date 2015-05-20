package com.tz.nodie.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.tz.nodie.R;
import com.tz.nodie.thread.MyThread;

public class MainActivity extends Activity implements OnClickListener,
		Callback, OnTouchListener {
	private ImageView common, nightmare, hell, purgatory;
	private int index;
	private SurfaceView surfaceView;
	private MyThread myThread;
	private int gameType;
	private int w;
	private int h;
	private int lineSpan;

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
			gameType = 2;
			break;
		case R.id.nightmare:
			gameType = 3;
			break;
		case R.id.hell:
			gameType = 4;
			break;
		case R.id.purgatory:
			gameType = 5;
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
		surfaceView.setOnTouchListener(this);
		setContentView(surfaceView);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 画板的宽高
		w = surfaceView.getWidth();
		h = surfaceView.getHeight();
		lineSpan = (h * 4 / 5) / gameType;
		// 画板准备好，开始画图的线程
		myThread = new MyThread(holder, this, w, h, gameType);
		myThread.setStatus(myThread.RUNNING);
		myThread.start();
	}

	public void surfaceCreated(SurfaceHolder holder) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// 画板销毁，线程需要停止
		myThread.setStart(false);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (myThread.getStatus()) {
		case MyThread.RUNNING:
			// 游戏正在运行
			touchRole(event);
			break;
		case MyThread.GAMEOVER:
			// 返回或重来
			backOrRestart(event);
			break;

		default:
			break;
		}
		return true;
	}

	private void backOrRestart(MotionEvent event) {
		    float y=event.getY();
		    if(y>h/2&&y<=3*h/4){
		    	//点击了返回区域
		    	initMenuView();
		    }else if(y>3*h/4){
		    	//点击重来区域
		    	myThread.setStatus(MyThread.RUNNING);
		    	myThread.initRole();
		    }
	}

	/**
	 * 确定手指点击的区域
	 * 
	 * @param event
	 */
	private void touchRole(MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			// 第一根手指按下
			float downY = event.getY();
			jump(downY);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			// 多根手指按下
			float downYM = event.getY(event.getPointerCount()-1);
			jump(downYM);
			break;

		default:
			break;
		}
	}

	/**
	 * 根据点击的y坐标往上跳
	 * 
	 * @param downYM
	 */
	private void jump(float y) {
		// 遍历所有的游戏区域，判断点击的区域
		for (int i = 0; i < gameType; i++) {
			// 计算出游戏区域的上下界限
			float gameUp = h / 10 + i * lineSpan;
			float gameDown = h / 10 + (i + 1) * lineSpan;
			if (y >= gameUp && y < gameDown && !myThread.roles[i].isJump()) {
				// 点击到当前i的游戏区域，让role跳起来
				myThread.roles[i].setSpeedY(-h / 50);// 设置向上的速度
				myThread.roles[i].setJump(true);
			}
		}
	}
}