package com.tz.viewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tz.viewpager.fragment.CommonFragment;
import com.tz.viewpager.fragment.PicFragment;
import com.tz.viewpager.fragment.PicFragmentFour;
import com.tz.viewpager.fragment.PicFragmentThree;
import com.tz.viewpager.fragment.PicFragmentTwo;
import com.tz.viewpager.fragment.PicListFragment;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int LENGTH=6;
    private Fragment[] fragments={
    	new CommonFragment(),new PicListFragment(),new PicFragment(),new PicFragmentTwo(),
    	new PicFragmentThree(),
    	new PicFragmentFour()
    };
	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}


	@Override
	public Fragment getItem(int position) {
		Log.i("INFO", "创建了第"+position+"页面");
		return fragments[position];
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return super.isViewFromObject(view, object);
	}

	@Override
	public int getCount() {
		return LENGTH;
	}
  
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	Log.i("INFO", "销毁了第"+position+"页面");
    	super.destroyItem(container, position, object);
    }
}
