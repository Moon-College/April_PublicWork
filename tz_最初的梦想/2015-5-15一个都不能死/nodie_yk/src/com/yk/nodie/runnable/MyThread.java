package com.yk.nodie.runnable;

import java.util.Random;

import com.yk.nodie.R;
import com.yk.nodie.role.Role;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * ���߳��л��ƶ���
 * 
 * @author yk
 * 
 */
public class MyThread extends Thread {
	/**
	 * ������
	 */
	private Context context;
	/**
	 * ����ܼ�
	 */
	private SurfaceHolder holder;
	/**
	 * ����
	 */
	private Paint paint;
	/**
	 * �����Ⱥ͸߶�
	 */
	private int w, h;
	/**
	 * ��Ϸ�ȼ�
	 */
	private int gameLevel;
	/**
	 * ͼƬ��Դ
	 */
	private Bitmap[] bms;
	/**
	 * ͼƬ·��
	 */
	private int[] paths;
	/**
	 * �Ƿ�ʼ����
	 */
	private boolean isStart;
	/**
	 * ��������
	 */
	public Role[] roles;
	/**
	 * ��Ϸ��������״̬
	 */
	public final static int RUNNING = 0;
	/**
	 * ��Ϸ����״̬
	 */
	public final static int STANDOFF = 1;
	/**
	 * ��Ϸ����״̬
	 */
	public final static int GAMEOVER = 2;
	/**
	 * ��Ϸ״̬ Ĭ����������
	 */
	private int gameStatus = RUNNING;
	/**
	 * ��Ϸ����߶�
	 */
	private int gameSpan;
	
	/**
	 * �ϰ�������
	 */
	private Rect[] rects;
	/**
	 * ��Ϸ��ʼʱ��
	 */
	private long startTime;
	/**
	 * ��Ϸ����ʱ��
	 */
	private long overTime;

	public MyThread(Context context, SurfaceHolder holder, int w, int h, int gameLevel) {
		super();
		this.context = context;
		this.holder = holder;
		this.w = w;
		this.h = h;
		this.gameLevel = gameLevel;
		paint = new Paint();
		isStart = true;
		paths = new int[] { R.drawable.role1_00, R.drawable.role1_01, R.drawable.role1_02, R.drawable.role1_03, R.drawable.role1_04, R.drawable.role1_05, };
		bms = new Bitmap[paths.length];
		for (int i = 0; i < paths.length; i++) {
			bms[i] = BitmapFactory.decodeResource(context.getResources(), paths[i]);
		}
		//������Ϸ����߶�
		gameSpan=h*4/(5*gameLevel);
		//��ʼ����������
		roles=new Role[gameLevel];
		rects=new Rect[gameLevel];
		//��ʼ������
		initSpirit();
		
	}

	@Override
	public void run() {
		super.run();
		while (isStart) {
			Canvas canvas=null;
			try {
				// ��ȡ���忪ʼ����
				canvas = holder.lockCanvas();
				if (canvas != null) {
					switch (gameStatus) {
					case RUNNING:
						// ��Ϸ��������
						drawRunningView(canvas);
						break;
					case STANDOFF:
						// ��Ϸ����״̬
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						// ��Ϸ����
						drawGameOverView(canvas);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(canvas!=null){
					//���������Ϊ�վͽ�������
					holder.unlockCanvasAndPost(canvas);
				}
			}
			
		}
		
	}

	
	/**
	 * ������Ϸ�������еĻ���
	 * @param canvas
	 */
	private void drawRunningView(Canvas canvas) {
		//���ư�ɫ����
		canvas.drawColor(Color.WHITE);
		//���û��ʴ�ϸ
		paint.setStrokeWidth(5);
		for (int i = 0; i < gameLevel; i++) {
			//��ȡ�ߵ�y����
			int line_y=h/10+(i+1)*gameSpan;
			//���ϰ���������������ƶ�
			rects[i].left=rects[i].left-w/180;
			rects[i].right=rects[i].right-w/180;
			//ģ�⾫�����µļ��ٶ�
			roles[i].setSpeedY(roles[i].getSpeedY()+h/800f);
			roles[i].setY(roles[i].getY()+roles[i].getSpeedY());
			//����Ѿ����
			if(roles[i].getY()>line_y-roles[i].getHeith()){
				
				roles[i].setY(line_y-roles[i].getHeith());
				//���ü��ٶ�Ϊ0
				roles[i].setSpeedY(0);
				roles[i].setIsJump(false);
			}
			//�ж�����ϰ����ƶ�������ٴ��ұ�3/2+�����ȷ�Χ��0~1/2*w
			if(rects[i].right<=0){
				rects[i].left=(int) (w*(3/2+new Random().nextFloat()/2));
				int rect_w=(int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				int rect_h=(int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				rects[i].right=rects[i].left+rect_w;
				rects[i].top=line_y-rect_h;
			}
			
			//������������ཻ�������ײ
			if(rects[i].intersect(roles[i].getRectFromRole())){
				//���뽩��ģʽ
				gameStatus=STANDOFF;
				overTime=System.currentTimeMillis();
			}
			paint.setTextSize(30);
			//���·���
			String score=(System.currentTimeMillis()-startTime)+"\'";
			float score_w=paint.measureText(score);
			//�������Ͻǵ÷�
			canvas.drawText(score,w-score_w,-paint.ascent(), paint);
			//����logo
			String logo="���������-yk";
			float logo_w=paint.measureText(logo);
			float logo_h=9*h/10+(h/10-(paint.descent()-paint.ascent()))/2-paint.ascent();
			canvas.drawText(logo,(w-logo_w)/2,logo_h, paint);
			//��������
			canvas.drawLine(0, line_y, w, line_y, paint);
			//���ƾ���
			roles[i].drawSelf(canvas);
			//�����ϰ���
			canvas.drawRect(rects[i], paint);
		}
		
	}
	/**
	 * ������Ϸ����״̬��ͼ
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		if(System.currentTimeMillis()-overTime>1000){
			//����һ��������Ϸ����״̬
			gameStatus=GAMEOVER;
		}else{//���ƽ���״̬�Ļ���
			for (int i = 0; i < gameLevel; i++) {
				//��ȡ�ߵ�y����
				int line_y=h/10+(i+1)*gameSpan;
				//��������
				canvas.drawLine(0, line_y, w,line_y, paint);		
				//���ƾ���С��
				roles[i].drawSelf(canvas);
				//���ƾ����ϰ���
				canvas.drawRect(rects[i], paint);
			}
			
		}
	}
	/**
	 * ������Ϸ����״̬��ͼ
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		//������Ϸ��������
		canvas.drawColor(Color.RED);
		//׼������
		String[] modes=new String[]{"��ͨ","ج��","����","����"};
		//��Ϸ�÷�
		String gameScore=modes[gameLevel-2]+"��"+(overTime-startTime)/1000f+"\'";
		String back="����";
	    String restart="����";
		
		drawTextCenter(canvas,gameScore,h/2,32);//3/6 4/6 5/6���ּ�����
		drawTextCenter(canvas, back, 2*h/3, 24);
		drawTextCenter(canvas, restart, 5*h/6, 24);
	}
	/**
	 * �����ı�
	 * @param canvas ����
	 * @param text �ı�
	 * @param y �ı�����y����
	 * @param textSize �ı���С
	 */
	private void drawTextCenter(Canvas canvas,String text,float y,int textSize) {
		paint.setTextSize(textSize);
		float text_w=paint.measureText(text);
		float text_h=paint.descent()-paint.ascent();
		canvas.drawText(text,(w-text_w)/2,y, paint);
	}

	public void initSpirit() {
		//������Ϸ��ʼʱ��
		startTime=System.currentTimeMillis();
		
		for (int i = 0; i < gameLevel; i++) {
			//��ȡ�ߵ�y����
			int line_y=h/10+(i+1)*gameSpan;
			//�����������
			Role role=new Role(bms);
			//���þ����λ��
			role.setX(w/5);
			role.setY(line_y-role.getHeith());
			//��Ӿ����������
			roles[i]=role;
			
			//�����ϰ������
			Rect rect=new Rect();
			rect.left=3/2*w;
			//�ϰ������������ ��Χ��1/4�����~6/4�����
			int rect_w=(int) (role.getWidth()*(Math.random()*5+1)/4);
			int rect_h=(int) (role.getWidth()*(Math.random()*5+1)/4);
			rect.top=line_y-rect_h;
			rect.right=3/2*w+rect_w;
			rect.bottom=line_y;
			rects[i]=rect;
		}
	}

	public void setIsStart(boolean isStart) {
		this.isStart = isStart;
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	

}
