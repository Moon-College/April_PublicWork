package com.tz.died;

import com.tz.died.runable.MyThread;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		Callback, OnTouchListener {

	private String Tag = "MainActivity";
	private ImageView mNormal, mHell, mNightmare, mPurgatary;
	private int gameType;
	private int w, h;
	private SurfaceView mSurfaceView;
	private MyThread thread;
	private int gameSpan;

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
			gameType = 4;
			break;
		case R.id.nightmare:// 噩梦
			gameType = 3;
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
		mSurfaceView.setOnTouchListener(this);
		setContentView(mSurfaceView);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		w = mSurfaceView.getWidth();// 画板宽
		h = mSurfaceView.getHeight();// 画板高
		gameSpan = h * 4 / (5 * gameType);
		thread = new MyThread(this, holder, w, h, gameType);
		thread.start();// 开始画画
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		// 画板改变

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// 画板销毁
		thread.setStart(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (thread.getGameStatu()) {
		case MyThread.RUNNING:
			runTouch(event);
			break;
		case MyThread.GAMEOVER:
			gameoverTouch(event);
			break;

		default:
			break;
		}

		return true;
	}

	private void runTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			// 得到按下的y坐标
			float y = event.getY();
			jumpRole(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			// 其他手指陆续按下来
			float y2 = event.getY(event.getPointerCount() - 1);
			jumpRole(y2);
			break;

		default:
			break;
		}
	}

	private void gameoverTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		float y = event.getY();
		if (y >= h / 2 && y <= 3 * h / 4) {
			// 返回
			strateMenuView();
		} else if (y > 3 * h / 4) {
			// 重来
			restart();
		}
	}
	private void restart() {
		thread.setGameStatu(MyThread.RUNNING);//设置游戏回到正常运行状态
		thread.initSpirit();//初始化精灵
	}

	/**
	 * 根据y坐标判断那个区域跳起来
	 * 
	 */
	private void jumpRole(float y) {
		// TODO Auto-generated method stub
		for (int i = 0; i < gameType; i++) {
			// 遍历所有的游戏区域，判断y坐标在哪个游戏区域
			int downD = h / 10 + (i + 1) * gameSpan;
			int downU = h / 10 + i * gameSpan;
			if (y >= downU && y <= downD&&!thread.role[i].isJump()) {
				thread.role[i].setSpeedY(-h / 55);
				thread.role[i].setJump(true);
                
			}
		}
	}
}
