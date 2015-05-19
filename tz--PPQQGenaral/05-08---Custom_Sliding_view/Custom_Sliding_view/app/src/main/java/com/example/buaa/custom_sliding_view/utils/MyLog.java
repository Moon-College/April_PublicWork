package com.example.buaa.custom_sliding_view.utils;

import android.util.Log;

/**
 * Created by Alex on 2015/5/17.
 */
public class MyLog {
    public static boolean isDebug;

    public static int i(String tag,String msg){
        if(isDebug){
            return Log.i(tag,msg);
        }else {
            return 0;
        }
    }

}
