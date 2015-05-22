package com.mxz.file.bean;

import java.io.File;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class MyFile {
	private String file_name;
	private String file_path;
	private boolean isPic;
	private File file;
	private SoftReference<Bitmap> bitmap;
	
	public SoftReference<Bitmap> getBitmap() {
		return bitmap;
	}
	public void setBitmap(SoftReference<Bitmap> bitmap) {
		this.bitmap = bitmap;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public boolean isPic() {
		return isPic;
	}
	public void setPic(boolean isPic) {
		this.isPic = isPic;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
