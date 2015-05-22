package com.yk.nodie;

import com.yk.nodie.runnable.MyThread;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 
 * @author yk
 * 
 */
public class MainActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
	/** ��Ϸ�ȼ� */
	private int gameLevel;
	/** �Ѷ�ѡ�� */
	private ImageView img_normal, img_nightmare, img_hell, img_pur;
	private MyThread myThread;
	private SurfaceView sv;
	private int w;
	private int h;
	private int gameSpan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ������Ϸ�˵���ͼ
		gameMenuView();

	}

	/**
	 * ��Ϸ�˵���ͼ
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
	 * ��ʼ��Ϸ��ͼ
	 */
	private void startGameView() {
		sv = new SurfaceView(this);
		// ע��ص�����
		sv.getHolder().addCallback(this);
		sv.setOnTouchListener(this);
		// ��������ص�activity
		setContentView(sv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal:
			gameLevel = 2;
			break;

		case R.id.nightmare:
			gameLevel = 3;
			break;

		case R.id.hell:
			gameLevel = 4;
			break;

		case R.id.pur:
			gameLevel = 5;
			break;
		default:
			break;
		}
		// ������Ϸ�ȼ���ʼ��Ϸ
		startGameView();
	}

	/**
	 * �����Ѿ�������ɣ���ʼ����
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		w = sv.getWidth();
		h = sv.getHeight();
		gameSpan = h * 4 / (5 * gameLevel);
		// ����һ���߳������ƶ���
		myThread = new MyThread(this, holder, w, h, gameLevel);
		myThread.start();
	}

	/** ����ı� */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/** �������� */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myThread.setIsStart(false);
	}

	/**
	 * ����������ָ���������������
	 * 
	 * @param y
	 */
	private void roleJump(float y) {
		for (int i = 0; i < gameLevel; i++) {
			float line_u = h / 10 + i * gameSpan;
			float line_d = h / 10 + (i + 1) * gameSpan;
			if (y >= line_u && y < line_d && !myThread.roles[i].getIsJump()) {
				// ��ָ�����������������
				myThread.roles[i].setSpeedY(-h / 55);
				myThread.roles[i].setIsJump(true);
			}
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (myThread.getGameStatus()) {
		case MyThread.RUNNING://��Ϸ��������
			controlRole(event);
			break;
		case MyThread.GAMEOVER://��Ϸ����
			backOrRestart(event);
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * ���ػ�������
	 * @param event
	 */
	private void backOrRestart(MotionEvent event) {
		float y=event.getY();
		if(y>h/2&&y<=3*h/4){//(4/6+5/6)/2=3/4
			//ִ�з��ز���
			gameMenuView();
		}else if(y>3*h/4){
			//ִ����������
			restart();
		}
		
	}
	/**
	 * 
	 */
	private void restart() {
		//��ʼ������
		myThread.initSpirit();
		//��ʼ������Ϸ
		myThread.setGameStatus(MyThread.RUNNING);
	}

	/**
	 * �������ﶯ��
	 * @param event
	 */
	private void controlRole(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:// ��һ����ָ����
			float y = event.getY();
			roleJump(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// �ǵ�һ����ָ����
			int index = event.getPointerCount() - 1;
			y = event.getY(index);
			roleJump(y);
			break;
		default:
			break;
		}
	}
}
