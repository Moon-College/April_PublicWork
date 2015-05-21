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
 * �����߳�һֱ������ϷԪ�صĻ���
 * @author Administrator
 *
 */
public class GameThread extends Thread {
	private Bitmap[] rolebitmaps;   //��ž���Ĳ�ͬ֡��ͼƬ
	private boolean flag;   //����ѭ���Ŀ�ʼ�ͽ���
	private Context context;   //�����Ķ���
	private int gameType;    //��Ϸ������
	public Role[] roles;
	private int areaSpan;    //��Ϸ����֮��ľ���
	private int w,h;    //��Ϸ����ĸ߶ȺͿ��
	private SurfaceHolder holder;
	private int gameStatus=0;     //��Ϸ������״̬
	public  final static int RUNNING=0;  //����״̬
	public final static int STANDOFF=1;   //����(��ײ)״̬
	public final static int GAMEOVER=2;   //��Ϸ����״̬
	private Rect[] rects;   //�ϰ������Ŀ
	private String[] gameMenuText={"��ͨ","ج��","����","����"};
	private long startTime;   //��Ϸ��ʼ��ʱ��
	private long endTime;     //��Ϸ������ʱ��
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
		areaSpan=4*h/(5*gameType);  //ÿ����Ϸ����ĸ߶�
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
		//ִ�л�������
		while(flag){
			//��ʼ��ȡ����������
			Canvas canvas = null;
			try{
				canvas = holder.lockCanvas();//����ȡ�Ļ����󶨸�����
				if(canvas!=null){
					//���ȷ�ϻ���OK�˿��Ի�����
					//˵����Ϸ���ݽ׶β�ͬ��Ҫ���ƵĶ�����ͬ
					switch (gameStatus) {
					case RUNNING:
						//��������
						drawRunningView(canvas);
						break;
					case STANDOFF:
						//����״̬
						drawStandOffView(canvas);
						break;
					case GAMEOVER:
						//��Ϸ����״̬
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
	 * ��Ϸ����ʱ���״̬
	 * @param canvas
	 */
	private void drawGameOverView(Canvas canvas) {
		canvas.drawColor(Color.RED);
		Paint paint=new Paint();
		paint.setStrokeWidth(5);
		String score=gameMenuText[gameType-2]+":"+(endTime-startTime)/1000+"\'"+(endTime-startTime)%1000+"\"";
		String back = "����";
		String reStart = "����";
		//����3���ı�
		drawText(canvas, score, h/2,30,paint);
		drawText(canvas, back, 4*h/6, 30,paint);
		drawText(canvas, reStart, 5*h/6,30,paint);
		
	}
	
	public void drawText(Canvas canvas,String text,float y,int textSize,Paint paint){
		paint.setTextSize(textSize);
		float measureText = paint.measureText(text);//�ı��Ŀ��
		canvas.drawText(text, (w-measureText)/2, y, paint);
	}

	/**
	 * ������ϰ�����ײ��ʱ��ִ�еķ���
	 * @param canvas
	 */
	private void drawStandOffView(Canvas canvas) {
		Paint paint=new Paint();
		paint.setTextSize(30);
		
		if(System.currentTimeMillis()-endTime >2000){
			gameStatus=GAMEOVER;    //��Ϸ����
			
		}else{
			canvas.drawColor(Color.WHITE);
			
			for(int i=0;i<gameType;i++){
				int lineY=h/10+areaSpan*(i+1);
				//����
				canvas.drawLine(0, lineY, w, lineY, paint);

				//���ϰ���
				canvas.drawRect(rects[i], paint);
				//������
				roles[i].drawRole(canvas);
			}
		}
		String score=(endTime-startTime)/1000+"\'"+(endTime-startTime)%1000+"\"";
		float  score_w = paint.measureText(score);
		canvas.drawText(score, w-score_w, -paint.ascent(), paint);
	}

	/**
	 * ��Ϸ�������е�״̬
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
			
			//ģ�������µļ��ٶ�
			roles[i].setSpeedY(roles[i].getSpeedY()+h/800f);
			roles[i].setY(roles[i].getY()+roles[i].getSpeedY());
			//����һֱ���µ�
			if(roles[i].getY()+roles[i].getHeight()>=lineY){
				//˵�������
				roles[i].setY(lineY-roles[i].getHeight());//����Y�����Ա�վ������
				roles[i].setSpeedY(0);//���ٶȹ���
				roles[i].setJump(false);
			}
			//����
			canvas.drawLine(0, lineY, w, lineY, paint);

			rects[i].left=rects[i].left-h/150;
			rects[i].right=rects[i].right-h/150;
			if(roles[i].getRoleRect().intersect(rects[i])){
				gameStatus=STANDOFF;
				endTime=System.currentTimeMillis();
			}
			if(rects[i].right<=0){
				//����Ŀ�ߣ���1/4�����~6/4�����
				int random_w = (int) (roles[i].getWidth()*(Math.random()*5+1)/4);
				int random_h = (int) (roles[i].getHeight()*(Math.random()*5+1)/4);
				int start_w=(int) (w*(new Random().nextFloat()/2));
				rects[i].left=3*w/2+start_w;
				rects[i].right=rects[i].left+random_w;
				rects[i].bottom=lineY;
				rects[i].top=rects[i].bottom-random_h;
			}
			
			//���ϰ���
			canvas.drawRect(rects[i], paint);
			//������
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
