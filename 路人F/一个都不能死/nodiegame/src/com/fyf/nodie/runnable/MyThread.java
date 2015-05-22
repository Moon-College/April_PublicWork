package com.fyf.nodie.runnable;

import com.fyf.nodie.R;
import com.fyf.nodie.people.Role;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread{
	private SurfaceHolder holder;
	private Context context;
	private int w,h;
	private int difficultMode;
	private boolean isStart;   //控制是否绘制
	private Bitmap[] bms;
	private int [] paths;
	private boolean isPause;
	Paint paint;
	private Role role; 
	public MyThread(Context context,SurfaceHolder holder,int w,int h,int difficultMode){
		this.context = context;
		this.holder = holder;
		this.w = w;
		this.h = h;
		this.difficultMode = difficultMode;
		paint = new Paint();
		isStart = true;
		//初始化数组
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
		role = new Role(bms,200,200);
//		role.setX(200);
//		role.setY(200);
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
						canvas.drawColor(Color.WHITE);
						canvas.drawText("测试", 100, 100, paint);
						role.drawSelf(canvas);//绘制role
					}
				} catch (Exception e) {
				
				}finally{
					if(canvas!=null){
						holder.unlockCanvasAndPost(canvas);
					}
				}			
		}
	}
	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
}
