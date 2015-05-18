package com.tz.nodie.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.tz.nodie.R;
import com.tz.nodie.entity.Role;

public class MyThread extends Thread {
	private SurfaceHolder surfaceHolder;
	private Context context;
	private int w, h; // 画板的宽高
	private int imageType;
	private Paint paint;
	private boolean isStart;
	private Bitmap[] bits;
	private Role role;
	private int[] paths;

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public MyThread(SurfaceHolder surfaceHolder, Context context, int w, int h,
			int imageType) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		this.w = w;
		this.h = h;
		this.imageType = imageType;
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

		role = new Role(bits);
		role.setX(200);
		role.setY(200);
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
					canvas.drawColor(Color.WHITE);
					role.drawRole(canvas);
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
}
