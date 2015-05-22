package com.fyf.nodie;

import com.fyf.nodie.runnable.MyThread;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NodiegameActivity extends Activity implements OnClickListener, Callback {
    private ImageView commonDifficulty,nigthmareDifficulty,infernalDifficulty,purgatoryDifficulty;
    private int difficultMode;                      //游戏难度
    private SurfaceView gameSurfaceView;   
    private int w,h;
    private MyThread thread;  //绘制的线程
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
		setContentView(gameSurfaceView);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		w = gameSurfaceView.getWidth();
		h = gameSurfaceView.getHeight();
		// 画板创建完毕，可以开始绘画
		thread = new MyThread(this, holder, w, h, difficultMode);
		thread.start();
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// 
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		//画板销毁 
		thread.setStart(false);
	}
}