package com.ckview.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ckview.nodie.R;
import android.app.Activity;
import android.view.View;

public class InitializeActivity {
	
	public static void initView(Activity activity){
		// get class of activity
		Class cls = activity.getClass();
		Field[] fields = cls.getDeclaredFields();
		
		for(Field field : fields){
			Class type = field.getType();
			if(View.class.isAssignableFrom(type)){
				// get method by name findViewById
				try {
					Method method = cls.getMethod("findViewById", int.class);
					
					// get class of R.id
					Class idClass = R.id.class;
					Field idField = idClass.getField(field.getName());
					Object value = idField.get("");
					Object control = method.invoke(activity, value);
					
					field.setAccessible(true);
					field.set(activity, control);
					
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
