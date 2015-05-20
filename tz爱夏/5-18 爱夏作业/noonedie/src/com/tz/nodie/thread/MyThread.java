package com.tz.nodie.thread;

import java.util.Random;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.tz.nodie.R;
import com.tz.nodie.entity.Role;

public class MyThread extends Thread {
	public static final int RUNNING = 0;
	public static final int STANDOFF = 1;
	public static final int GAMEOVER = 2;
	private int status = 0;
	private SurfaceHolder surfaceHolder;
	private Context context;
	private int w, h; // 画板的宽高
	private int gameType;
	private Paint paint;
	private boolean isStart;
	private Bitmap[] bits;
	public Role[] roles;
	private Rect[] rects;
	private int[] paths;
	private float lineSpan; // 游戏区域两线之间的距离
	private float lineY;
	private long overTime;
	private long startTime;

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public MyThread(SurfaceHolder surfaceHolder, Context context, int w, int h,
			int gameType) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		this.w = w;
		this.h = h;
		this.gameType = gameType;
		paint = new Paint();
		isStart = true;
		// 初始化图片
		paths = new int[] { R.drawable.role1_00, R.drawable.role1_01,
				R.drawable.role1_02, R.drawable.role1_03, R.drawable.role1_04,
				R.drawable.role1_05 };
		bits = new Bitmap[paths.length];
		for (int i = 0; i < paths.length; i++) {
			bits[i] = BitmapFactory.decodeResource(context.getResources(),
					paths[i]);
		}
		lineSpan = (h * 4 / 5) / gameType;
		roles = new Role[gameType];
		rects = new Rect[gameType];
		initRole();
	}

	/**
	 * init game data and set the init attrs to them
	 * 
	 * @author jackie
	 */
	public void initRole() {
		startTime = System.currentTimeMillis();
		for (int i = 0; i < gameType; i++) {
			// 创建人物
			Role role = new Role(bits);
			lineY = h / 10 + (i + 1) * lineSpan;
			role.setX(w / 8);
			role.setY(lineY - bits[i].getHeight());
			roles[i] = role;
			// 创建障碍物
			Rect rect = new Rect();
			rect.left = 3 * w / 2;
			rect.top = (int) (lineY - bits[i].getHeight()
					* (Math.random() * 5 + 1) / 4);
			rect.right = (int) (bits[i].getWidth() * (Math.random() * 5 + 1)
					/ 4 + 3 * w / 2);
			rect.bottom = (int) lineY;
			rects[i] = rect;
		}
	}

	@Override
	public void run() {
		super.run();
		// 是否开始画画
		while (isStart) {
			Canvas canvas = null;
			try {
				// 将获取的画布绑定给画板
				canvas = surfaceHolder.lockCanvas();
				if (canvas != null) {
					// 确认画布不为空才开始画
					// 分三个场景画画
					switch (status) {
					case RUNNING:
						drawRunningView(canvas);
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
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	private void drawGameOverView(Canvas canvas) {
		// 游戏结束
		canvas.drawColor(Color.RED);
		// 绘制文本
		String[] modes = new String[] { "普通", "噩梦", "地狱", "炼狱" };
		String mode = modes[gameType - 2];
		String scoreText = mode + " : " + (overTime - startTime) / 1000f + "\'";
		String back = "返回";
		String restart = "重来";
		drawText(canvas, scoreText, h / 2, 50);
		drawText(canvas, back, 4 * h / 6, 40);
		drawText(canvas, restart, 5 * h / 6, 40);
	}

	private void drawText(Canvas canvas, String text, float textY, int textSize) {
		// 文本的横坐标都居中显示
		float textX = (w - paint.measureText(text)) / 2;
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		canvas.drawText(text, textX, textY, paint);
	}

	private void drawStandOffView(Canvas canvas) {
		if (System.currentTimeMillis() - overTime > 1000) {
			// 进入gameOver模式
			status = GAMEOVER;
		} else {
			// 游戏僵化
			for (int i = 0; i < gameType; i++) {

				float lineY = h / 10 + (i + 1) * lineSpan;
				// 画线
				canvas.drawLine(0, lineY, w, lineY, paint);
				// 画人
				roles[i].drawRole(canvas);
				// 画障碍物
				canvas.drawRect(rects[i], paint);
			}
		}
	}

	private void drawRunningView(Canvas canvas) {
		// 初始化游戏数据
		paint.setStrokeWidth(5);
		canvas.drawColor(Color.WHITE);
		//右上角画分数
		String score=(System.currentTimeMillis()-startTime)/1000f+"\'";
		float scoreX=w-paint.measureText(score);
        float scoreY=-paint.ascent();
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        canvas.drawText(score, scoreX, scoreY, paint);
        
        //底部中间画签名
        String name="Jackie";
        float nameX=(w-paint.measureText(name))/2;
        float nameY=(h/10-paint.descent()+paint.ascent())/2+9*h/10-paint.ascent();
		canvas.drawText(name, nameX, nameY, paint);
        
        for (int i = 0; i < gameType; i++) {

			float lineY = h / 10 + (i + 1) * lineSpan;
			// 模拟人的加速度
			roles[i].setSpeedY(roles[i].getSpeedY() + h / 800f);
			roles[i].setY(roles[i].getY() + roles[i].getSpeedY());
			if (roles[i].getY() + roles[i].getHeight() >= lineY) {
				roles[i].setY(lineY - roles[i].getHeight());
				roles[i].setSpeedY(0);
				roles[i].setJump(false);
			}

			// 让障碍物从右往左移动
			rects[i].left = rects[i].left - h / 100;
			rects[i].right = rects[i].right - h / 100;
			// 让障碍物循环的画
			if (rects[i].right <= 0) {
				// 障碍物随机的左边坐标
				int random_left = (int) (w * (new Random().nextFloat() / 2));
				rects[i].left = 3 * w / 2 + random_left;
				rects[i].right = (int) (bits[i].getWidth()
						* (Math.random() * 5 + 1) / 4 + rects[i].left);
			}

			if (rects[i].intersect(roles[i].getRectByRole())) {
				// 说明人与障碍物相交，进入僵持模式
				status = STANDOFF;
				overTime = System.currentTimeMillis();
			}
			
			// 画线
			canvas.drawLine(0, lineY, w, lineY, paint);
			// 画人
			roles[i].drawRole(canvas);
			// 画障碍物
			canvas.drawRect(rects[i], paint);
			
		}
	}

}
