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
    private int difficultMode;                      //��Ϸ�Ѷ�
    private SurfaceView gameSurfaceView;   
    private int w,h;
    private MyThread thread;  //���Ƶ��߳�
    private int gameSpan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMenuView();
    }
    /**
     * ��ʼ���˵�����
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
		//�����Ѷȿ�����Ϸ��ͼ
		startGameView();
	}
	/**
	 * ������Ϸ����
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
		// ���崴����ϣ����Կ�ʼ�滭
		thread = new MyThread(this, holder, w, h, difficultMode);
		thread.start();
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		 
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		//�������� 
		thread.setStart(false);
	}
	/**
	 * ��������Touch�¼�
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (thread.getGameStatu()) {
		case MyThread.RUNNING:
			//��Ϸ�������У��ҵ���Ӧ�����role��������
			confirRole(event);
			break;
		case MyThread.GAMEOVER:
			//���ػ�������
			backOrReStart(event);
			break;
		default:
			break;
		}
		return true;
	}
	/**
	 * �жϷ��ػ�������
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
	 * ȷ��������¼�
	 * @param event
	 */
	private void confirRole(MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			//��һ����ָ������
			float y1 = event.getY();
			roleJump(y1);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			//������ָ������
			float y2 = event.getY(event.getPointerCount()-1);
			roleJump(y2);
			break;
		default:
			break;
		}
	}
	/**
	 * ���ݵ�������ȡ��Ӧ��������Ȼ��������
	 * @param y
	 */
	private void roleJump(float y) {
		for(int i = 0;i < difficultMode; i++){
			//����������Ϸ���򣬲��жϵ��������
			int lineU = h/10 + (i)*gameSpan;
			int lineD = h/10 + (i+1)*gameSpan;
			if(y >= lineU && y < lineD &&!thread.roles[i].isJump()){
				thread.roles[i].setSpeedY(-h/55);//���ϵ��ٶ�
				thread.roles[i].setJump(true);
			}
		}		
	}
}