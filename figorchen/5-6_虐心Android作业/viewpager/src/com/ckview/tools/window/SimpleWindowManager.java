package com.ckview.tools.window;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SimpleWindowManager {
	
	/**
	 * get the width of window
	 * @param context
	 * @return width
	 */
	public static int getWindowWidth(Context context){
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.widthPixels;
	}
	
	/**
	 * get the height of window
	 * @param context
	 * @return width
	 */
	public static int getWindowHeight(Context context){
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.heightPixels;
	}
	
	
	
	/**
	 * get the DisplayMetrics
	 * @param context
	 * @return DisplayMetrics instances
	 */
	public static DisplayMetrics getDisplayMetrics(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
}
