package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class MyThread extends Thread {
	private boolean isStart;
	private SurfaceHolder holder;
	private Context context;
	private int w, h;
	private Paint paint;
	private int gameType;
	private Bitmap[] bms;
	private int[] path;
	private Role role;
	private boolean isPause;

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public MyThread(Context context, SurfaceHolder holder, int w, int h,
			int gameType) {
		this.holder = holder;
		this.context = context;
		this.w = w;
		this.h = h;
		this.gameType = gameType;
		paint = new Paint();
		isStart = true;
		path = new int[] { R.drawable.role1_00, R.drawable.role1_01,
				R.drawable.role1_02, R.drawable.role1_03, R.drawable.role1_04,
				R.drawable.role1_05 };
		bms = new Bitmap[path.length];
		for (int i = 0; i < path.length; i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), path[i]);
			bms[i] = bitmap;
		}
		role = new Role(bms);
		role.setX(200);
		role.setY(200);

	}

	@Override
	public void run() {
		super.run();
		while (isStart) {
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					canvas.drawColor(Color.WHITE);
					role.drawSelf(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

}
