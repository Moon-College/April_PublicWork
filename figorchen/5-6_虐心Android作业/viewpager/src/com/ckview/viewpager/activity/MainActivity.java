package com.ckview.viewpager.activity;

import com.ckview.tools.log.BaseLog;
import com.ckview.tools.window.SimpleWindowManager;
import com.ckview.viewpager.R;
import com.ckview.viewpager.adapter.ViewPagerFragmentAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("ResourceAsColor")
public class MainActivity extends FragmentActivity implements OnCheckedChangeListener, OnPageChangeListener {
	private static final FragmentManager FragmentManager = null;
	private HorizontalScrollView hsv;
	private RadioGroup rg;
	private ViewPager vp;
	private View v;
	private int windowWidth;
	private RadioButton rb;
	private String[] countries;
	private int fromX = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		countries = getResources().getStringArray(R.array.countries);
		initView();
		
		//set a listener to response choose a radio button
		rg.setOnCheckedChangeListener(this);
		
		//set a listener to response ViewPager change
		vp.setOnPageChangeListener(this);
	}
	
	/**
	 * initialize all View
	 */
	@SuppressLint("NewApi")
	private void initView() {
		windowWidth = SimpleWindowManager.getWindowWidth(this);
		hsv = (HorizontalScrollView) findViewById(R.id.hsv);
		rg = (RadioGroup) findViewById(R.id.rg);
		v = findViewById(R.id.v);
		v.getLayoutParams().width = windowWidth/3;
		for(int i = 0; i < countries.length; i++){
			rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.radio_button, null);
			rg.addView(rb);
			rb.setText(countries[i]);
			if(i == 0){
				rb.setBackgroundColor(getResources().getColor(R.color.choose));
			}
			rb.setId(i);
			rb.getLayoutParams().width = windowWidth/3;
		}
		vp = (ViewPager) findViewById(R.id.vp);
		ViewPagerFragmentAdapter adapter = ViewPagerFragmentAdapter.getViewPagerFragmentAdapter(this.getSupportFragmentManager(), countries);
		vp.setAdapter(adapter);
	}

	/**
	 * when RadioButton is checked,the ViewPager will present corresponding page
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		vp.setCurrentItem(checkedId);
		for(int i = 0; i < group.getChildCount(); i++){
			rb = (RadioButton) group.getChildAt(i);
			rb.setBackgroundColor(getResources().getColor(R.color.unchoose));
		}
		rb = (RadioButton) group.getChildAt(checkedId);
		rb.setBackgroundColor(getResources().getColor(R.color.choose));
		hsv.smoothScrollTo((checkedId-1)*windowWidth/3, 0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		BaseLog.debug("debug", "0-----"+arg0);
		BaseLog.debug("debug", "1-----"+arg1);
		BaseLog.debug("debug", "2-----"+arg2);
		BaseLog.debug("debug", "3-----"+(int) (windowWidth/3*(arg0 - 1 + arg1)));
		hsv.scrollTo((int) (windowWidth/3*(arg0 - 1 + arg1)), 0);
		lineScroll(arg0, arg1);
	}

	private void lineScroll(int arg0, float offset) {
		rb = (RadioButton) rg.getChildAt(arg0);
		int[] location = new int[2];
		rb.getLocationInWindow(location);
		TranslateAnimation translateAnimation = new TranslateAnimation(fromX, location[0]+offset*windowWidth/3, 0, 0);
		translateAnimation.setDuration(50);
		translateAnimation.setFillAfter(true);
		fromX = (int) (location[0]+offset*windowWidth/3);
		v.startAnimation(translateAnimation);
	}

	@Override
	public void onPageSelected(int arg0) {
		for(int i = 0; i < rg.getChildCount(); i++){
			rb = (RadioButton) rg.getChildAt(i);
			rb.setBackgroundColor(getResources().getColor(R.color.unchoose));
		}
		rb = (RadioButton) rg.getChildAt(arg0);
		rb.setBackgroundColor(getResources().getColor(R.color.choose));
	}

}
