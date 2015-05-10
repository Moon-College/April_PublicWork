package com.tz.viewpager;

import java.util.ArrayList;

import com.tz.adapter.MyFragmentPagerAdapter;
import com.tz.fragment.TestFragment;
import com.tz.viewpager.utils.Constant;

import android.R.color;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity  {
	 private ViewPager mPager;  
	    private ArrayList<Fragment> fragmentList;  
	    private ImageView cursor;  
	    private TextView tv_spring, tv_summer, tv_autumn, tv_winter;  
	    private int currIndex;//当前页卡编号  
	    private int bmpW;//横线图片宽度  
	    private int offset;//图片移动的偏移量  
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	// TODO Auto-generated method stub
	    	return super.onTouchEvent(event);
	    }

	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.main);  
	          
	        InitTextView();  
	        InitImage();  
	        InitViewPager();  
	    }  
	      
	      
	    /* 
	     * 初始化标签名 
	     */  
	    public void InitTextView(){  
	    	tv_spring = (TextView)findViewById(R.id.tv_spring);  
	    	tv_summer = (TextView)findViewById(R.id.tv_summer);  
	    	tv_autumn = (TextView)findViewById(R.id.tv_autumn);  
	    	tv_winter = (TextView)findViewById(R.id.tv_winter);  
	    	tv_spring.setOnClickListener(new txListener(0));  
	    	tv_summer.setOnClickListener(new txListener(1));  
	    	tv_autumn.setOnClickListener(new txListener(2));  
	    	tv_winter.setOnClickListener(new txListener(3));  
	    }  
	      
	   
	      
	    public class txListener implements View.OnClickListener{  
	        private int index=0;  
	          
	        public txListener(int i) {  
	            index =i;  
	        }

			public void onClick(View v) {
				// TODO Auto-generated method stub
			    mPager.setCurrentItem(index);  
			}  
	       
	    }  
	      
	      
	    /* 
	     * 初始化图片的位移像素 
	     */  
	    public void InitImage(){  
	    	cursor = (ImageView)findViewById(R.id.cursor);  
	        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher).getWidth();  
	        DisplayMetrics dm = new DisplayMetrics();  
	        getWindowManager().getDefaultDisplay().getMetrics(dm);  
	        int screenW = dm.widthPixels;  
	        offset = (screenW/4 - bmpW)/2;  
	        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）  
	        Matrix matrix = new Matrix();  
	        matrix.postTranslate(offset, 0);  
	        cursor.setImageMatrix(matrix);  
	    }  
	      
	    /* 
	     * 初始化ViewPager 
	     */  
	    public void InitViewPager(){  
	        mPager = (ViewPager)findViewById(R.id.viewpager);  
	        fragmentList = new ArrayList<Fragment>();  
	        //春天
	        Fragment spring = TestFragment.newInstance(R.drawable.spring);
	        //夏天
	        Fragment summer = TestFragment.newInstance(R.drawable.summer);
	        //秋天
	        Fragment autumn = TestFragment.newInstance(R.drawable.autumn);
	        //冬天
	        Fragment winter = TestFragment.newInstance(R.drawable.winter);
	        fragmentList.add(spring);  
	        fragmentList.add(summer);  
	        fragmentList.add(autumn);  
	        fragmentList.add(winter);  
	          
	        //给ViewPager设置适配器  
	        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
	        mPager.setCurrentItem(0);//设置当前显示标签页为第一页  
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器  	   
	        }  
	  
	     
		    public class MyOnPageChangeListener implements OnPageChangeListener{  
		        private int one = offset *2 +bmpW;//两个相邻页面的偏移量  

				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}

				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}

				public void onPageSelected(int arg0) {
					Toast.makeText(MainActivity.this, "第几个页面"+arg0, Toast.LENGTH_SHORT).show();
		            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画  
		            currIndex = arg0;  
		            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态  
		            animation.setDuration(200);//动画持续时间0.2秒  
		            cursor.startAnimation(animation);//是用ImageView来显示动画的  
		            int i = currIndex + 1;  
				}
		        
		    }



		      



		
	
}