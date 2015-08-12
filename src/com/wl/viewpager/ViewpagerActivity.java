package com.wl.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewpagerActivity extends Activity {
    /** Called when the activity is first created. */
    private ViewPager vp;
    private List<View> views;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        vp = (ViewPager) findViewById(R.id.vp);
        views = new ArrayList<View>();
        
        View one = LayoutInflater.from(getApplicationContext()).inflate(R.layout.page_one, null);
        View two = LayoutInflater.from(getApplicationContext()).inflate(R.layout.page_two, null);
        
        views.add(one);
        views.add(two);
        
        MyAdapter adapter = new MyAdapter();
        vp.setAdapter(adapter);
    }
	
	private class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return views.size();
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
			//super.destroyItem(container, position, object);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}
}