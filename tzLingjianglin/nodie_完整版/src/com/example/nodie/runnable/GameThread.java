package com.example.nodie.runnable;

import java.util.Random;

import com.example.nodie.R;
import com.example.nodie.bean.Role;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * 开启线程一直进行游戏元素的绘制
 * @author Administrator
 *
 */
public class GameThread extends Thread {
	private Bitmap[] rolebitmaps;   //存放精灵的不同帧的图片
	private boolean flag;   //控制循环的开始和结束
	private Context context;   //上线文对象
	private int gameType;    //游戏的类型
	public Role[] roles;
	private int areaSpan;    //游戏区域之间的距离
	private int w,h;    //游戏区域的高度和宽度
	private SurfaceHolder holder;
	private int gameStatus=0;     //游戏的运行状态
	public  final static int RUNNING=0;  //运行状态
	public final static int STANDOFF=1;   //阻塞(碰撞)状态
	public final static int GAMEOVER=2;   //游戏结束状态
	private Rect[] rects;   //障碍物的数目
	private String[] gameMenuText={"普通","噩梦","地狱","炼狱"};
	private long startTime;   //游戏开始的时间
	private long endTime;     //游戏结束的时间
	private static int bitmaps[]={R.drawable.role1_00,
		R.drawable.role1_01,
		R.drawable.role1_02,
		R.drawable.role1_03,
		R.drawable.role1_04,
		R.drawable.role1_05};
	public GameThread( SurfaceHolder holder, Context context, int gameType,int w,int h) {
		super();
		this.holder = holder;
		this.context = context;
		this.gameType = gameType;
		this.w=w;
		this.h=h;
		flag=true;
		rolebitmaps=new Bitmap[bitmaps.length];
		for(int i=0;i<bitmaps.length;i++){
			rolebitmaps[i]=BitmapFactory.decodeResource(context.getResources(), bitmaps[i]);
		}
		areaSpan=4*h/(5*gameType);  //每个游戏区域的高度
		roles=new Role[gameType];	
		
		rects=new Rect[gameType];
		initRole();
	}

	public void initRole(){
		for(int i=0;i<gameType;i++){
			int lineY=h/10+areaSpan*(i+1);
			Role role=new Role(rolebitmaps);
			role.setX(w/8);
			role.setY(lineY-role.getHeight());
			roles[i]=role;
			
			Rect rect=new Rect();
			int random_w = (int) (role.getWidth()*(Math.random()*5+1)/4);
			int random_h = (int) (role.getHeight()*(Math.random()*5+1)/4);
			
			rect.left=3*w/2;
			rect.right=rect.left+random_w;
			rect.bottom=lineY;
			rect.top=rect.bottom-random_h;
			rects[i]=rect;
		}
		startTime=System.currentTimeMillis();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		//执行画画任务
		while(flag){
			//开始获取画布来绘制
			Canvas canvas = null;
			try{
				canvas = holder.lockCanvas();//将获取的画布绑定给画板
				if(canvas!=null){
					//最后确认画布OK了可以画画了
					//说明游戏根据阶段不同所要绘制的东西不同
					switch (gameStatus) {
					case RUNNING:
						//正常运行
						drawRunningView(canvas);
						break;
					case STANDOFF:
						//僵持状态
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						//游戏结束状态
						drawGameOverView(canvas);
						break;
					default:
						break;
					}
				}
			}catch(Exception e){
				
			}finally{
				if(canvas!=null){
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	/**
	 * 游戏结束时候的状态
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		canvas.drawColor(Color.RED);
		Paint paint=new Paint();
		paint.setStrokeWidth(5);
		String score=gameMenuText[gameType-2]+":"+(endTime-startTime)/1000+"\'"+(endTime-startTime)%1000+"\"";
		String back = "返回";
		String reStart = "重来";
		//绘制3段文本
		drawText(canvas, score, h/2,30,paint);
		drawText(canvas, back, 4*h/6, 30,paint);
		drawText(canvas, reStart, 5*h/6,30,paint);
		
	}
	
	public void drawText(Canvas canvas,String text,float y,int textSize,Paint paint){
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);//文本的宽度
		canvas.drawText(text, (w-measureText)/2, y, paint);
	}

	/**
	 * 精灵和障碍物碰撞的时候执行的方法
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		Paint paint=new Paint();
		paint.setTextSize(30);
		
		if(System.currentTimeMillis()-endTime >2000){
			gameStatus=GAMEOVER;    //游戏结束
			
		}else{
			canvas.drawColor(Color.WHITE);
			
			for(int i=0;i<gameType;i++){
				int lineY=h/10+areaSpan*(i+1);
				//画线
				canvas.drawLine(0, lineY, w, lineY, paint);

				//画障碍物
				canvas.drawRect(rects[i], paint);
				//画人物
				roles[i].drawRole(canvas);
			}
		}
		String score=(endTime-startTime)/1000+"\'"+(endTime-startTime)%1000+"\"";
		float  score_w = paint.measureText(score);
		canvas.drawText(score, w-score_w, -paint.ascent(), paint);
	}

	/**
	 * 游戏正在运行的状态
	 * @param canvas
	 */
	private void drawRunningView(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint=new Paint();
		paint.setStrokeWidth(5);
		paint.setTextSize(30);
		long end=System.currentTimeMillis();
		String score=(end-startTime)/1000+"\'"+(end-startTime)%1000+"\"";
		float  score_w = paint.measureText(score);
		canvas.drawText(score, w-score_w, -paint.ascent(), paint);
		for(int i=0;i<gameType;i++){
			int lineY=h/10+areaSpan*(i+1);
			
			//模拟人向下的加速度
			roles[i].setSpeedY(roles[i].getSpeedY()+h/800f);
			roles[i].setY(roles[i].getY()+roles[i].getSpeedY());
			//不能一直往下掉
			if(roles[i].getY()+roles[i].getHeight()>=lineY){
				//说明落地了
				roles[i].setY(lineY-roles[i].getHeight());//设置Y坐标以便站在线上
				roles[i].setSpeedY(0);//让速度归零
				roles[i].setJump(false);
			}
			//画线
			canvas.drawLine(0, lineY, w, lineY, paint);

			rects[i].left=rects[i].left-h/150;
			rects[i].right=rects[i].right-h/150;
			if(roles[i].getRoleRect().intersect(rects[i])){
				gameStatus=STANDOFF;
				endTime=System.currentTimeMillis();
			}
			if(rects[i].right<=0){
				//随机的宽高：宽1/4人物宽~6/4人物宽
				int random_w = (int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				int random_h = (int) (roles[i].getHeight()*(Math.random()*5+1)/4);
				int start_w=(int) (w*(new Random().nextFloat()/2));
				rects[i].left=3*w/2+start_w;
				rects[i].right=rects[i].left+random_w;
				rects[i].bottom=lineY;
				rects[i].top=rects[i].bottom-random_h;
			}
			
			//画障碍物
			canvas.drawRect(rects[i], paint);
			//画人物
			roles[i].drawRole(canvas);
		}
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
}
