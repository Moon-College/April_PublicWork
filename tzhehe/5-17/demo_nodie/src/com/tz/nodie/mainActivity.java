package com.tz.nodie;

import com.tz.nodie.thread.MyThread;
import com.tz.nodie.util.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class mainActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
	private ImageView normal, nightmare, hell, purr;
	private SurfaceView mSurfaceView;
	private int gameType;
	private MyThread thread;
	private int w;
	private int h;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTitleView();
	}

	/**
	 * 创建游戏导航的视图
	 */
	public void initTitleView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.main);
		normal = (ImageView) findViewById(R.id.normal);
		normal.setOnClickListener(this);
		nightmare = (ImageView) findViewById(R.id.nightmare);
		nightmare.setOnClickListener(this);
		hell = (ImageView) findViewById(R.id.hell);
		hell.setOnClickListener(this);
		purr = (ImageView) findViewById(R.id.purr);
		purr.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal:
			gameType=2;
			break;
		case R.id.nightmare:
			gameType=3;
			break;
		case R.id.hell:
			gameType=4;
			break;
		case R.id.purr:
			gameType=5;
			break;
		default:
			break;
		}
		startGame();

	}
	
	/**
	 * 开始创建游戏视图
	 */
	private void startGame() {
		//开启画板
		mSurfaceView=new SurfaceView(this);
		//绑定画板
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.setOnTouchListener(this);
		//将画板绑定在Activity页面
		setContentView(mSurfaceView);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		w = mSurfaceView.getWidth();
		h = mSurfaceView.getHeight();
		thread = new MyThread(w,h, this, holder,gameType);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread.setStart(false);
	}
	
	
	/**
	 * 点击画板的touch事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (thread.getGameStatu()) {
		case Constant.RUNNING:
			//游戏正在运行，找到对应区域的role跳起来
			confirmRole(event);
			break;
		case Constant.GAMEOVER:
			//返回或者重来
			backOrStart(event);
			break;
		default:
			break;
		}
		return true;
	}
	
	

	private void backOrStart(MotionEvent event) {
		float y = event.getY();
		if(y>=h/2&&y<=3*h/4){
			//返回
			initTitleView();
		}else if(y>3*h/4){
			//重来
			restart();
		}
		
	}

	private void restart() {
		//重新开始游戏
		thread.setGameStatu(Constant.RUNNING);
		//初始化精灵
		thread.initSpirit();
	}

	private void confirmRole(MotionEvent event) {
		int action = event.getAction();
		switch (action&MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			//第一根手指按下来
			float y = event.getY();
			roleJump(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			//其他手指陆续按下来
			float y2 = event.getY(event.getPointerCount()-1);
			roleJump(y2);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 人物挑起来了
	 * @param y
	 */
	private void roleJump(float y) {
		for (int i = 0; i < gameType; i++) {
			int lineD = h/10 + (i+1)*thread.getGameSpan();
			int lineU = h/10 + (i)*thread.getGameSpan();
			
			if(y>=lineU&&y<lineD&&!thread.roles[i].isJump()){
				//这个区域被点到了，第i个区域
				thread.roles[i].setSpeedY(-h/55);//向上的速度
				thread.roles[i].setJump(true);//人跳起来了
			}
		}
	}
	
	

}