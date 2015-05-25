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
	private int difficultMode;              //游戏难度
	private boolean isStart;                //控制是否绘制
	private int gameStatu = 0;              //游戏阶段
	public static final int RUNNING = 0;          //
	public static final int STANDOFF = 1;         //游戏阶段
	public static final int GAMEOVER = 2;         //
	private int gameSpan;                   //游戏区间
	public Role[] roles;                    //人物数组
	public Rect[] rects;                   //障碍物数组
	private Bitmap[] bms;                   //人物动作图片数组
	private int [] paths;                   //人物动作图片地址
	private Bitmap heartMap;                //心图
	private Bitmap left_heart;              //左半心
	private int heartWidth;                 //心形宽度
	private int blood;                      //血量
	private int halfHeart;                  //半心数量
	private Bitmap boomMap;                    //炸弹图
	private int boomWidth;                  //炸弹宽度
	public int boom;                       //炸弹数量
	private boolean [] isTouchIng;           //判断是否在碰撞中
	private int roleHeigth;                 //人物高度	
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
		//初始化心形图片
		heartMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		left_heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.left_heart);
		heartWidth = heartMap.getWidth();
		//初始化炸弹图片
		boomMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.boom);
		boomWidth = boomMap.getWidth();
		initBitmap();
		//计算游戏区间高度
		gameSpan = h*4/(5*difficultMode);
		roles = new Role[difficultMode];
		rects = new Rect[difficultMode];
		isTouchIng = new boolean[difficultMode];
		initSpirit();

	}
	/**
	 * 初始化图片数组
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
		//执行绘画
		while(isStart){
			//获取画布
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();//将画布绑定给画板
					if(canvas!=null){
						//确认画布准备好，可以开始绘制
						//根据游戏阶段绘图
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
	 * 游戏结束状态
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		canvas.drawColor(Color.RED);
		String [] modesStrings = new String[]{
				"普通","噩梦","地狱","炼狱"
		};
		String  mode = modesStrings[difficultMode-2];
		String scoreType = mode + ":" + (overTime - startTime)/1000f + "\'";
		String back = "返回";
		String reStart = "重来";
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
	 * 游戏僵持状态
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
	 * 游戏运行状态
	 * @param canvas
	 */
	private void drawRunningView(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		//绘制血量-----------------------------------------------
		if(blood == 3){
			for(int i = 0 ; i < blood; i++){
				canvas.drawBitmap(heartMap, i*heartWidth,0, paint);
			}
			halfHeart = 0;
		}else{
			for(int i = 0 ; i < blood; i++){
				canvas.drawBitmap(heartMap, i*heartWidth,0, paint);
			}
			//判断是否有半心，
			if (halfHeart == 1) {
				canvas.drawBitmap(left_heart,blood*heartWidth, 0, paint);
			}else if(halfHeart >= 2){
				canvas.drawBitmap(heartMap, blood, blood*halfHeart, paint);
				blood++;
				halfHeart = 0;
			}else {
			}
		}
		//绘制时间-----------------------------------------------
		String score = "时间:"+(System.currentTimeMillis() - startTime)/1000f+"\'";
		paint.setTextSize(h/20);
		canvas.drawText(score, w*1/2,  -paint.ascent(), paint);
		canvas.drawBitmap(heartMap, 0,0, paint);
		paint.setStrokeWidth(5);
		//绘制炸弹---------------------------------------------
		for(int i = 0; i<boom; i++){
			canvas.drawBitmap(boomMap, i*boomWidth, h/10+difficultMode*gameSpan, paint);
		}
		String clickMe = "《----点击我";
		canvas.drawText(clickMe, w*1/2, 9*h/10+(h/10 -paint.descent()+paint.ascent())/2-paint.ascent(), paint);
		//绘制游戏区--------------------------------------------
		for(int i = 0;i<difficultMode;i++){
			int lineY = h/10+(i+1)*gameSpan;	       			//第i根线的Y坐标								
			//模拟人向下加速度
			roles[i].setSpeedY(roles[i].getSpeedY() + h/800f);
			roles[i].setY(roles[i].getY() + roles[i].getSpeedY());
			//判断是否落地
			if (roles[i].getY() + roleHeigth >= lineY) {
				roles[i].setY(lineY - roleHeigth);
				roles[i].setSpeedY(0);
				roles[i].setJump(false);
			}		
			
			//先让障碍从右往左移动
			rects[i].left = rects[i].left + (-h/150);
			rects[i].right = rects[i].right + (-h/150);
			//障碍物位置重置
			if(rects[i].right <= 0){
				initRect(rects[i],i, lineY);
				if(isTouchIng[i] == false){
					halfHeart++;
				}else{
					isTouchIng[i] = false;
				}
			}
			//判断是否碰到障碍
			if(rects[i].intersect(roles[i].getRectFromRole())){
				//判断该人物是否处于碰撞状态
				if(isTouchIng[i] == false){
					//判断血量是否足够
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
	 * 创建人物,障碍物
	 */
	public void initSpirit(){
		blood = 3;                                              //初始化血量
		halfHeart = 0;											//初始化半心数量
		boom = 3;                                               //初始化炸弹数
		startTime = System.currentTimeMillis();                 //初始化开始时间
		for(int i = 0; i < difficultMode; i++) {
			int lineY = h/10+(i+1)*gameSpan;
			Role role = new Role(bms, w/8,lineY-bms[0].getHeight());
			roles[i] = role;
			//障碍物初始化
			Rect rect = new Rect();
			initRect(rect,i,lineY);			
		}
	}
	/**
	 * 障碍物初始化
	 * @param i
	 * @param lineY
	 * @param role
	 */
	public void initRect(Rect rect,int i,int lineY) {
		//随机宽高0.25~1.5倍人物宽
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
