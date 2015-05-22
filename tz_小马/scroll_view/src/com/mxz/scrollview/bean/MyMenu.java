package com.mxz.scrollview.bean;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class MyMenu {
	private SoftReference<Bitmap> bitmap;
	private String text;
	private int imgId;
	
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public SoftReference<Bitmap> getBitmap() {
		return bitmap;
	}
	public void setBitmap(SoftReference<Bitmap> bitmap) {
		this.bitmap = bitmap;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
