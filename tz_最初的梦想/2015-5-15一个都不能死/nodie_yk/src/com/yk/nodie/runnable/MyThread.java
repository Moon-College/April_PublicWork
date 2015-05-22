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
 * 在线程中绘制动画
 * 
 * @author yk
 * 
 */
public class MyThread extends Thread {
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 画板管家
	 */
	private SurfaceHolder holder;
	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 画板宽度和高度
	 */
	private int w, h;
	/**
	 * 游戏等级
	 */
	private int gameLevel;
	/**
	 * 图片资源
	 */
	private Bitmap[] bms;
	/**
	 * 图片路径
	 */
	private int[] paths;
	/**
	 * 是否开始绘制
	 */
	private boolean isStart;
	/**
	 * 精灵数组
	 */
	public Role[] roles;
	/**
	 * 游戏正常运行状态
	 */
	public final static int RUNNING = 0;
	/**
	 * 游戏僵持状态
	 */
	public final static int STANDOFF = 1;
	/**
	 * 游戏结束状态
	 */
	public final static int GAMEOVER = 2;
	/**
	 * 游戏状态 默认正常运行
	 */
	private int gameStatus = RUNNING;
	/**
	 * 游戏区间高度
	 */
	private int gameSpan;
	
	/**
	 * 障碍物数组
	 */
	private Rect[] rects;
	/**
	 * 游戏开始时间
	 */
	private long startTime;
	/**
	 * 游戏结束时间
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
		//计算游戏区间高度
		gameSpan=h*4/(5*gameLevel);
		//初始化精灵数组
		roles=new Role[gameLevel];
		rects=new Rect[gameLevel];
		//初始化精灵
		initSpirit();
		
	}

	@Override
	public void run() {
		super.run();
		while (isStart) {
			Canvas canvas=null;
			try {
				// 获取画板开始绘制
				canvas = holder.lockCanvas();
				if (canvas != null) {
					switch (gameStatus) {
					case RUNNING:
						// 游戏正常运行
						drawRunningView(canvas);
						break;
					case STANDOFF:
						// 游戏僵持状态
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						// 游戏结束
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
					//如果画布不为空就解锁画布
					holder.unlockCanvasAndPost(canvas);
				}
			}
			
		}
		
	}

	
	/**
	 * 绘制游戏正常运行的画面
	 * @param canvas
	 */
	private void drawRunningView(Canvas canvas) {
		//绘制白色背景
		canvas.drawColor(Color.WHITE);
		//设置画笔粗细
		paint.setStrokeWidth(5);
		for (int i = 0; i < gameLevel; i++) {
			//获取线的y坐标
			int line_y=h/10+(i+1)*gameSpan;
			//让障碍物从右向左匀速移动
			rects[i].left=rects[i].left-w/180;
			rects[i].right=rects[i].right-w/180;
			//模拟精灵向下的加速度
			roles[i].setSpeedY(roles[i].getSpeedY()+h/800f);
			roles[i].setY(roles[i].getY()+roles[i].getSpeedY());
			//如果已经落地
			if(roles[i].getY()>line_y-roles[i].getHeith()){
				
				roles[i].setY(line_y-roles[i].getHeith());
				//设置加速度为0
				roles[i].setSpeedY(0);
				roles[i].setIsJump(false);
			}
			//判断如果障碍物移动到左边再从右边3/2+随机宽度范围：0~1/2*w
			if(rects[i].right<=0){
				rects[i].left=(int) (w*(3/2+new Random().nextFloat()/2));
				int rect_w=(int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				int rect_h=(int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				rects[i].right=rects[i].left+rect_w;
				rects[i].top=line_y-rect_h;
			}
			
			//如果两个矩形相交则产生碰撞
			if(rects[i].intersect(roles[i].getRectFromRole())){
				//进入僵持模式
				gameStatus=STANDOFF;
				overTime=System.currentTimeMillis();
			}
			paint.setTextSize(30);
			//更新分数
			String score=(System.currentTimeMillis()-startTime)+"\'";
			float score_w=paint.measureText(score);
			//绘制右上角得分
			canvas.drawText(score,w-score_w,-paint.ascent(), paint);
			//绘制logo
			String logo="最初的梦想-yk";
			float logo_w=paint.measureText(logo);
			float logo_h=9*h/10+(h/10-(paint.descent()-paint.ascent()))/2-paint.ascent();
			canvas.drawText(logo,(w-logo_w)/2,logo_h, paint);
			//绘制线条
			canvas.drawLine(0, line_y, w, line_y, paint);
			//绘制精灵
			roles[i].drawSelf(canvas);
			//绘制障碍物
			canvas.drawRect(rects[i], paint);
		}
		
	}
	/**
	 * 绘制游戏僵持状态视图
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		if(System.currentTimeMillis()-overTime>1000){
			//僵持一秒后进入游戏结束状态
			gameStatus=GAMEOVER;
		}else{//绘制僵持状态的画面
			for (int i = 0; i < gameLevel; i++) {
				//获取线的y坐标
				int line_y=h/10+(i+1)*gameSpan;
				//绘制线条
				canvas.drawLine(0, line_y, w,line_y, paint);		
				//绘制精灵小人
				roles[i].drawSelf(canvas);
				//绘制精灵障碍物
				canvas.drawRect(rects[i], paint);
			}
			
		}
	}
	/**
	 * 绘制游戏结束状态视图
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		//绘制游戏结束背景
		canvas.drawColor(Color.RED);
		//准备文字
		String[] modes=new String[]{"普通","噩梦","地狱","炼狱"};
		//游戏得分
		String gameScore=modes[gameLevel-2]+"："+(overTime-startTime)/1000f+"\'";
		String back="返回";
	    String restart="重来";
		
		drawTextCenter(canvas,gameScore,h/2,32);//3/6 4/6 5/6文字间距相等
		drawTextCenter(canvas, back, 2*h/3, 24);
		drawTextCenter(canvas, restart, 5*h/6, 24);
	}
	/**
	 * 绘制文本
	 * @param canvas 画布
	 * @param text 文本
	 * @param y 文本基线y坐标
	 * @param textSize 文本大小
	 */
	private void drawTextCenter(Canvas canvas,String text,float y,int textSize) {
		paint.setTextSize(textSize);
		float text_w=paint.measureText(text);
		float text_h=paint.descent()-paint.ascent();
		canvas.drawText(text,(w-text_w)/2,y, paint);
	}

	public void initSpirit() {
		//设置游戏开始时间
		startTime=System.currentTimeMillis();
		
		for (int i = 0; i < gameLevel; i++) {
			//获取线的y坐标
			int line_y=h/10+(i+1)*gameSpan;
			//创建精灵对象
			Role role=new Role(bms);
			//设置精灵的位置
			role.setX(w/5);
			role.setY(line_y-role.getHeith());
			//添加精灵对象到数组
			roles[i]=role;
			
			//创建障碍物对象
			Rect rect=new Rect();
			rect.left=3/2*w;
			//障碍物宽高随机产生 范围：1/4人物宽~6/4人物宽
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
