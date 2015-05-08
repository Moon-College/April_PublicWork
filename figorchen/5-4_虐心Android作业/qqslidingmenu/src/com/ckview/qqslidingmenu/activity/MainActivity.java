package com.ckview.qqslidingmenu.activity;

import com.ckview.qqslidingmenu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class MainActivity extends Activity {
	private HorizontalScrollView hsv;
	private int windowWidth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	

}
