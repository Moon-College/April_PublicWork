package com.yk.viewpage.nevigation.fragment;

import com.yk.viewpage.nevigation.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MyFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		int position=this.getArguments().getInt("position");
		ImageView imgView=new ImageView(getActivity());
		imgView.setScaleType(ScaleType.CENTER_CROP);
		switch (position) {
		case 0:
			imgView.setImageResource(R.drawable.a1);
			break;
		case 1:
			imgView.setImageResource(R.drawable.a2);
			break;
		case 2:
			imgView.setImageResource(R.drawable.a3);
			break;
		case 3:
			imgView.setImageResource(R.drawable.a4);
			break;
		case 4:
			imgView.setImageResource(R.drawable.a5);
			break;
		case 5:
			imgView.setImageResource(R.drawable.a6);
			break;
		case 6:
			imgView.setImageResource(R.drawable.a7);
			break;
		default:
			imgView.setImageResource(R.drawable.ic_launcher);
			break;
		}
		return imgView;
	}
}
