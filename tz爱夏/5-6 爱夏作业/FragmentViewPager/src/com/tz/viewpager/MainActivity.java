package com.tz.viewpager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tz.viewpager.adapter.MyViewPagerAdapter;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {
	private ViewPager viewPager;
	private FragmentManager fm;
	private MyViewPagerAdapter pagerAdapter;
	private HorizontalScrollView hsv;
	private RadioGroup rg;
	private RadioButton important, video, fan, sport, music, money;
	private int currentItem;
	private View view;
	private int fromX;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fm = getSupportFragmentManager();
		initView();
	}

	/**
	 * init view and viewPager
	 * 
	 * @author jackie
	 */
	private void initView() {
		hsv = (HorizontalScrollView) findViewById(R.id.scrollView);
		rg = (RadioGroup) findViewById(R.id.rg);
		view = findViewById(R.id.view);
		important = (RadioButton) findViewById(R.id.important);
		video = (RadioButton) findViewById(R.id.video);
		fan = (RadioButton) findViewById(R.id.fan);
		sport = (RadioButton) findViewById(R.id.sport);
		music = (RadioButton) findViewById(R.id.music);
		money = (RadioButton) findViewById(R.id.money);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pagerAdapter = new MyViewPagerAdapter(fm);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
		rg.setOnCheckedChangeListener(this);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.important:
			currentItem = 0;
			break;
		case R.id.video:
			currentItem = 1;
			break;
		case R.id.fan:
			currentItem = 2;
			break;
		case R.id.sport:
			currentItem = 3;
			break;
		case R.id.music:
			currentItem = 4;
			break;
		case R.id.money:
			currentItem = 5;
			break;
		default:
			break;
		}
		viewPager.setCurrentItem(currentItem);
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// 滑动到某个页面时，顶部title需要居中显示
		Log.i("INFO", "positionOffset" + positionOffset);
		int total = (int) ((position + positionOffset) * important.getWidth());
		// 显示在屏幕里的左边距离
		int left = (viewPager.getWidth() - important.getWidth()) / 2;
		// hsv滑动的距离
		int scrollX = total - left;
		hsv.scrollTo(scrollX, 0);
		// viewPager翻页后，红线也要移动到选中的title下面
		lineScroll(position, positionOffset);
	}

	private void lineScroll(int position, float positionOffset) {
		// 根据radioButton的坐标计算出红线的坐标
		RadioButton radioButton = (RadioButton) rg.getChildAt(position);
		int[] location = new int[2];
		radioButton.getLocationInWindow(location);
		// 红线与button需要居中显示，需要计算差量
		TranslateAnimation animation = new TranslateAnimation(fromX,
				location[0] + (radioButton.getWidth() - view.getWidth()) / 2
						+ positionOffset * important.getWidth(), 0, 0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		view.startAnimation(animation);
		fromX = (int) (location[0] + (radioButton.getWidth() - view.getWidth())
				/ 2 + positionOffset * important.getWidth());
	}

	public void onPageSelected(int arg0) {

	}

}