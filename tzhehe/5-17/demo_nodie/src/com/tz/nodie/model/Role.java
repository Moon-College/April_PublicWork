package com.tz.nodie.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Role {
	private Bitmap bitmap;
	private Bitmap[]  bitmaps;
	float x,y;//����
	float speedX,speedY;//�ٶ�
	int width,height;//���
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
	 *�л�ͼƬ
	 */
	private  void  animateRole(long span){
		if(System.currentTimeMillis()-lastTime>=span){
			//˵��ʱ�䵽�ˣ�����ͼ��
			index ++;
			if(index ==bitmaps.length){
				index = 0;
			}
			bitmap = bitmaps[index];
			lastTime = System.currentTimeMillis();
		}
	}
	/**
	 * ����������Ƶ�������
	 */
	public void drawSelf(Canvas canvas){
		animateRole(200);
		//�����Լ�
		canvas.drawBitmap(bitmap, this.getX(),this.getY(),null);
	}
	/**
	 * ��ȡ����ľ���
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
