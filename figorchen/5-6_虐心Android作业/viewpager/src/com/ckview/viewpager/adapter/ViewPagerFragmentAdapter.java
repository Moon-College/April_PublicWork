package com.ckview.viewpager.adapter;

import com.ckview.viewpager.fragment.MyFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
	
	// save countries
	private String[] countries;
	
	public String[] getCountries() {
		return countries;
	}

	public void setCountries(String[] countries) {
		this.countries = countries;
	}
	
	/**
	 * static function to get ViewPagerFragmentAdapter from constructor
	 * @param fm FragmentManager for constructor
	 * @param countries 
	 * @return
	 */
	public static ViewPagerFragmentAdapter getViewPagerFragmentAdapter(FragmentManager fm,String[] countries){
		ViewPagerFragmentAdapter vpAdapter = new ViewPagerFragmentAdapter(fm);
		vpAdapter.setCountries(countries);
		return vpAdapter;
	}
	
	public ViewPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * get item by position
	 * @param arg0 position
	 */
	@Override
	public Fragment getItem(int arg0) {
		MyFragment mfg = new MyFragment();
		Bundle bundle = new Bundle();
		bundle.putCharSequence("country", countries[arg0]);
		mfg.setArguments(bundle);
		return mfg;
	}

	/**
	 * get all fragment count
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return countries.length;
	}
	
}
