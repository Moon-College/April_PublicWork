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
	// ��Ϸ��״̬
	private int gameStatu = 0;
	private int gameSpan;
	// �ߵ�����
	public Rect[] rects;
	// ���������
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
		// ÿ����Ϸ֮��Ŀ��
		gameSpan = h * 4 / 5 / gameType;
		rects = new Rect[gameType];
		roles = new Role[gameType];
		initSpirit();
	}

	// ��ʼ����Ϸ�����Ԫ��
	public void initSpirit() {
		startTime = System.currentTimeMillis();
		// ��������
		for (int i = 0; i < gameType; i++) {
			// ��i���ߵ�Y����
			int lineY = h / 10 + (i + 1) * gameSpan;
			// ��������
			Role role = new Role(bitmaps);// ����һ��role
			role.setX(w / 10);
			role.setY(lineY - role.getHeight());
			roles[i] = role;
			// �����ϰ���
			Rect rect = new Rect();
			// ����Ŀ�ߣ���1/4�����~6/4�����
			int random_w = (int) (role.getWidth() * (Math.random() * 5 + 1) / 4);
			int random_h = (int) (role.getHeight() * (Math.random() * 5 + 1) / 4);
			rect.left = 3 * w / 2;// �ϰ�������
			rect.right = rect.left + random_w;// �ϰ�����ұ�
			rect.bottom = lineY;
			rect.top = rect.bottom - random_h;
			// ��ӵ�����
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
				// �õ�����
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
					// �󶨻���ͱ���Ҫ��������Ȼ�ᱨ��
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	/**
	 * ������Ϸ��ʱ��
	 * 
	 * @param canvas
	 */
	private void gameRunning(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		// role.drawSelf(canvas);
		// �������Ͻǵĵ÷��ı�
		paint.setTextSize(20);
		String score = (System.currentTimeMillis() - startTime) / 1000f + "\'";
		float score_w = paint.measureText(score);
		canvas.drawText(score, w - score_w, -paint.ascent(), paint);
		
		//���ײ�������logo
		String logo = "һ����������-�Ǻ�";
		float logo_y = 9*h/10+(h/10 -paint.descent()+paint.ascent())/2-paint.ascent();
		drawText(canvas, logo, logo_y, 20);
		
		for (int i = 0; i < gameType; i++) {
			int lineY = h / 10 + (i + 1) * gameSpan;

			// �ϰ���һֱ���ƶ�
			rects[i].left = rects[i].left + (-h / 150);
			rects[i].right = rects[i].right + (-h / 150);
			if (rects[i].left <= 0) {
				int random_w = (int) (role.getWidth() * (Math.random() * 5 + 1) / 4);
				int random_h = (int) (role.getHeight()* (Math.random() * 5 + 1) / 4);
				int random_start = (int) (w * (new Random().nextFloat() / 2));
				// ��������
				rects[i].left = 3 * w / 2 + random_start;
				rects[i].right = rects[i].left + random_w;
				rects[i].top = rects[i].bottom - random_h;
			}

			// ģ�������µļ��ٶ�
			roles[i].setSpeedY(roles[i].getSpeedY() + h / 800f);
			roles[i].setY(roles[i].getY() + roles[i].getSpeedY());
			// ��������˽����ߵĸ߶Ⱦ�ֹͣ����
			if (roles[i].getY() + roles[i].getHeight() >= lineY) {
				roles[i].setSpeedY(0);
				roles[i].setY(lineY - roles[i].getHeight());
				roles[i].setJump(false);
			}
			if (rects[i].intersect(roles[i].getRectFromRole())) {
				// ������
				// ���뽩��״̬
				gameStatu = Constant.STANDOFF;
				overTime = System.currentTimeMillis();
			}

			// ����
			canvas.drawLine(0, lineY, w, lineY, paint);
			// ��һ����
			roles[i].drawSelf(canvas);
			// ����һ���ϰ���
			canvas.drawRect(rects[i], paint);
		}

	}

	private void gameOver(Canvas canvas) {
		// ��Ϸ������
		canvas.drawColor(Color.RED);
		// ��������
		String[] modes = new String[] { "��ͨ", "ج��", "����", "����" };
		String mode = modes[gameType - 2];
		String scoreType = mode + ":" + (overTime - startTime) / 1000f + "\'";
		String back = "����";
		String reStart = "����";
		// ����3���ı�
		drawText(canvas, scoreType, h / 2, 28);
		drawText(canvas, back, 4 * h / 6, 24);
		drawText(canvas, reStart, 5 * h / 6, 24);

	}

	/**
	 * ��Ϸ����
	 * 
	 * @param canvas
	 */
	private void gameStanoff(Canvas canvas) {
		if (System.currentTimeMillis() - overTime >= 1000) {
			// ��ϷgameOver
			gameStatu = Constant.GAMEOVER;
		} else {
			for (int i = 0; i < gameType; i++) {
				// ��i���ߵ�Y����
				int lineY = h / 10 + (i + 1) * gameSpan;
				// ����
				canvas.drawLine(0, lineY, w, lineY, paint);
				// ��һ����
				roles[i].drawSelf(canvas);
				// ����һ���ϰ���
				canvas.drawRect(rects[i], paint);
			}
		}

	}
/**
 * �����ı���
 * @param canvas
 * @param text
 * @param y
 * @param textSize
 */
	public void drawText(Canvas canvas, String text, float y, int textSize) {
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);// �ı��Ŀ��
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
