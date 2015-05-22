package com.mxz.scrollview.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mxz.scrollview.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ReflexUtil {
	public static void initView(Activity activity) throws Exception{
		Class cls=activity.getClass();
		Log.i("info", cls+"*---*");
		Field fields[]=cls.getDeclaredFields();
		for (Field field : fields) {
			Class type=field.getType();
			
			if(View.class.isAssignableFrom(type)){
				field.setAccessible(true);
				
				Method method=cls.getMethod("findViewById", int.class);
				String name=field.getName();
				Log.i("info", type+"*---*"+name);
				Class idcls=R.id.class;
				Field idfield=idcls.getDeclaredField(name);
				Object value=idfield.get("");
				Object control=method.invoke(activity, value);
				field.set(activity, control);
				
//				Method listenermethod=control.getClass().getMethod("setOnClickListener",OnClickListener.class);
//				listenermethod.invoke(control, activity);
			}
		}
	}
}
