package com.tz.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tz.fragment.Myfragment;
import com.tz.fragment.Myfragment1;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ViewPager vp;
	private RadioButton mChina, mFrance, mJapan, mKorea, mUs, mNkorea;
	private RadioGroup mRg;
	private HorizontalScrollView mHsv;
	private int mcurrent = -1;
	private View mline;
	private int fromXDelta;
	private FragmentManager fm;
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setListener();
	}

	private void initView() {
		fm = getSupportFragmentManager();
		vp = (ViewPager) findViewById(R.id.vp);
		mChina = (RadioButton) findViewById(R.id.rb_china);
		mFrance = (RadioButton) findViewById(R.id.rb_France);
		mJapan = (RadioButton) findViewById(R.id.rb_Japan);
		mKorea = (RadioButton) findViewById(R.id.rb_Korea);
		mUs = (RadioButton) findViewById(R.id.rb_us);
		mNkorea = (RadioButton) findViewById(R.id.rb_NKorea);
		mHsv = (HorizontalScrollView) findViewById(R.id.hsv);
		mRg = (RadioGroup) findViewById(R.id.rg);
		mline = findViewById(R.id.v);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		mRg.setOnCheckedChangeListener(this);
		MyPageAdapter mAdapter = new MyPageAdapter(fm);
		vp.setAdapter(mAdapter);
		vp.setOnPageChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		switch (checkedId) {
		case R.id.rb_china:
			mcurrent = 0;
			break;
		case R.id.rb_us:
			mcurrent = 1;
			break;
		case R.id.rb_Japan:
			mcurrent = 2;
			break;
		case R.id.rb_Korea:
			mcurrent = 3;
			break;
		case R.id.rb_France:
			mcurrent = 4;
			break;
		case R.id.rb_NKorea:
			mcurrent = 5;
			break;

		default:
			Log.i(TAG, "误操作");
			break;
		}
		vp.setCurrentItem(mcurrent);
	}

	private class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment frament = null;
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				frament = new Myfragment();
			} else {
				frament = new Myfragment1();
				Bundle b = new Bundle();
				b.putInt("position",arg0);
				frament.setArguments(b);
			}

			return frament;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mRg.getChildCount();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		int total = (int) ((arg0 + arg1) * mChina.getWidth());
		Log.i("INFO", "rb_position:" + arg0);
		int green = (vp.getWidth() - mChina.getWidth()) / 2;
		int dx = total - green;// 计算出要滑出去的距离
		mHsv.scrollTo(dx, 0);
		lineScoll(arg0, arg1);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

		map.put(arg0 + 1, arg0 + 1);

	}

	private void lineScoll(int position, float positionOffSet) {
		// TODO Auto-generated method stub
		RadioButton rb = (RadioButton) mRg.getChildAt(position);
		int[] location = new int[2];
		rb.getLocationInWindow(location);
		TranslateAnimation animation = new TranslateAnimation(fromXDelta,
				location[0] + positionOffSet * mChina.getWidth(), 0, 0);
		animation.setDuration(50);// 动画持续事件
		animation.setFillAfter(true);
		fromXDelta = (int) (location[0] + positionOffSet * mChina.getWidth());
		mline.startAnimation(animation);// 线开始动画
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int count = -1;
		switch (event.getAction()) {
		case KeyEvent.KEYCODE_BACK:
			for (Map.Entry m : map.entrySet()) {
				count = (Integer) m.getValue();
			}
			if (count == 1) {
				return true;
			}
			break;

		default:
			Log.i("i", "误操作");
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
