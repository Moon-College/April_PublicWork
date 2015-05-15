package com.yk.viewpage.nevigation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yk.viewpage.nevigation.adapter.ViewPageAdapter;

public class MainActivity extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {
	/**水平滑动条*/
	private HorizontalScrollView hsv;
	/**单选按钮组*/
	private RadioGroup rg;
	/**底线*/
	private View line;
	/**viewpage*/
	private ViewPager viewpage;
	private float fromXDelta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		initViews();
	}

	private void findViews() {
		hsv=(HorizontalScrollView) findViewById(R.id.hsv);
		rg=(RadioGroup) findViewById(R.id.rg);
		line=findViewById(R.id.line);
		viewpage=(ViewPager) findViewById(R.id.viewpage);
		rg.setOnCheckedChangeListener(this);
	}

	private void initViews() {
		ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager(),rg.getChildCount());
		viewpage.setAdapter(adapter);
		viewpage.setOnPageChangeListener(this);
		//默认第一项被选中
		rg.check(R.id.mm1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
		int radbWidth=rg.getChildAt(position).getWidth();
		int scrollXTotal=(int) ((position+positionOffset)*radbWidth);
		int offset=(viewpage.getWidth()-radbWidth)/2;
		hsv.scrollTo(scrollXTotal-offset,0);
		lineScroll(position,positionOffset);
	}

	private void lineScroll(int position, float positionOffset) {
		RadioButton rdb=(RadioButton) rg.getChildAt(position);
		int radbWidth=rdb.getWidth();
		int[] location=new int[2];
		rdb.getLocationInWindow(location);
		TranslateAnimation translateAnimation=new TranslateAnimation(fromXDelta, location[0]+positionOffset*radbWidth, 0, 0);
		translateAnimation.setDuration(50);
		translateAnimation.setFillAfter(true);
		line.startAnimation(translateAnimation);
		fromXDelta=location[0]+positionOffset*radbWidth;
	}

	@Override
	public void onPageSelected(int position) {
		int id = 0;
		switch (position) {
		case 0:
			id=R.id.mm1;
			break;
		case 1:
			id=R.id.mm2;
			break;
		case 2:
			id=R.id.mm3;
			break;
		case 3:
			id=R.id.mm4;
			break;
		case 4:
			id=R.id.mm5;
			break;
		case 5:
			id=R.id.mm6;
			break;
		case 6:
			id=R.id.mm7;
			break;
		default:
			break;
		}
		rg.check(id);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.mm1:
			viewpage.setCurrentItem(0);
			break;
		case R.id.mm2:
			viewpage.setCurrentItem(1);
			break;
		case R.id.mm3:
			viewpage.setCurrentItem(2);
			break;
		case R.id.mm4:
			viewpage.setCurrentItem(3);
			break;
		case R.id.mm5:
			viewpage.setCurrentItem(4);
			break;
		case R.id.mm6:
			viewpage.setCurrentItem(5);
			break;
		case R.id.mm7:
			viewpage.setCurrentItem(6);
			break;
		default:
			break;
		}
	}
}
