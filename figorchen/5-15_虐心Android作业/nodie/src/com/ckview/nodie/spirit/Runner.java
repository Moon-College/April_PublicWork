package com.ckview.nodie.spirit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Runner {
	private float x;
	private float y;
	private float speedX;
	private float speedY;
	private float width;
	private float height;
	private Bitmap bitmapRun;
	private Bitmap bitmapDie;
	private Bitmap[] bitmaps;
	private long lastTime;
	private int index;
	private boolean isDie = false;
	private boolean isJupm;
	
	/**
	 * @return the isJupm
	 */
	public boolean isJupm() {
		return isJupm;
	}
	/**
	 * @param isJupm the isJupm to set
	 */
	public void setJupm(boolean isJupm) {
		this.isJupm = isJupm;
	}
	
	/**
	 * @return the isDie
	 */
	public boolean isDie() {
		return isDie;
	}
	/**
	 * @param isDie the isDie to set
	 */
	public void setDie(boolean isDie) {
		this.isDie = isDie;
	}
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}
	/**
	 * @return the speedX
	 */
	public float getSpeedX() {
		return speedX;
	}
	/**
	 * @return the speedY
	 */
	public float getSpeedY() {
		return speedY;
	}
	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * @return the bitmapRun
	 */
	public Bitmap getBitmapRun() {
		return bitmapRun;
	}
	/**
	 * @return the bitmapDie
	 */
	public Bitmap getBitmapDie() {
		return bitmapDie;
	}
	/**
	 * @return the bitmaps
	 */
	public Bitmap[] getBitmaps() {
		return bitmaps;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * @param speedX the speedX to set
	 */
	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}
	/**
	 * @param speedY the speedY to set
	 */
	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	/**
	 * @param bitmapRun the bitmapRun to set
	 */
	public void setBitmapRun(Bitmap bitmapRun) {
		this.bitmapRun = bitmapRun;
	}
	/**
	 * @param bitmapDie the bitmapDie to set
	 */
	public void setBitmapDie(Bitmap bitmapDie) {
		this.bitmapDie = bitmapDie;
	}
	/**
	 * @param bitmaps the bitmaps to set
	 */
	public void setBitmaps(Bitmap[] bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}

	public Runner(Bitmap[] bitmaps, Bitmap bitmapDie) {
		super();
		this.bitmaps = bitmaps;
		this.bitmapDie = bitmapDie;
		this.width = bitmaps[0].getWidth();
		this.height = bitmaps[0].getHeight();
	}
	
	private void changeCurrentBitmap(){
		if(System.currentTimeMillis() - lastTime > 50){
			lastTime = System.currentTimeMillis();
			if(++index == bitmaps.length){
				index = 0;
			}
			bitmapRun = bitmaps[index];
		}
	}
	
	public void drawSelf(Canvas canvas){
		this.changeCurrentBitmap();
//		Paint paint = new Paint();
//		paint.setColor(Color.RED);
//		paint.setStrokeWidth(1);
		if(isDie){
			canvas.drawBitmap(bitmapDie, x, y, null);
//			canvas.drawLine(0, y, x, y, paint);
//			canvas.drawLine(0, y+bitmapDie.getHeight(), x, y+bitmapDie.getHeight(), paint);
		}else{
//			canvas.drawLine(0, y, x, y, paint);
			canvas.drawBitmap(bitmapRun, x, y, null);
		}
	}
	
	public Rect getRect(){
		return new Rect((int)x, (int)y, (int)(x+width), (int)(y+height));
	}
}
