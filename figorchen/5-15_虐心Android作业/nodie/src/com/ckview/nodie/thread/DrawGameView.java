package com.ckview.nodie.thread;

import com.ckview.nodie.R;
import com.ckview.nodie.spirit.Runner;
import com.ckview.utils.StaticValue;
import com.ckview.utils.log.BaseLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class DrawGameView extends Thread {
	private int gameType;
	private SurfaceHolder surFaceHolder;
	private Canvas canvas;
	private Paint paint;
	private Context context;
	private int width;
	private int height;
	private Boolean isStart;
	private Bitmap[] bitmaps;
	private Bitmap bitmapDie;
	private Runner[] runners;
	private Rect[] obstacles;
	private float[] linesY;
	private final float TOPOFFSETSCALE = 0.1f;
	private final float LEFTOFFSETSCALE = 0.1f;
	private float span;
	private int gameMode;
	private Rect tempRect;
	private long deadTime;
	private long startTime;
	private float canvasRealHeight;
	private boolean isHack;
	private String hackText;
	/**
	 * @return the hackText
	 */
	public String getHackText() {
		return hackText;
	}

	/**
	 * @param hackText the hackText to set
	 */
	public void setHackText(String hackText) {
		this.hackText = hackText;
	}

	/**
	 * @return the isHack
	 */
	public boolean isHack() {
		return isHack;
	}

	/**
	 * @param isHack the isHack to set
	 */
	public void setHack(boolean isHack) {
		this.isHack = isHack;
	}
	/**
	 * @return the isStart
	 */
	public Boolean getIsStart() {
		return isStart;
	}

	/**
	 * @param isStart the isStart to set
	 */
	public void setIsStart(Boolean isStart) {
		this.isStart = isStart;
	}
	/**
	 * @return the gameMode
	 */
	public int getGameMode() {
		return gameMode;
	}

	/**
	 * @param gameMode the gameMode to set
	 */
	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public DrawGameView(Context context, int gametype, SurfaceHolder surFaceHolder, int width, int height){
		this.context = context;
		this.gameType = gametype;
		this.surFaceHolder = surFaceHolder;
		this.width = width;
		this.height = height;
		this.isHack = false;
		bitmaps = new Bitmap[]{
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_00),
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_01),
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_02),
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_03),
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_04),
			BitmapFactory.decodeResource(context.getResources(), R.drawable.role1_05)
		};
		paint = new Paint();
		isStart = true;
		canvasRealHeight = height*(1 - TOPOFFSETSCALE*2);
		span = canvasRealHeight/gameType;
		tempRect = new Rect();
		this.initDrawGame();
	}
	
	public void initDrawGame(){
		hackText = StaticValue.NOHACK;
		//set game mode
		gameMode = StaticValue.RUNNING;
		
		// create lines
		linesY = new float[gameType];
		for(int i = 0; i< gameType; i++){
			linesY[i] = span*(i + 1) + height*TOPOFFSETSCALE;
		}
		
		// create runner instance
		bitmapDie = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_die);
		runners = new Runner[gameType];
		for(int i = 0; i< gameType; i++){
			runners[i] = new Runner(bitmaps, bitmapDie);
			runners[i].set(width*LEFTOFFSETSCALE, linesY[i] - runners[i].getHeight());
		}
		
		// create obstacles
		obstacles = new Rect[gameType];
		for(int i = 0; i< gameType; i++){
			obstacles[i] = new Rect((int)(1.5*width), (int)(linesY[i] - ((Math.random()*1.3+0.2)*runners[i].getHeight())), (int)((Math.random()*0.8+0.2)*runners[i].getWidth()), (int)linesY[i]);
		}
		isStart = true;
		// start time
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		super.run();
		while(isStart){
			try {
				canvas = surFaceHolder.lockCanvas();
				if(canvas != null){
					// fill white in background
					canvas.drawColor(Color.WHITE);
					paint.setColor(Color.BLACK);
					paint.setStrokeWidth(5);
					paint.setTextSize(40);
					switch (gameMode) {
						case StaticValue.RUNNING:
							drawRunning(canvas);
							break;
							
						case StaticValue.DEAD:
							drawDead(canvas);
							break;
							
						case StaticValue.OVER:
							drawOver(canvas);
							break;
	
						default:
							break;
					}
					
				}
			} catch (Exception e) {
				BaseLog.info("INFO", "fiald to lock canvas: "+e);
			} finally {
				surFaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	/**
	 * draw running view
	 * @param canvas
	 */
	private void drawRunning(Canvas canvas) {
		for(int i = 0; i <gameType; i++){
			//move obstacle
			obstacles[i].left -= 5;
			obstacles[i].right -= 5;
			if(obstacles[i].right <= 0){
				obstacles[i].left = (int)((Math.random()*0.5+1)*width);
				obstacles[i].right = (int)(obstacles[i].left + ((Math.random()*0.8+0.2)*runners[i].getWidth()));
				obstacles[i].top = (int)(linesY[i] - ((Math.random()*1.3+0.2)*runners[i].getHeight()));
			}
			
			//change y with speed
			runners[i].setSpeedY(runners[i].getSpeedY() + runners[i].getHeight()/80);
			runners[i].setY(runners[i].getY() + runners[i].getSpeedY());
			if(runners[i].getY() > linesY[i] - runners[i].getHeight()){
				runners[i].setY(linesY[i] - runners[i].getHeight());
				runners[i].setSpeedY(0);
				runners[i].setJupm(false);
			}
			
			// hack mode
			if(isHack && !runners[i].isJupm()){
				if(obstacles[i].left - runners[i].getX() <= 120 && obstacles[i].left - runners[i].getX() > 0){
					runners[i].setSpeedY(-runners[i].getHeight()/4);
					runners[i].setJupm(true);
				}
				
			}
			
			tempRect.set(obstacles[i]);
			if(tempRect.intersect(runners[i].getRect())){
				// runner is dead, change game mode to dead
				gameMode = StaticValue.DEAD;
				// set runner is dead
				runners[i].setDie(true);
				//move back obstacle to right
				obstacles[i].left += 5;
				obstacles[i].right += 5;
				deadTime = System.currentTimeMillis();
				break;
			}else{
				// draw line
				canvas.drawLine(0, linesY[i], width, linesY[i], paint);
				// draw runner
				runners[i].drawSelf(canvas);
				// draw obstacle
				canvas.drawRect(obstacles[i], paint);
				// draw score
				String text = String.valueOf((System.currentTimeMillis() - startTime)/1000f)+"秒";
				float x = width - paint.measureText(text) -20;
				float y = height*TOPOFFSETSCALE/2;
				drawTextCenter(x, y, text, false);
				// draw hack text
				float hackTextY = height*(1 - TOPOFFSETSCALE/2);
				drawTextCenter(hackTextY, hackText, false);
			}
		}		
	}
	
	/**
	 * draw dead view
	 * @param canvas
	 */
	private void drawDead(Canvas canvas) {
		if(System.currentTimeMillis() - deadTime > 1500){
			gameMode = StaticValue.OVER;
			return;
		}
		for(int i = 0; i <gameType; i++){
			// draw line
			canvas.drawLine(0, linesY[i], width, linesY[i], paint);
			// draw runner
			runners[i].drawSelf(canvas);
			// draw obstacle
			canvas.drawRect(obstacles[i], paint);
			// draw score
			String text = String.valueOf((deadTime - startTime)/1000f)+"秒";
			float x = width - paint.measureText(text) -20;
			float y = height*TOPOFFSETSCALE/2;
			drawTextCenter(x, y, text, false);
			// draw hack text
			hackText = StaticValue.TAUNT;
			float hackTextY = height*(1 - TOPOFFSETSCALE/2);
			drawTextCenter(hackTextY, hackText, false);
		}
	}
	
	/**
	 * draw over view
	 * @param canvas2
	 */
	private void drawOver(Canvas canvas) {
		float y;
		String text;
		
		// fill red in background
		canvas.drawColor(Color.RED);
		
		// draw score
		float score = (deadTime - startTime)/1000f;
		text = score+"秒";
		y = canvasRealHeight/4;
		drawTextCenter(y, text, true);
		
		// draw restart
		text = "老子不服！";
		y = canvasRealHeight/2;
		drawTextCenter(y, text, true);
		
		//draw back
		text = "返回！";
		y = canvasRealHeight*3/4;
		drawTextCenter(y, text, true);
	}

	/**
	 * draw text in center by y
	 * @param x
	 * @param y
	 * @param text
	 * @param offset
	 */
	private void drawTextCenter(float x, float relativeY, String text, boolean offSet) {
		float y;
		if(offSet){
			y = relativeY + (-paint.ascent() - paint.descent())/2 + height*TOPOFFSETSCALE;
		}else{
			y = relativeY + (-paint.ascent() - paint.descent())/2;
		}
		canvas.drawText(text, x, y, paint);
	}
	
	/**
	 * draw text in center by y. if offset is true, y of text will plus top offset.
	 * @param y
	 * @param text
	 * @param offset
	 */
	private void drawTextCenter(float relativeY, String text, boolean offSet) {
		float x = (width - paint.measureText(text))/2;
		float y;
		if(offSet){
			y = relativeY + (-paint.ascent() - paint.descent())/2 + height*TOPOFFSETSCALE;
		}else{
			y = relativeY + (-paint.ascent() - paint.descent())/2;
		}
		canvas.drawText(text, x, y, paint);
	}

	/**
	 * find area by y and make runner jump
	 * @param y
	 */
	public void runnerJump(float y) {
		for(int i = 0; i < gameType; i++){
			if(y > height*TOPOFFSETSCALE + span*i && y < linesY[i] && !runners[i].isJupm()){
				BaseLog.info("AREA", "up-----"+height*TOPOFFSETSCALE + span*i);
				BaseLog.info("AREA", "down-----"+linesY[i]);
				BaseLog.info("AREA", "y-----"+y);
				runners[i].setSpeedY(-runners[i].getHeight()/4);
				runners[i].setJupm(true);
				break;
			}
		}
	}

	public int drawGameView(float y) {
		if(y >= 0.375*canvasRealHeight + height*TOPOFFSETSCALE && y < 0.625*canvasRealHeight + height*TOPOFFSETSCALE){
			return StaticValue.RESTART;
		}else if(y >= 0.625*canvasRealHeight + height*TOPOFFSETSCALE && y < 0.875*canvasRealHeight + height*TOPOFFSETSCALE){
			return StaticValue.BACK;
		}
		return StaticValue.NOTHING;
	}

	public boolean isInHackArea(float y) {
		if(y > height*(1-TOPOFFSETSCALE) && y < height){
			return true;
		}
		return false;
	}
	
	
}
