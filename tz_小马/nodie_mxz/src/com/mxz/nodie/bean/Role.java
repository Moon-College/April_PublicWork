package com.mxz.nodie.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Role {
	private Bitmap bitmap;
	private Bitmap[] bms;
	private float x,y;
	private float speedX,speedY;
	private int width,height;
	private long lastTime;
	private int index;
	private boolean isJump;
	private boolean isJumpTow;
	
	public boolean isJumpTow() {
		return isJumpTow;
	}

	public void setJumpTow(boolean isJumpTow) {
		this.isJumpTow = isJumpTow;
	}

	public Role(Bitmap[] bms){
		this.bms=bms;
		this.bitmap=bms[0];
		this.width=bitmap.getWidth();
		this.height=bitmap.getHeight();
	}
	
	public boolean isJump() {
		return isJump;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Bitmap[] getBms() {
		return bms;
	}
	public void setBms(Bitmap[] bms) {
		this.bms = bms;
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
	
	public void animateRole(long span){
		if(System.currentTimeMillis()-lastTime>=span){
			index++;
			if(index==bms.length){
				index=0;
			}
			bitmap=bms[index];
			lastTime=System.currentTimeMillis();
			
		}
	}
	public void drawSelf(Canvas canvas){
		animateRole(50);
		canvas.drawBitmap(bitmap, this.getX(), this.getY(), null);
	}
	
	public Rect getRectFromRole(Canvas canvas,Paint paint){
		Rect rect=new Rect();
		rect.left=(int) this.getX();
		rect.right=(int) (this.getX()+(this.getWidth()*0.8));
		rect.top=(int) this.getY();
		rect.bottom=(int) (this.getY()+(this.getHeight()*0.8));
		//canvas.drawRect(rect, paint);
//		Log.i("INFO","-----------");
//		Log.i("INFO", this.getX()+"");
//		Log.i("INFO", this.getY()+"");
		return rect;
	}
}
