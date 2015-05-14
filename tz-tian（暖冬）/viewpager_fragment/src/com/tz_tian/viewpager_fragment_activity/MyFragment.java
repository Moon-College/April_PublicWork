package com.tz_tian.viewpager_fragment_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
	String[] contries = new String[]{
			"中国","韩国","朝鲜","日本","美国","英国"
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		int position = bundle.getInt("id");
		TextView tv = new TextView(getActivity());
		tv.setText(contries[position]);
		return tv;
	}

}
