package com.tz.sqlite.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao<T> {
	SQLiteDatabase db;
	private String classAddr;
	public BaseDao(SQLiteDatabase db,String classAddr){
		this.classAddr=classAddr;
		this.db=db;
	}
	
	public void save(T t) throws IllegalAccessException, InstantiationException, ClassNotFoundException{
		Field[] f = t.getClass().getDeclaredFields();
		String sb="";
		for (Field field : f) {
			sb+= "'"+getter(t, field.getName())+"',";
		}
		String sql="insert into "+getName()+" values ("+sb.substring(0, sb.length()-1)+")";
		db.execSQL(sql);
	}
	
	 /**
     * @param obj
     *            操作的对象
     * @param att
     *            操作的属性
     * */
	  public  Object getter(Object obj, String att) {
		  Object b=null;
	        try {
	            Method method = obj.getClass().getMethod("get" + att);
	             b=  method.invoke(obj);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       return  b;
	    }
    /**
     * @param obj
     *            操作的对象
     * @param att
     *            操作的属性
     * @param value
     *            设置的值
     * @param type
     *            参数的属性
     * */
    public Object setter(Class obj, String att, Object value,
    		
            Class<?> type) {
    	Object o=null;
        try {
        	obj.newInstance();
            Method method = obj.getMethod("set" + att, type);
             o=   method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    return o;
    }
    
    
    
    
   
    /**
     * 得到实体类的名字
     * @param t
     * @return
     */
    public String getName(){
    	return classAddr;
    }
    
    
    
  public   List<T> qurryAll(T t) throws IllegalAccessException, InstantiationException{
    	List<T> list=new ArrayList<T>();
    	String sql="select * from "+getName();
    	Cursor cursor = db.rawQuery(sql, null);
    	while(cursor.moveToNext()){
			//查到一个
    		Field[] f = t.getClass().getDeclaredFields();
			for (Field field : f) {
				setter(t.getClass(), field.getName(), cursor.getString(cursor.getColumnIndex(field.getName())), field.getType());

				continue;
			}
		}
    	
    	
    	
    	return list;
    }
    
}
