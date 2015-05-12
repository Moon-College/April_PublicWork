package com.tz.fragment;

import com.tz.activity.R;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class Myfragment1 extends Fragment {
	private View v;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle b=getArguments();
		int postion=b.getInt("position");
		if(postion%2==0){
			v = inflater.inflate(R.layout.myfragment1, null);
		}else{
		    v=new TextView(getActivity());
		    if(View.class.isAssignableFrom(TextView.class)){
		    	TextView tv=(TextView)v;
		    	tv.setText("not is response");
		    	tv.setTextColor(getResources().getColor(R.color.red_tv));
		    }
		    
		}
		return v;
	}
}
