package com.yk.nodie;

import com.yk.nodie.runnable.MyThread;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
/**
 * 
 * @author yk
 *
 */
public class MainActivity extends Activity implements OnClickListener, Callback {
	/** 游戏等级 */
	private int gameLevel;
	/** 难度选中 */
	private ImageView img_normal, img_nightmare, img_hell, img_pur;
	private MyThread myThread;
	private SurfaceView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//加载游戏菜单视图
		gameMenuView();

	}

	/**
	 * 游戏菜单视图
	 */
	private void gameMenuView() {
		setContentView(R.layout.activity_main);
		findViews();
		initViews();
	}

	private void findViews() {
		img_normal = (ImageView) findViewById(R.id.normal);
		img_nightmare = (ImageView) findViewById(R.id.nightmare);
		img_hell = (ImageView) findViewById(R.id.hell);
		img_pur = (ImageView) findViewById(R.id.pur);
	}

	private void initViews() {
		img_normal.setOnClickListener(this);
		img_nightmare.setOnClickListener(this);
		img_hell.setOnClickListener(this);
		img_pur.setOnClickListener(this);
	}

	/**
	 * 开始游戏视图
	 */
	private void startGameView() {
		sv = new SurfaceView(this);
		//注册回调方法
		sv.getHolder().addCallback(this);
		//将画板加载到activity
		setContentView(sv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal:
			gameLevel=2;
			break;

		case R.id.nightmare:
			gameLevel=3;
			break;

		case R.id.hell:
			gameLevel=4;
			break;

		case R.id.pur:
			gameLevel=5;
			break;
		default:
			break;
		}
		//根据游戏等级开始游戏
		startGameView();
	}

	/**
	 * 画板已经加载完成，开始绘制
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		int w=sv.getWidth();
		int h=sv.getHeight();
		//开启一个线程来绘制动画
		myThread = new MyThread(this,holder,w,h,gameLevel);
		myThread.start();
	}
	/**画板改变*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
	/**画板销毁*/
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(myThread!=null){
			myThread.stop();
		}
	}
}
