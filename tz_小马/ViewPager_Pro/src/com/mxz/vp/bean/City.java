package com.mxz.vp.bean;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class City {
	private String name;
	private SoftReference<Bitmap> bitmap;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SoftReference<Bitmap> getBitmap() {
		return bitmap;
	}
	public void setBitmap(SoftReference<Bitmap> bitmap) {
		this.bitmap = bitmap;
	}
	
}	
