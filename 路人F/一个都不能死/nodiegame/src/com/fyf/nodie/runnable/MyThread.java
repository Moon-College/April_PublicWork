package com.fyf.nodie.runnable;

import java.util.Random;

import com.fyf.nodie.R;
import com.fyf.nodie.people.Role;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread{
	private SurfaceHolder holder;
	private Context context;
	private int w,h; 
	private int difficultMode;              //��Ϸ�Ѷ�
	private boolean isStart;                //�����Ƿ����
	private int gameStatu = 0;              //��Ϸ�׶�
	public static final int RUNNING = 0;          //
	public static final int STANDOFF = 1;         //��Ϸ�׶�
	public static final int GAMEOVER = 2;         //
	private int gameSpan;                   //��Ϸ����
	public Role[] roles;                    //��������
	public Rect[] rects;                   //�ϰ�������
	private Bitmap[] bms;                   //���ﶯ��ͼƬ����
	private int [] paths;                   //���ﶯ��ͼƬ��ַ
	private Bitmap heartMap;                //��ͼ
	private Bitmap left_heart;              //�����
	private int heartWidth;                 //���ο��
	private int blood;                      //Ѫ��
	private int halfHeart;                  //��������
	private Bitmap boomMap;                    //ը��ͼ
	private int boomWidth;                  //ը�����
	public int boom;                       //ը������
	private boolean [] isTouchIng;           //�ж��Ƿ�����ײ��
	private int roleHeigth;                 //����߶�	
	private boolean isPause;
	private long startTime;
	private long overTime;
	Paint paint;
	public MyThread(Context context,SurfaceHolder holder,int w,int h,int difficultMode){
		this.context = context;
		this.holder = holder;
		this.w = w;
		this.h = h;
		this.difficultMode = difficultMode;
		paint = new Paint();
		isStart = true;
		//��ʼ������ͼƬ
		heartMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		left_heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.left_heart);
		heartWidth = heartMap.getWidth();
		//��ʼ��ը��ͼƬ
		boomMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.boom);
		boomWidth = boomMap.getWidth();
		initBitmap();
		//������Ϸ����߶�
		gameSpan = h*4/(5*difficultMode);
		roles = new Role[difficultMode];
		rects = new Rect[difficultMode];
		isTouchIng = new boolean[difficultMode];
		initSpirit();

	}
	/**
	 * ��ʼ��ͼƬ����
	 */
	private void initBitmap() {
		paths = new int[]{
				R.drawable.role1_00,	
				R.drawable.role1_01,
				R.drawable.role1_02,
				R.drawable.role1_03,
				R.drawable.role1_04,
				R.drawable.role1_05
			};
		bms = new Bitmap[paths.length];
		for(int i=0;i<paths.length;i++){
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),paths[i]);
			bms[i] = bitmap;				
		} 
		roleHeigth = bms[0].getHeight();
	}
	@Override
	public void run() {
		super.run();
		//ִ�л滭
		while(isStart){
			//��ȡ����
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();//�������󶨸�����
					if(canvas!=null){
						//ȷ�ϻ���׼���ã����Կ�ʼ����
						//������Ϸ�׶λ�ͼ
						switch (gameStatu) {
						case RUNNING:
							drawRunningView(canvas);
							break;
						case STANDOFF:
							drawStandoffView(canvas);
							break;
						case GAMEOVER:
							drawGameOverView(canvas);
							break;
						default:
							break;
						}
					}
				} catch (Exception e) {
				
				}finally{
					if(canvas!=null){
						holder.unlockCanvasAndPost(canvas);
					}
				}			
		}
	}
	/**
	 * ��Ϸ����״̬
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		canvas.drawColor(Color.RED);
		String [] modesStrings = new String[]{
				"��ͨ","ج��","����","����"
		};
		String  mode = modesStrings[difficultMode-2];
		String scoreType = mode + ":" + (overTime - startTime)/1000f + "\'";
		String back = "����";
		String reStart = "����";
		drawText(canvas, scoreType, h*1/6, h*1/15);
		drawText(canvas, back, h*4/6, h*1/15);
		drawText(canvas, reStart, h*5/6, h*1/15);
	}
	public void drawText(Canvas canvas,String text,float y,float textSize){
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);
		canvas.drawText(text, (w - measureText)/2, y, paint);
	}
	/**
	 * ��Ϸ����״̬
	 * @param canvas
	 */
	private void drawStandoffView(Canvas canvas) {
		if(System.currentTimeMillis() - overTime >= 1000){		
			gameStatu = GAMEOVER;
		}else{
			for(int i = 0;i<difficultMode;i++){
				int lineY = h/10+(i+1)*gameSpan;									
				canvas.drawLine(0, h/10, w, h/10, paint);
				canvas.drawLine(0, lineY, w, lineY, paint);
				roles[i].drawSelf(canvas);
				canvas.drawRect(rects[i], paint);
			}
		}
	}
	/**
	 * ��Ϸ����״̬
	 * @param canvas
	 */
	private void drawRunningView(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		//����Ѫ��-----------------------------------------------
		if(blood == 3){
			for(int i = 0 ; i < blood; i++){
				canvas.drawBitmap(heartMap, i*heartWidth,0, paint);
			}
			halfHeart = 0;
		}else{
			for(int i = 0 ; i < blood; i++){
				canvas.drawBitmap(heartMap, i*heartWidth,0, paint);
			}
			//�ж��Ƿ��а��ģ�
			if (halfHeart == 1) {
				canvas.drawBitmap(left_heart,blood*heartWidth, 0, paint);
			}else if(halfHeart >= 2){
				canvas.drawBitmap(heartMap, blood, blood*halfHeart, paint);
				blood++;
				halfHeart = 0;
			}else {
			}
		}
		//����ʱ��-----------------------------------------------
		String score = "ʱ��:"+(System.currentTimeMillis() - startTime)/1000f+"\'";
		paint.setTextSize(h/20);
		canvas.drawText(score, w*1/2,  -paint.ascent(), paint);
		canvas.drawBitmap(heartMap, 0,0, paint);
		paint.setStrokeWidth(5);
		//����ը��---------------------------------------------
		for(int i = 0; i<boom; i++){
			canvas.drawBitmap(boomMap, i*boomWidth, h/10+difficultMode*gameSpan, paint);
		}
		String clickMe = "��----�����";
		canvas.drawText(clickMe, w*1/2, 9*h/10+(h/10 -paint.descent()+paint.ascent())/2-paint.ascent(), paint);
		//������Ϸ��--------------------------------------------
		for(int i = 0;i<difficultMode;i++){
			int lineY = h/10+(i+1)*gameSpan;	       			//��i���ߵ�Y����								
			//ģ�������¼��ٶ�
			roles[i].setSpeedY(roles[i].getSpeedY() + h/800f);
			roles[i].setY(roles[i].getY() + roles[i].getSpeedY());
			//�ж��Ƿ����
			if (roles[i].getY() + roleHeigth >= lineY) {
				roles[i].setY(lineY - roleHeigth);
				roles[i].setSpeedY(0);
				roles[i].setJump(false);
			}		
			
			//�����ϰ����������ƶ�
			rects[i].left = rects[i].left + (-h/150);
			rects[i].right = rects[i].right + (-h/150);
			//�ϰ���λ������
			if(rects[i].right <= 0){
				initRect(rects[i],i, lineY);
				if(isTouchIng[i] == false){
					halfHeart++;
				}else{
					isTouchIng[i] = false;
				}
			}
			//�ж��Ƿ������ϰ�
			if(rects[i].intersect(roles[i].getRectFromRole())){
				//�жϸ������Ƿ�����ײ״̬
				if(isTouchIng[i] == false){
					//�ж�Ѫ���Ƿ��㹻
					if (blood == 1) {
						gameStatu = STANDOFF;
						overTime = System.currentTimeMillis();
					}else{
						blood--;
						isTouchIng[i] = true;
					}
				}		
			}
			canvas.drawLine(0, h/10, w, h/10, paint);
			canvas.drawLine(0, lineY, w, lineY, paint);
			roles[i].drawSelf(canvas);
			canvas.drawRect(rects[i], paint);
		}
		
	}
	/**
	 * ��������,�ϰ���
	 */
	public void initSpirit(){
		blood = 3;                                              //��ʼ��Ѫ��
		halfHeart = 0;											//��ʼ����������
		boom = 3;                                               //��ʼ��ը����
		startTime = System.currentTimeMillis();                 //��ʼ����ʼʱ��
		for(int i = 0; i < difficultMode; i++) {
			int lineY = h/10+(i+1)*gameSpan;
			Role role = new Role(bms, w/8,lineY-bms[0].getHeight());
			roles[i] = role;
			//�ϰ����ʼ��
			Rect rect = new Rect();
			initRect(rect,i,lineY);			
		}
	}
	/**
	 * �ϰ����ʼ��
	 * @param i
	 * @param lineY
	 * @param role
	 */
	public void initRect(Rect rect,int i,int lineY) {
		//������0.25~1.5�������
		int random_w = (int) (roleHeigth*(Math.random()*5+1)/7);
		int random_h = (int) (roleHeigth*(Math.random()*5+1)/7);
		int random_start = (int) (w*(new Random().nextFloat()/2));
		rect.set(w*3/2+random_start, lineY-random_h, w*3/2+random_start+random_w, lineY);
		rects[i] = rect;
	}
	
	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	public void setGameStatu(int gameStatu) {
		this.gameStatu = gameStatu;
	}
	public int getGameStatu() {
		return gameStatu;
	}

}
