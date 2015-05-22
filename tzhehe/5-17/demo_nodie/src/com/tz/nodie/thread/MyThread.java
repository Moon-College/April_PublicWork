package com.tz.nodie.thread;

import java.util.Random;

import com.tz.nodie.R;
import com.tz.nodie.model.Role;
import com.tz.nodie.util.Constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class MyThread extends Thread {
	private SurfaceHolder holder;
	private int w, h, gameType;
	private Context context;
	private boolean isStart;
	private Paint paint;
	private Role role;
	// 游戏的状态
	private int gameStatu = 0;
	private int gameSpan;
	// 线的数组
	public Rect[] rects;
	// 人物的数组
	public Role[] roles;
	private Bitmap[] bitmaps;
	private int[] imgs;
	private Long overTime;
	private long startTime;

	public MyThread(int w, int h, Context context, SurfaceHolder holder,
			int gameType) {
		this.w = w;
		this.h = h;
		this.context = context;
		this.holder = holder;
		this.gameType = gameType;
		isStart = true;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		imgs = new int[] { R.drawable.role1_00, R.drawable.role1_01,
				R.drawable.role1_02, R.drawable.role1_03, R.drawable.role1_04,
				R.drawable.role1_05 };
		bitmaps = new Bitmap[imgs.length];
		for (int i = 0; i < imgs.length; i++) {
			bitmaps[i] = BitmapFactory.decodeResource(context.getResources(),
					imgs[i]);
		}
		/*
		 * role = new Role(bitmaps); role.setX(100); role.setY(200);
		 */
		// 每个游戏之间的宽度
		gameSpan = h * 4 / 5 / gameType;
		rects = new Rect[gameType];
		roles = new Role[gameType];
		initSpirit();
	}

	// 初始化游戏里面的元素
	public void initSpirit() {
		startTime = System.currentTimeMillis();
		// 创建人物
		for (int i = 0; i < gameType; i++) {
			// 第i根线的Y坐标
			int lineY = h / 10 + (i + 1) * gameSpan;
			// 创建人物
			Role role = new Role(bitmaps);// 创建一个role
			role.setX(w / 10);
			role.setY(lineY - role.getHeight());
			roles[i] = role;
			// 创建障碍物
			Rect rect = new Rect();
			// 随机的宽高：宽1/4人物宽~6/4人物宽
			int random_w = (int) (role.getWidth() * (Math.random() * 5 + 1) / 4);
			int random_h = (int) (role.getHeight() * (Math.random() * 5 + 1) / 4);
			rect.left = 3 * w / 2;// 障碍物的左边
			rect.right = rect.left + random_w;// 障碍物的右边
			rect.bottom = lineY;
			rect.top = rect.bottom - random_h;
			// 添加到数组
			rects[i] = rect;

		}
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
		super.run();
		while (isStart) {
			Canvas canvas = null;
			try {
				// 得到画布
				canvas = holder.lockCanvas();
				if (canvas != null) {
					switch (gameStatu) {
					case Constant.RUNNING:
						gameRunning(canvas);
						break;
					case Constant.STANDOFF:
						gameStanoff(canvas);
						break;
					case Constant.GAMEOVER:
						gameOver(canvas);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {

			} finally {
				if (canvas != null) {
					// 绑定画板就必须要解锁，不然会报错
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	/**
	 * 运行游戏的时候
	 * 
	 * @param canvas
	 */
	private void gameRunning(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		// role.drawSelf(canvas);
		// 绘制右上角的得分文本
		paint.setTextSize(20);
		String score = (System.currentTimeMillis() - startTime) / 1000f + "\'";
		float score_w = paint.measureText(score);
		canvas.drawText(score, w - score_w, -paint.ascent(), paint);
		
		//画底部开发者logo
		String logo = "一个都不能死-呵呵";
		float logo_y = 9*h/10+(h/10 -paint.descent()+paint.ascent())/2-paint.ascent();
		drawText(canvas, logo, logo_y, 20);
		
		for (int i = 0; i < gameType; i++) {
			int lineY = h / 10 + (i + 1) * gameSpan;

			// 障碍物一直在移动
			rects[i].left = rects[i].left + (-h / 150);
			rects[i].right = rects[i].right + (-h / 150);
			if (rects[i].left <= 0) {
				int random_w = (int) (role.getWidth() * (Math.random() * 5 + 1) / 4);
				int random_h = (int) (role.getHeight()* (Math.random() * 5 + 1) / 4);
				int random_start = (int) (w * (new Random().nextFloat() / 2));
				// 从新设置
				rects[i].left = 3 * w / 2 + random_start;
				rects[i].right = rects[i].left + random_w;
				rects[i].top = rects[i].bottom - random_h;
			}

			// 模拟人向下的加速度
			roles[i].setSpeedY(roles[i].getSpeedY() + h / 800f);
			roles[i].setY(roles[i].getY() + roles[i].getSpeedY());
			// 如果超过了脚下线的高度就停止往下
			if (roles[i].getY() + roles[i].getHeight() >= lineY) {
				roles[i].setSpeedY(0);
				roles[i].setY(lineY - roles[i].getHeight());
				roles[i].setJump(false);
			}
			if (rects[i].intersect(roles[i].getRectFromRole())) {
				// 碰到了
				// 进入僵持状态
				gameStatu = Constant.STANDOFF;
				overTime = System.currentTimeMillis();
			}

			// 画线
			canvas.drawLine(0, lineY, w, lineY, paint);
			// 画一个人
			roles[i].drawSelf(canvas);
			// 绘制一个障碍物
			canvas.drawRect(rects[i], paint);
		}

	}

	private void gameOver(Canvas canvas) {
		// 游戏结束了
		canvas.drawColor(Color.RED);
		// 绘制文字
		String[] modes = new String[] { "普通", "噩梦", "地狱", "炼狱" };
		String mode = modes[gameType - 2];
		String scoreType = mode + ":" + (overTime - startTime) / 1000f + "\'";
		String back = "返回";
		String reStart = "重来";
		// 绘制3段文本
		drawText(canvas, scoreType, h / 2, 28);
		drawText(canvas, back, 4 * h / 6, 24);
		drawText(canvas, reStart, 5 * h / 6, 24);

	}

	/**
	 * 游戏结束
	 * 
	 * @param canvas
	 */
	private void gameStanoff(Canvas canvas) {
		if (System.currentTimeMillis() - overTime >= 1000) {
			// 游戏gameOver
			gameStatu = Constant.GAMEOVER;
		} else {
			for (int i = 0; i < gameType; i++) {
				// 第i根线的Y坐标
				int lineY = h / 10 + (i + 1) * gameSpan;
				// 画线
				canvas.drawLine(0, lineY, w, lineY, paint);
				// 画一个人
				roles[i].drawSelf(canvas);
				// 绘制一个障碍物
				canvas.drawRect(rects[i], paint);
			}
		}

	}
/**
 * 绘制文本框
 * @param canvas
 * @param text
 * @param y
 * @param textSize
 */
	public void drawText(Canvas canvas, String text, float y, int textSize) {
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);// 文本的宽度
		canvas.drawText(text, (w - measureText) / 2, y, paint);
	}
	
	
	public int getGameStatu(){
		return gameStatu;
	}
	
	public void setGameStatu(int gameStatu){
		this.gameStatu=gameStatu;
	}

	public int getGameSpan() {
		return gameSpan;
	}

	
	
}
