package com.tz.died.runable;

import java.util.Random;

import com.tz.died.R;
import com.tz.rale.Role;

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
	private Context context;
	private int w;
	private int h;
	private int[] paths;
	private Bitmap[] bitmaps;
	public boolean isStart;
	private int gameType;
	Paint paint;
	public Role role[];
	private Rect rect[];
	private int gameSpan;
	private int left;
	public int gameStatu = 0;
	public static final int RUNNING = 0;
	public static final int STANDOFF = 1;
	public static final int GAMEOVER = 2;
	private long overTime;
   
	public int getGameStatu() {
		return gameStatu;
	}

	public void setGameStatu(int gameStatu) {
		this.gameStatu = gameStatu;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public MyThread(Context context, SurfaceHolder holder, int w, int h,
			int gameType) {
		this.holder = holder;
		this.context = context;
		this.gameType = gameType;
		this.w = w;
		this.h = h;
		paint = new Paint();
		isStart = true;
		// 初始化数组
		paths = new int[] { R.drawable.role1_00, R.drawable.role1_01,
				R.drawable.role1_02, R.drawable.role1_03, R.drawable.role1_04,
				R.drawable.role1_05, };
		bitmaps = new Bitmap[paths.length];
		for (int i = 0; i < paths.length; i++) {
			// 每循环一次，根据path加载对应图片
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), paths[i]);
			bitmaps[i] = bitmap;
		}
		role = new Role[gameType];
		rect = new Rect[gameType];
		gameSpan = 4 * h / (5 * gameType);
		initSpirit();
	}

	public void initSpirit() {
		// TODO Auto-generated method stub
		for (int i = 0; i < gameType; i++) {

			left = 3 * w / 2;
			int lineY = h / 10 + (i + 1) * gameSpan;
			int roleY = lineY - bitmaps[0].getHeight();
			int randomX = (int) (bitmaps[0].getWidth()
					* (Math.random() * 5 + 1) / 4);
			int randomY = (int) (bitmaps[0].getHeight()
					* (Math.random() * 5 + 1) / 4);

			// 创建障碍物
			Rect rect = new Rect();
			rect.left = left;
			rect.right = rect.left + randomY;

			rect.bottom = lineY;
			rect.top = rect.bottom - randomX;
			this.rect[i] = rect;
			// 创建人物
			Role role = new Role(bitmaps);
			role.setX(w / 10 + 20);
			role.setY(roleY);
			this.role[i] = role;

		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		// 执行画画任务
		while (isStart) {
			// 开始获取画布来绘制
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();// 将获取的画布绑定给画板
				if (canvas != null) {
					canvas.drawColor(Color.WHITE);// 绘制白色的背景

					// 最后确认画布OK了可以画画了
					// 说明游戏根据阶段不同所要绘制的东西不同
					switch (gameStatu) {
					case RUNNING:
						// 正常运行
						drawRunningView(canvas);
						break;
					case STANDOFF:
						// 僵持状态
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						// 游戏结束状态
						drawGameOverView(canvas);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {

			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}

		}
	}

	private void drawRunningView(Canvas canvas) {

		for (int i = 0; i < gameType; i++) {
			// 第一个i的线的距离
			int lineY = h / 10 + (i + 1) * gameSpan;
			int roleY = lineY - bitmaps[0].getHeight();
			// 障碍物从右往左移
			rect[i].left = rect[i].left - h / 150;
			rect[i].right = rect[i].right - h / 150;
			if (rect[i].left < 0) {
				int randomX = (int) (bitmaps[0].getWidth()
						* (Math.random() * 5 + 1) / 4);
				int randomY = (int) (bitmaps[0].getHeight()
						* (Math.random() * 5 + 1) / 4);
				// 创建障碍物
				rect[i].left = left;
				rect[i].right = rect[i].left + randomY;
				rect[i].top = rect[i].bottom - randomX;

			}
			// 向下移的方法
			role[i].setSpeedY(role[i].getSpeedY() + h / 800f);
			role[i].setY(role[i].getY() + role[i].getSpeedY());
			// 判断不能超过线的距离
			if (role[i].getY() + role[i].getHeight() >= lineY) {
				role[i].setJump(false);
				role[i].setY(roleY);
			}
			if (rect[i].intersect(role[i].getRolerect())) {
				gameStatu = STANDOFF;
				overTime = System.currentTimeMillis();
			}

			role[i].drawSelf(canvas);
			canvas.drawLine(0, lineY, w, lineY, paint);
			// 绘制一个障碍物
			canvas.drawRect(rect[i], paint);
		}

	}

	/**
	 * 游戏僵持状态
	 * 
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		if (System.currentTimeMillis() - overTime >= 1000) {
              gameStatu=GAMEOVER;
		} else {
			for (int i = 0; i < gameType; i++) {
				// 第一个i的线的距离
				int lineY = h / 10 + (i + 1) * gameSpan;
				// 画线
				canvas.drawLine(0, lineY, w, lineY, paint);
				// 画一个人
				this.role[i].drawSelf(canvas);
				// 绘制一个障碍物
				canvas.drawRect(this.rect[i], paint);
			}
		}
	}

	/**
	 * 游戏结束
	 * 
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		// TODO Auto-generated method stub
        canvas.drawColor(Color.RED);
		//绘制文字
		String [] modes = new String[]{
				"普通","噩梦","地狱","炼狱"
		};
		String back="返回";
		String reset="重来";
		//绘制3段文本
		drawText(canvas, modes[gameType-2], h/2,28);
		drawText(canvas, back, h*4/6,20);
		drawText(canvas, reset, h*5/6,20);
		
	}
	public void drawText(Canvas canvas,String text,float y,int textSize){
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);//文本的宽度
		canvas.drawText(text, (w-measureText)/2, y, paint);
	}
}
