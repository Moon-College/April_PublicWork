package com.ckview.utils.log;

import android.util.Log;

public class BaseLog {
	public static boolean isDebug = true;
	public static boolean isInfo = true;
	public static boolean isError = true;
	public static boolean isWarn = true;
	public static boolean debugSwitch = true;
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void debug(String tag, String msg){
		if(debugSwitch&&isDebug){
			Log.d(tag, msg);
		}
	}
	
	/**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void debug(String tag, String msg, Throwable tr){
		if(debugSwitch&&isDebug){
			Log.d(tag, msg, tr);
		}
	}
	
	/**
     * Send a {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void info(String tag, String msg){
		if(debugSwitch&&isInfo){
			Log.i(tag, msg);
		}
	}
	
	/**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void info(String tag, String msg, Throwable tr){
		if(debugSwitch&&isInfo){
			Log.i(tag, msg, tr);
		}
	}
	
	/**
     * Send a {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void error(String tag, String msg){
		if(debugSwitch&&isError){
			Log.e(tag, msg);
		}
	}
	
	/**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void error(String tag, String msg, Throwable tr){
		if(debugSwitch&&isError){
			Log.e(tag, msg, tr);
		}
	}
	
	/**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void warn(String tag, String msg){
		if(debugSwitch&&isWarn){
			Log.w(tag, msg);
		}
	}
	
	/**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void warn(String tag, String msg, Throwable tr){
		if(debugSwitch&&isWarn){
			Log.w(tag, msg, tr);
		}
	}
}
