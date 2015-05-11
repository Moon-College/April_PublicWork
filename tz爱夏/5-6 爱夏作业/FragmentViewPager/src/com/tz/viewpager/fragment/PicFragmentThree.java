package com.tz.viewpager.fragment;

import com.tz.viewpager.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PicFragmentThree extends Fragment {
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	   ImageView imageView=new ImageView(getActivity());
    	   imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	   imageView.setScaleType(ScaleType.FIT_XY);
    	   imageView.setImageDrawable(getResources().getDrawable(R.drawable.beauty5));
    	return imageView;
    }
}
