package com.tz.dlidingmenu.utils;

import android.util.Log;

/**
 * Log统一管理�?
 * 
 * @author way
 * 
 */
public class L
{

	private L()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isDebug = true;// 是否�?��打印bug，可以在application的onCreate函数里面初始�?
	private static final String TAG = "way";

	// 下面四个是默认tag的函�?
	public static void i(String msg)
	{
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg)
	{
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg)
	{
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg)
	{
		if (isDebug)
			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函�?
	public static void i(String tag, String msg)
	{
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg)
	{
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg)
	{
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg)
	{
		if (isDebug)
			Log.i(tag, msg);
	}
}