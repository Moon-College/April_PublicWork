package com.tz.viewpager.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.tz.viewpager.R;
import com.tz.viewpager.adapter.ImageAdapter;

public class PicListFragment extends Fragment {

	private ListView picListView;
    private ImageAdapter imageAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pic_fragment, null);
		picListView = (ListView) view.findViewById(R.id.picListView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageAdapter=new ImageAdapter(getActivity());
		this.picListView.setAdapter(imageAdapter);
	}
}
