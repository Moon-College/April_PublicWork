package com.tz.died.runable;

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
	private Role role;
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
		this.gameType=gameType;
		this.w = w;
		this.h = h;
		paint=new Paint();
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
		role = new Role(bitmaps);
		role.setX(200);
		role.setY(200);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		//执行画画任务
				while(isStart){
					//开始获取画布来绘制
					Canvas canvas = null;
					try{
						canvas = holder.lockCanvas();//将获取的画布绑定给画板
						if(canvas!=null){
							//最后确认画布OK了可以画画了
							canvas.drawColor(Color.WHITE);//绘制白色的背景
							drawRectLine(canvas);
						}
					}catch(Exception e){
						
					}finally{
						if(canvas!=null){
							holder.unlockCanvasAndPost(canvas);
						}
					}
			
		}
	}

	private void drawRectLine(Canvas canvas) {
		// TODO Auto-generated method stub
		int gameSpan=4*h/(5*gameType);
		for(int i=0;i<gameType;i++){
			int lineY=h/10+(i+1)*gameSpan;
			int roleY=lineY-bitmaps[0].getHeight();
			role.setX(h/10+20);
			role.setY(roleY);
			role.drawSelf(canvas);
			canvas.drawLine(0, lineY, w, lineY, paint);
		}
		
	}
}
