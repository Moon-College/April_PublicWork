package com.fragmentviewpager.fragment;

import com.fragmentviewpager.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ContentFragment extends Fragment {
	private int [] grils = new int [] {
			R.drawable.gril1,
			R.drawable.gril2,
			R.drawable.gril3,
			R.drawable.gril5,
			R.drawable.gril4,
			R.drawable.gril6,
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView background = new ImageView(getActivity());
		Bundle bundle = getArguments();
		int position = bundle.getInt("position", 0);
		background.setImageResource(grils[position]);
		return background;
	}

}
