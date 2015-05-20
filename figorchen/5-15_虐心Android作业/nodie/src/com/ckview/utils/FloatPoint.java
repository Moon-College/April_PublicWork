package com.ckview.utils;

import com.ckview.utils.log.BaseLog;

import android.util.FloatMath;

public class FloatPoint {
	private float x;
	private float y;
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
	public FloatPoint(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	public FloatPoint() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static float getLength(FloatPoint fristPoint, FloatPoint secondPoint){
		BaseLog.debug("ck1", "x = "+fristPoint.getX()+"   y= "+fristPoint.getY());
		BaseLog.debug("ck1", "x = "+secondPoint.getX()+"   y= "+secondPoint.getY());
		return FloatMath.sqrt((fristPoint.x - secondPoint.getX())*(fristPoint.x - secondPoint.getX()) + (fristPoint.y - secondPoint.getY())*(fristPoint.y - secondPoint.getY()));
	}
	
	public static FloatPoint getMiddlePoint(FloatPoint fristPoint, FloatPoint secondPoint) {
		FloatPoint middlePoint = new FloatPoint();
		middlePoint.setX((fristPoint.getX() + secondPoint.getX()) / 2);
		middlePoint.setY((fristPoint.getY() + secondPoint.getY()) / 2);
		return middlePoint;
	}
}
