package com.ckview.nodie.activity;

import com.ckview.nodie.R;
import com.ckview.nodie.thread.DrawGameView;
import com.ckview.utils.InitializeActivity;
import com.ckview.utils.StaticValue;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
	
	private ImageView im_normal, im_nightmare, im_hell, im_purgatory;
	private SurfaceView surfaceView;
	private int gameType;
	private int width;
	private int height;
	private DrawGameView drawGameView;
	private long lastDown;
	private int hackTouchCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		startMenuView();
	}

	private void startMenuView() {
		gameType = 0;
		setContentView(R.layout.main_activity);
		InitializeActivity.initView(this);
		im_normal.setOnClickListener(this);
		im_nightmare.setOnClickListener(this);
		im_hell.setOnClickListener(this);
		im_purgatory.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case  KeyEvent.KEYCODE_BACK:
			if(drawGameView != null && drawGameView.getIsStart()){
				drawGameView.setIsStart(false);
				startMenuView();
				return false;
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.im_normal:
				gameType = StaticValue.NORMAL;
				break;
			case R.id.im_nightmare:
				gameType = StaticValue.NIGHTMARE;
				break;
			case R.id.im_hell:
				gameType = StaticValue.HELL;
				break;
			case R.id.im_purgatory:
				gameType = StaticValue.PURGATORY;
				break;
			default:
				break;
		}
		startGame();
	}

	private void startGame() {
		if(gameType != StaticValue.UNSELECTED){
			surfaceView = new SurfaceView(this);
			surfaceView.getHolder().addCallback(this);
			setContentView(surfaceView);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		width = surfaceView.getWidth();
		height = surfaceView.getHeight();
		surfaceView.setOnTouchListener(this);
		drawGameView = new DrawGameView(this, gameType, holder, width, height);
		drawGameView.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawGameView.setIsStart(false);
//		drawGameView.stop();
//		drawGameView.destroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (drawGameView.getGameMode()) {
			case StaticValue.RUNNING:
				// find area by pointer and make runner jump
				findRunnerAndJump(event);
				// open hack mode
				openHack(event);
				break;
				
			case StaticValue.OVER:
				// game over menu
				int action;
				action = drawGameView.drawGameView(event.getY());
				switch (action) {
					case StaticValue.RESTART:
						drawGameView.initDrawGame();
						drawGameView.setGameMode(StaticValue.RUNNING);
						break;
					case StaticValue.BACK:
						drawGameView.setIsStart(false);
						startMenuView();
						break;
	
					default:
						break;
					}
				break;
		}
		return true;
	}

	/**
	 * check if open hack
	 * @param event
	 */
	private void openHack(MotionEvent event) {
		if(drawGameView.isInHackArea(event.getY())){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(System.currentTimeMillis() - lastDown < 500){
					hackTouchCount++;
				}else{
					hackTouchCount = 0;
				}
				break;
			case MotionEvent.ACTION_UP:
				lastDown = System.currentTimeMillis();
				if(hackTouchCount > 3){
					if(drawGameView.isHack()){
						drawGameView.setHack(false);
						drawGameView.setHackText(StaticValue.NOHACK);
					}else{
						drawGameView.setHackText(StaticValue.HACKED);
						drawGameView.setHack(true);
					}
					hackTouchCount = 0;
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * confirm pointers touch
	 * @param event
	 */
	private void findRunnerAndJump(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				drawGameView.runnerJump(event.getY());
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN:
				drawGameView.runnerJump(event.getY(event.getPointerCount()-1));
				break;
	
			default:
				break;
		}
	}
}
