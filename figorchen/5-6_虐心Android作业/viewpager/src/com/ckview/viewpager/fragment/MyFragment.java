package com.ckview.viewpager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		String country = bundle.getString("country");
		TextView tv = new TextView(getActivity());
		tv.setText(country);
		return tv;
	}
}
