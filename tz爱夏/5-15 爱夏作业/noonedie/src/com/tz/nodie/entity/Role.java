package com.tz.nodie.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Role {
	private Bitmap[] bits;
	private Bitmap bitmap;
	private int width;
	private int height;
	private float speedX;
	private float speedY;
	private int x, y; // 坐标
	private int index;
	private long lastTime;

	public Role(Bitmap[] bits) {
		this.bits = bits;
		this.bitmap = bits[0];
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 切换图片
	 * @param span
	 */
	private void animateRole(long span) {

		if (System.currentTimeMillis() - lastTime >= span) {
			// 切图
			index++;
			if(index==bits.length){
				index=0;
			}
			bitmap = bits[index];
			lastTime=System.currentTimeMillis();
		} 
	}
	 
	/**
	 * 绘制角色
	 */
	public void drawRole(Canvas canvas){
		animateRole(200);
		canvas.drawBitmap(bitmap, this.getX(), this.getY(), null);
	}
}
