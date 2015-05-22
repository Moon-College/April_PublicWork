package com.mxz.vp.adapter;

import com.mxz.vp.fragment.MyFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PVAdapter extends FragmentPagerAdapter {
	int count=0;
	public PVAdapter(FragmentManager fm,int count) {
		super(fm);
		this.count=count;
		
	}
	public void setCount(int count){
		this.count=count;
		
	}
	//每一页长什么样
	@Override
	public Fragment getItem(int position) {
		MyFragment frag=new MyFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("position", position);
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public int getCount() {
		return count;
	}

	
	

}
