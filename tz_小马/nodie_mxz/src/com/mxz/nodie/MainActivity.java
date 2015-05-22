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
     * �����˵���ͼ
     */
	private void startMainView() {
        super.setContentView(R.layout.main);
        //��ʼ���ؼ�
        this.normal=(ImageView) this.findViewById(R.id.normal);
        this.nightmare=(ImageView) this.findViewById(R.id.nightmare);
        this.hell=(ImageView) this.findViewById(R.id.hell);
        this.pur=(ImageView) this.findViewById(R.id.pur);
        //���ð�ť����¼�
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
	 * ������Ϸ��ͼ
	 */
	private void startGameView() {
		//ʵ��������
		mysurfaceView=new SurfaceView(this);
		//�������
		mysurfaceView.getHolder().addCallback(this);
		//����ontouch�¼�����
		mysurfaceView.setOnTouchListener(this);
		//������ӵ���������ͼ��
		super.setContentView(mysurfaceView);	
	}
	/**
	 * ���崴���ɹ�����
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		w = mysurfaceView.getWidth();
		h = mysurfaceView.getHeight();
		gameSpan = h*4/(5*gameType);
		//����׼�����ˣ������̻߳滭
		this.thread=new MyThread(this, holder, w, h, gameType);
		//�߳̿�ʼ
		this.thread.start();
	}
	/**
	 * ����ı亯��
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
	/**
	 * �������ٺ���
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.thread.setStart(false);
	}
	/**
	 * ���崥���¼�
	 */
	public boolean onTouch(View v, MotionEvent event) {
		//ȡ����ϷĿǰ״̬
		switch(thread.getGameStatu()){
		//����״̬
		case MyThread.RUNNING:
			confirmRole(event);
			break;
		//����״̬
		case MyThread.GAMEOVER:
			backOrStart(event);
			break;
			default:
				break;
		}
		return true;
	}
	/**
	 * ��Ϸ
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
	 * ȷ�����������
	 * @param event
	 */
	private void confirmRole(MotionEvent event) {
		int action = event.getAction();
		switch(action&MotionEvent.ACTION_MASK){
		//��һ����ָ������
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