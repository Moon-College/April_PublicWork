package com.example.nodie.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Role {
	private Bitmap bitmap;//当前图片
	private Bitmap[] bms;//完成一个动作的所有图片的数组
	float x,y;//图片将绘制的位置
	float speedX,speedY;//速度
	int width,height;//图片绘制的宽高
	private long lastTime;
	private int index;
	public Role(Bitmap[] bms){
		this.bms = bms;
		this.bitmap = bms[0];
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
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
	
	/**
	 * 切换图片开启动画
	 * @param span
	 */
	private void animateRole(long span){
		//每隔一小段时间就切换当前图片
		if(System.currentTimeMillis() - lastTime>=span){
			//说明时间到了，该切图了
			index ++;
			if(index ==bms.length){
				index = 0;
			}
			bitmap = bms[index];
			lastTime = System.currentTimeMillis();
		}
	}
	/**
	 * 将该任务绘制到画布上
	 */
	public void drawRole(Canvas canvas){
		animateRole(200);
		//绘制自己
		canvas.drawBitmap(bitmap, this.getX(),this.getY(),null);
	}
}
