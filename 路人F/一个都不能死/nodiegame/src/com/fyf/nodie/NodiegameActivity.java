package com.fyf.nodie;

import com.fyf.nodie.runnable.MyThread;

import android.R.integer;
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

public class NodiegameActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
    private ImageView commonDifficulty,nigthmareDifficulty,infernalDifficulty,purgatoryDifficulty;
    private int difficultMode;                      //游戏难度
    private SurfaceView gameSurfaceView;   
    private int w,h;
    private MyThread thread;  //绘制的线程
    private int gameSpan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMenuView();
    }
    /**
     * 初始化菜单界面
     */
	private void startMenuView() {
		setContentView(R.layout.main);
		commonDifficulty = (ImageView) findViewById(R.id.commondifficulty);
		nigthmareDifficulty = (ImageView) findViewById(R.id.nigthmaredifficulty);
		infernalDifficulty = (ImageView) findViewById(R.id.infernaldifficulty);
		purgatoryDifficulty = (ImageView) findViewById(R.id.purgatorydifficulty);
		commonDifficulty.setOnClickListener(this);
		nigthmareDifficulty.setOnClickListener(this);
		infernalDifficulty.setOnClickListener(this);
		purgatoryDifficulty.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commondifficulty:
			difficultMode = 2;
			break;
		case R.id.nigthmaredifficulty:
			difficultMode = 3;
			break;
		case R.id.infernaldifficulty:
			difficultMode = 4;
			break;
		case R.id.purgatorydifficulty:
			difficultMode = 5;
			break;
		default:
			break;
		}
		//根据难度开启游戏视图
		startGameView();
	}
	/**
	 * 开启游戏界面
	 */
	private void startGameView() {
		gameSurfaceView = new SurfaceView(this);
		gameSurfaceView.getHolder().addCallback(this);
		gameSurfaceView.setOnTouchListener(this);		
		setContentView(gameSurfaceView);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		w = gameSurfaceView.getWidth();
		h = gameSurfaceView.getHeight();
		gameSpan = h*4/(5*difficultMode);
		// 画板创建完毕，可以开始绘画
		thread = new MyThread(this, holder, w, h, difficultMode);
		thread.start();
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		 
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		//画板销毁 
		thread.setStart(false);
	}
	/**
	 * 点击画板的Touch事件
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (thread.getGameStatu()) {
		case MyThread.RUNNING:
			//游戏正在运行，找到对应区域的role并跳起来
			confirRole(event);
			break;
		case MyThread.GAMEOVER:
			//返回或者重来
			backOrReStart(event);
			break;
		default:
			break;
		}
		return true;
	}
	/**
	 * 判断返回或者重来
	 * @param event
	 */
	private void backOrReStart(MotionEvent event) {
		float y = event.getY();
		if(y >= h/2 && y <= h*3/4){
			startMenuView();
		}else if(y >= h*3/4)
			thread.setGameStatu(MyThread.RUNNING);
			thread.initSpirit();
	}
	/**
	 * 确定被点击事件
	 * @param event
	 */
	private void confirRole(MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			//第一根手指暗下来
			float y1 = event.getY();
			roleJump(y1);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			//其他手指按下来
			float y2 = event.getY(event.getPointerCount()-1);
			roleJump(y2);
			break;
		default:
			break;
		}
	}
	/**
	 * 根据点击区域获取对应区域的人物，然后跳起来
	 * @param y
	 */
	private void roleJump(float y) {
		for(int i = 0;i < difficultMode; i++){
			//遍历所有游戏区域，并判断点击的区域
			int lineU = h/10 + (i)*gameSpan;
			int lineD = h/10 + (i+1)*gameSpan;
			if(y >= lineU && y < lineD &&!thread.roles[i].isJump()){
				thread.roles[i].setSpeedY(-h/55);//向上的速度
				thread.roles[i].setJump(true);
			}
		}		
	}
}