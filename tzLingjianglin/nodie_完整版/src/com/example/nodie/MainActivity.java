package com.example.nodie;

import com.example.nodie.runnable.GameThread;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener,Callback,OnTouchListener{
	private int araeSpan;
	private GameThread thread;
	private SurfaceView surfaceView;
	private ImageView normal,nightmare,hell,pur;
	private int gameType;    //��ʾ��Ϸ���Ѷ�ϵ��
	private int w;
	private int h;   //��Ϸ����Ŀ�͸�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		setContentView(R.layout.main);
		normal=(ImageView) findViewById(R.id.normal);
		nightmare=(ImageView) findViewById(R.id.nightmare);
		hell=(ImageView) findViewById(R.id.hell);
		pur=(ImageView) findViewById(R.id.pur);
		//��ӵ���¼��ļ���
		normal.setOnClickListener(this);
		nightmare.setOnClickListener(this);
		hell.setOnClickListener(this);
		pur.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		case R.id.pur:
			gameType=5;
			break;
		default:
			break;
		}
		startGameView();
	}
	/**
	 * ������Ϸ����
	 */
	private void startGameView() {
		surfaceView = new SurfaceView(this);
		surfaceView.setOnTouchListener(this);
		surfaceView.getHolder().addCallback(this);
		setContentView(surfaceView);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		w = surfaceView.getWidth();//�����
		h = surfaceView.getHeight();//�����
		araeSpan=4*h/(5*gameType);
		thread=new GameThread(surfaceView.getHolder(), this, gameType, w, h);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread.setFlag(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("onTouch�����˶�");
		switch (thread.getGameStatus()) {
		case GameThread.RUNNING:
			confirmRole(event);
			break;
		case GameThread.STANDOFF:
			
			break;
		case GameThread.GAMEOVER:
			backOrRepeat(event);  //���ػ�������
			break;
		default:
			break;
		}
		return true;
	}
	private void backOrRepeat(MotionEvent event) {
		float y = event.getY();
		if(y>=h/2&&y<=3*h/4){
			//����
			initView();
		}else if(y>3*h/4){
			//����
			restart();
		}
		
	}

	private void restart() {
		thread.setGameStatus(GameThread.RUNNING);//������Ϸ�ص���������״̬
		thread.initRole();//��ʼ������
	}

	//�������¼�
	private void confirmRole(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			float y=event.getY();
			//�ö�Ӧ��Ϸ�����ڵ�role������
			roleJump(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			int count=event.getPointerCount();
			for(int i=0;i<count;i++){
				float dy=event.getY(i);
				roleJump(dy);
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 * ������Ǹ�role����
	 * @param y   �����Ļ��λ��
	 */
	private void roleJump(float y) {
		for(int i=0;i<gameType;i++){
			System.out.println("araeSpan=="+araeSpan);
			int lineY1=h/10+i*araeSpan;
			int lineY2=h/10+(i+1)*araeSpan;
			if(y>=lineY1 && y< lineY2 && !thread.roles[i].isJump()){
				//������򱻵㵽�ˣ���i������
				//������GameThread����speedY
				thread.roles[i].setSpeedY(-h/45);//���ϵ��ٶ�
				thread.roles[i].setJump(true);
			}
		}
		
	}

}
