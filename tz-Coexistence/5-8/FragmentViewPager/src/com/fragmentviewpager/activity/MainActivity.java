package com.fragmentviewpager.activity;

import java.util.ArrayList;
import java.util.List;

import com.fragmentviewpager.fragment.ContentFragment;

import android.os.Bundle;
import android.animation.Animator;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	/** ViewPager导航控件 */
	private ViewPager viewPager;
	/** 存放单选按钮的水平滑动视图 */
	private HorizontalScrollView hsv;
	/** 单选集 */
	private RadioGroup radioGroup;
	/** 所有图片的名字 */
	private List<String> imageNameList;
	/** 所有单选框 */
	private List<RadioButton> radioButtoonList;
	/** 当前的显示图片的名称 */
	private TextView currentImageName;
	/** 标题栏下的紫线 */
	private View line;
	/** 存储游标的位置x,y */
	private int [] lineLocation;
	/** 当前单选框的位置 */
	private int [] radioButtonLocation;
	/** 屏幕宽高 */
	private int winWidth,winHeight;
	/** 单选框的宽度 */
	private int radioWidth;
	/** 紫线开始移动的位置 */
	private int fromX;
	/** 紫线需要移动到的位置 */
	private int toX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindowWidthAndHeight();
		initView();
		lineLocation = getLineLocation();
		initData();
	}

	/**
	 * Init Data
	 */
	private void initData() {
		imageNameList = new ArrayList<String>();
		radioButtoonList = new ArrayList<RadioButton>();
		RadioButton grilRadio;
		String name;
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			grilRadio = (RadioButton) radioGroup.getChildAt(i);
			name = grilRadio.getText().toString();
			radioButtoonList.add(grilRadio);
			imageNameList.add(name);
		}
		// 设置默认显示
		currentImageName.setText(imageNameList.get(0));
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPagerScrollListener());
		radioGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * Init View
	 */
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		hsv = (HorizontalScrollView) findViewById(R.id.hsv);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		currentImageName = (TextView) findViewById(R.id.currentImageName);
		line = findViewById(R.id.line);
	}

	
	class ViewPagerAdapter extends FragmentPagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			ContentFragment contentFragment = new ContentFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			contentFragment.setArguments(bundle);
			return contentFragment;
		}

		@Override
		public int getCount() {
			return imageNameList.size();
		}
	}
	
	class ViewPagerScrollListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
			// 计算对应选项居中时 水平滑动视图需要滑动的位置
			// 屏幕边到中间文字之间的距离
			int num = (winWidth-radioWidth)/2;
			// 需要滑动的总长度
			int total = position*radioWidth;
			// 计算需要滑出去的距离
			hsv.scrollTo(total - num, 0);
			
			radioWidth = radioButtoonList.get(0).getWidth();// 获取单选框的宽度
			radioButtonLocation = getCurrentRadioButtonLoaction(position);
			// 设置紫线平移动画
			TranslateAnimation animation = new TranslateAnimation(
					fromX, 
					radioButtonLocation[0]+radioWidth * positionOffset, 
					0, 
					0);
			animation.setFillAfter(true);// 设置动画结束时停在结束时的位置
			animation.setDuration(10);
			// 开始的X坐标 = 当前单选框的的x坐标+单选框的宽度 * ViewPager滑动偏移量
			fromX = (int) (radioButtonLocation[0]+radioWidth * positionOffset);
			line.startAnimation(animation);
		}

		@Override
		public void onPageSelected(int position) {
			currentImageName.setText(imageNameList.get(position));
		}
		
	}
	
	/**
	 * Get Cursor Location
	 * @return Location[]
	 */
	private int [] getLineLocation(){
		int [] location = new int [2];
		line.getLocationInWindow(location);
		return location;
	}
	
	/**
	 * Get Current RadioButton Location
	 * @param position Current RadioButton index
	 * @return location[0] = x, location[1] = y
	 */
	public int [] getCurrentRadioButtonLoaction(int position){
		int [] location = new int[2];
		radioGroup.getChildAt(position).getLocationInWindow(location);
		return location;
	}
	
	/**
	 * Get Window Width and Height
	 */
	public void getWindowWidthAndHeight(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		winWidth = dm.widthPixels;
		winHeight = dm.heightPixels;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int currentIndex = 0;
		switch (checkedId) {
		case R.id.gril1:
			currentIndex = 0;
			break;
		case R.id.gril2:
			currentIndex = 1;
			break;
		case R.id.gril3:
			currentIndex = 2;
			break;
		case R.id.gril4:
			currentIndex = 3;
			break;
		case R.id.gril5:
			currentIndex = 4;
			break;
		case R.id.gril6:
			currentIndex = 5;
			break;

		default:
			break;
		}
		viewPager.setCurrentItem(currentIndex,true);
	}
}
