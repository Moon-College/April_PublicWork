package com.example.nodie.runnable;

import com.example.nodie.R;
import com.example.nodie.bean.Role;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 开启线程一直进行游戏元素的绘制
 * @author Administrator
 *
 */
public class GameThread extends Thread {
	private Bitmap[] roles;   //存放精灵的不同帧的图片
	private int index;   //当前播放到第几帧
	private boolean flag;   //控制循环的开始和结束
	private SurfaceView view;   
	private Context context;   //上线文对象
	private int gameType;    //游戏的类型
	private Role role;
	
	
	
	private static int bitmaps[]={R.drawable.role1_00,
		R.drawable.role1_01,
		R.drawable.role1_02,
		R.drawable.role1_03,
		R.drawable.role1_04,
		R.drawable.role1_05};
	public GameThread(int index, SurfaceView view, Context context, int gameType) {
		super();
		this.index = index;
		this.view = view;
		this.context = context;
		this.gameType = gameType;
		flag=true;
		roles=new Bitmap[bitmaps.length];
		for(int i=0;i<bitmaps.length;i++){
			roles[i]=BitmapFactory.decodeResource(context.getResources(), bitmaps[i]);
		}
		
		role = new Role(roles);
		role.setX(200);
		role.setY(200);
	}


	@Override
	public void run() {
		super.run();
		SurfaceHolder holder=view.getHolder();
		Canvas canvas=null;

		
		while(flag){
			canvas=holder.lockCanvas();
			canvas.drawColor(Color.WHITE);//绘制白色的背景
			role.drawRole(canvas);

			holder.unlockCanvasAndPost(canvas);
		}
		
		
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
