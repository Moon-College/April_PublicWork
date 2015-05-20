package com.tz.nodie.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Role {
	private Bitmap[] bits;
	private Bitmap bitmap;
	private int width;
	private int height;
	private float speedX;
	private float speedY;
	private float x, y; // 坐标
	private int index;
	private long lastTime;
	private boolean isJump;

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

	public boolean isJump() {
		return isJump;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

	/**
	 * 切换图片
	 * 
	 * @param span
	 */
	private void animateRole(long span) {

		if (System.currentTimeMillis() - lastTime >= span) {
			// 切图
			index++;
			if (index == bits.length) {
				index = 0;
			}
			bitmap = bits[index];
			lastTime = System.currentTimeMillis();
		}
	}

	/**
	 * 绘制角色
	 */
	public void drawRole(Canvas canvas) {
		animateRole(100);
		canvas.drawBitmap(bitmap, this.getX(), this.getY(), null);
	}

	public Rect getRectByRole() {
		Rect rect = new Rect();
		rect.left = (int) this.getX();
		rect.top = (int) this.getY();
		rect.right = (int) (this.getX() + this.getWidth());
		rect.bottom = (int) (this.getY() + this.getHeight());
		return rect;
	}
}
