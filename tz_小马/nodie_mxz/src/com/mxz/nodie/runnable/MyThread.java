package com.mxz.nodie.runnable;

import java.util.Random;

import com.mxz.nodie.R;
import com.mxz.nodie.bean.Role;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread {
	private Context context;
	private SurfaceHolder holder;
	private int w,h;
	private Paint paint;
	private int gameType;
	private boolean isStart;
	private Bitmap[] bms;
	private int[] paths;
	public Role[] roles;
	private Rect rects[];	
	private int gameSpan;
	private long startTime;
	private int gameStatu=0;
	private long overTime;
	public static final int RUNNING=0;
	public static final int STANDOFF=1;
	public static final int GAMEOVER=2;

	
	

	public MyThread(Context context,SurfaceHolder holder,int w,int h,int gameType){
		this.context=context;
		this.holder=holder;
		this.w=w;
		this.h=h;
		this.gameType=gameType;
		this.isStart=true;
		this.paint=new Paint();
		this.paths=new int[]{
				R.drawable.role1_00,
				R.drawable.role1_01,
				R.drawable.role1_02,
				R.drawable.role1_03,
				R.drawable.role1_04,
				R.drawable.role1_05,
		};
		this.bms=new Bitmap[paths.length];
		for (int i = 0; i < paths.length; i++) {
			Bitmap bitmap=BitmapFactory.decodeResource(this.context.getResources(), paths[i]);
			this.bms[i]=bitmap;
		}
		
		this.gameSpan=4*h/(5*gameType);
		roles=new Role[gameType];
		rects=new Rect[gameType];
		
		initSpirit();
	}
	
	public void initSpirit() {
		Log.i("INFO",gameType+"==="+roles.length);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < gameType; i++) {
			int lineY=h/10+(i+1)*gameSpan;
			Role role=new Role(this.bms);
			role.setX(w/8);
			role.setY(lineY-role.getHeight());
			this.roles[i]=role;
			
			Rect rect=new Rect();
			int random_w =(int) (role.getWidth()*(Math.random()*5+1)/4)/3;
			int random_h = (int) (role.getHeight()*(Math.random()*5+1)/4)/3;
			
			rect.left=3*w/4;
			rect.right=rect.left+random_w;
			rect.bottom=lineY;
			rect.top =rect.bottom-random_h;
			rects[i]=rect;
			
		}
	}

	@Override
	public void run() {
		super.run();
		while(isStart){
			Canvas canvas=null;
			try{
				canvas=this.holder.lockCanvas();
				if(canvas!=null){
					switch(gameStatu){
					case RUNNING:
						drawRunnintView(canvas);
						break;
					case STANDOFF:
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						drawGameOverView(canvas);
						break;
					default:
						break;
					}
					
				}
			}catch(Exception e){
				
			}finally{
				if(canvas!=null){
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	/**
	 * 游戏运行时
	 * @param canvas
	 */
	private void drawRunnintView(Canvas canvas) {
		//设置画布颜色
		canvas.drawColor(Color.WHITE);
		//设置线的粗细
		this.paint.setStrokeWidth(3);
		
		//设置文本大小
		paint.setTextSize(20);
		//设置文本内容
		String score=(System.currentTimeMillis()-this.startTime)/1000f+"\'";
		//获取文本宽度
		float score_w=paint.measureText(score);
		//绘画文本
		canvas.drawText(score, w-score_w, -paint.ascent(), paint);
		
		
		String logo="小马哥游戏";
		//logo的y坐标
		float logo_y= (float) (9*h/10+(h/10-paint.descent()+paint.ascent())/2-paint.ascent());
		//绘画底部logo
		drawText(canvas,logo,logo_y,20);
		
		for (int i = 0; i < this.roles.length; i++) {
			//取得每个人物的y坐标
			int lineY=h/10+(i+1)*gameSpan;
			
			//设置人物向下的加速度
			this.roles[i].setSpeedY(this.roles[i].getSpeedY()+h/800f);
			//设置人物的y坐标
			this.roles[i].setY(this.roles[i].getY()+this.roles[i].getSpeedY());
			//设置人物踩到线就停止下掉
			if(this.roles[i].getY()+this.roles[i].getHeight()>=lineY){
				this.roles[i].setY(lineY-this.roles[i].getHeight());
				this.roles[i].setSpeedY(0);
				this.roles[i].setJump(false);
				this.roles[i].setJumpTow(false);
			}
			
			
			
			//障碍物移动left减少
			rects[i].left=rects[i].left-(h/150);
			//障碍物移动right减少
			rects[i].right=rects[i].right-(h/150);
			//障碍物right的位置小于等于0，说明障碍物抛出界面了，重新绘制一次
			if(rects[i].right<=0){
				//障碍物随机宽度
				int random_w =(int) (roles[i].getWidth()*(Math.random()*5+1)/4)/2;
				//障碍物随机高度
				int random_h = (int) (roles[i].getHeight()*(Math.random()*5+1)/4)/2;
				//障碍物随机开始位置
				int random_start=(int) (w*new Random().nextFloat()/2);
				//障碍物左边
				rects[i].left=w+random_start;
				//障碍物右边
				rects[i].right=rects[i].left+random_w;
				//障碍物上边
				rects[i].top =rects[i].bottom-random_h;
			}
			
			if(rects[i].intersect(roles[i].getRectFromRole(canvas,paint))){
				
				gameStatu=STANDOFF;
				overTime=System.currentTimeMillis();
			}
			//画人物
			this.roles[i].drawSelf(canvas);
			//画线
			canvas.drawLine(0, lineY, w, lineY, paint);
			//绘制障碍物
			canvas.drawRect(this.rects[i], paint);
			
		}
	}
	private void drawText(Canvas canvas, String text, float y,int textSize) {
		paint.setTextSize(textSize);
		float measureText=paint.measureText(text);
		canvas.drawText(text, (w-measureText)/2, y, paint);
		
	}

	/**
	 * 游戏僵持时
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		if(System.currentTimeMillis()-overTime>=1000){
			gameStatu=GAMEOVER;
		}else{
			for (int i = 0; i < gameType; i++) {
				int lineY=h/10+(i+1)*gameSpan;
				canvas.drawLine(0, lineY, w, lineY, paint);
				roles[i].drawSelf(canvas);
				//canvas.drawRect(rects[i], paint);
			}
		}
	}
	/**
	 * 游戏结束时
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		canvas.drawColor(Color.RED);
		String modes[]=new String[]{"普通","噩梦","地狱","炼狱"};
		String mode=modes[this.gameType-2];
		String scoreType=mode+":"+(overTime-startTime)/1000f+"\'";
		String back="返回";
		String reStart="重来";
		this.drawText(canvas, scoreType, h/2, 28);
		this.drawText(canvas, back, 4*h/6, 24);
		this.drawText(canvas, reStart, 5*h/6, 24);
	}

	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	public int getGameStatu() {
		return gameStatu;
	}
	public void setGameStatu(int gameStatu) {
		this.gameStatu = gameStatu;
	}
}

