package com.tz.nodie.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Role {
	private Bitmap bitmap;
	private Bitmap[]  bitmaps;
	float x,y;//坐标
	float speedX,speedY;//速度
	int width,height;//宽高
	private long lastTime;
	private int index;
	private boolean isJump;
	
	public Role(Bitmap[]  bitmaps){
		this.bitmaps=bitmaps;
		this.bitmap=bitmaps[0];
		width=bitmap.getWidth();
		height=bitmap.getHeight();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap[] getBitmaps() {
		return bitmaps;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	/**
	 *切换图片
	 */
	private  void  animateRole(long span){
		if(System.currentTimeMillis()-lastTime>=span){
			//说明时间到了，该切图了
			index ++;
			if(index ==bitmaps.length){
				index = 0;
			}
			bitmap = bitmaps[index];
			lastTime = System.currentTimeMillis();
		}
	}
	/**
	 * 将该任务绘制到画布上
	 */
	public void drawSelf(Canvas canvas){
		animateRole(200);
		//绘制自己
		canvas.drawBitmap(bitmap, this.getX(),this.getY(),null);
	}
	/**
	 * 获取人物的矩形
	 */
	public Rect getRectFromRole(){
		Rect rect = new Rect();
		rect.left = (int) this.getX();
		rect.right = (int) (this.getX() + this.getWidth());
		rect.top = (int) this.getY();
		rect.bottom = (int) (this.getY() + this.getHeight());
		return rect;
	}

	public boolean isJump() {
		return isJump;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}
	
	
}
