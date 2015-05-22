package com.mxz.nodie;

import com.mxz.nodie.runnable.MyThread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
    /** Called when the activity is first created. */
	private ImageView normal,nightmare,hell,pur;
	private int gameType;
	private SurfaceView mysurfaceView;
	private MyThread thread;
	private int gameSpan;
	private int w;
	private int h;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMainView();
    }
    /**
     * 开启菜单视图
     */
	private void startMainView() {
        super.setContentView(R.layout.main);
        //初始化控件
        this.normal=(ImageView) this.findViewById(R.id.normal);
        this.nightmare=(ImageView) this.findViewById(R.id.nightmare);
        this.hell=(ImageView) this.findViewById(R.id.hell);
        this.pur=(ImageView) this.findViewById(R.id.pur);
        //设置按钮点击事件
        this.normal.setOnClickListener(this);
        this.nightmare.setOnClickListener(this);
        this.hell.setOnClickListener(this);
        this.pur.setOnClickListener(this);
	}
	public void onClick(View v) {
		int action=v.getId();
		switch(action){
		case R.id.normal:
			gameType=2;
			break;
		case R.id.nightmare:
			gameType=3;
			break;
		case R.id.hell:
			gameType=4;
			break;
		case R.id.pur:
			gameType=5;
			break;
		default:
			break;
		}
		startGameView();
	}
	/**
	 * 开启游戏视图
	 */
	private void startGameView() {
		//实例化画板
		mysurfaceView=new SurfaceView(this);
		//画板监听
		mysurfaceView.getHolder().addCallback(this);
		//画板ontouch事件监听
		mysurfaceView.setOnTouchListener(this);
		//画板添加到上下文视图中
		super.setContentView(mysurfaceView);	
	}
	/**
	 * 画板创建成功函数
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		w = mysurfaceView.getWidth();
		h = mysurfaceView.getHeight();
		gameSpan = h*4/(5*gameType);
		//画板准备好了，启动线程绘画
		this.thread=new MyThread(this, holder, w, h, gameType);
		//线程开始
		this.thread.start();
	}
	/**
	 * 画板改变函数
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
	/**
	 * 画板销毁函数
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.thread.setStart(false);
	}
	/**
	 * 画板触摸事件
	 */
	public boolean onTouch(View v, MotionEvent event) {
		//取得游戏目前状态
		switch(thread.getGameStatu()){
		//运行状态
		case MyThread.RUNNING:
			confirmRole(event);
			break;
		//结束状态
		case MyThread.GAMEOVER:
			backOrStart(event);
			break;
			default:
				break;
		}
		return true;
	}
	/**
	 * 游戏
	 * @param event
	 */
	private void backOrStart(MotionEvent event) {
		float y=event.getY();
		if(y>=h/2&&y<=3*h/4){
			startMainView();
		}else if(y>3*h/4){
			restart();
		}
	}
	
	private void restart() {
		thread.setGameStatu(MyThread.RUNNING);
		thread.initSpirit();
		
	}
	/**
	 * 确定被点击区域
	 * @param event
	 */
	private void confirmRole(MotionEvent event) {
		int action = event.getAction();
		switch(action&MotionEvent.ACTION_MASK){
		//第一根手指按下来
		case MotionEvent.ACTION_DOWN:
			float y=event.getY();
			roleJump(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			float y2=event.getY(event.getPointerCount()-1);
			roleJump(y2);
			break;
			default:
				break;
		}
	}
	
	
	private void roleJump(float y) {
		for (int i = 0; i < this.gameType; i++) {
			int lineD=h/10+(i+1)*this.gameSpan;
			int lineU=h/10+(i)*this.gameSpan;
			if(y>=lineU&&y<lineD&&!thread.roles[i].isJump()){
				thread.roles[i].setSpeedY(-h/40);
				thread.roles[i].setJump(true);
				
			}
		}
	}
}