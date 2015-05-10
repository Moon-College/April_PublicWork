package com.tz.fragment;



import com.tz.viewpager.R;

import android.graphics.BitmapFactory;
import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.util.Log;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.Button;
import android.widget.ImageView;

public class TestFragment extends Fragment {

	 private static final String TAG = "TestFragment";  
	    private int hello=R.drawable.ic_launcher; 
	  
	  public  static TestFragment newInstance(int s) {  
	        TestFragment newFragment = new TestFragment();  
	        Bundle bundle = new Bundle();  
	        bundle.putInt("pic", s);  
	        newFragment.setArguments(bundle);  
	          
	        //bundle还可以在每个标签里传送数据  
	          
	          
	        return newFragment;  
	  
	    }  
	  
	  
	  
	    @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
	        Log.d(TAG, "TestFragment-----onCreateView");  
	        Bundle args = getArguments();  
	        hello = args != null ? args.getInt("pic") : hello;  
	      ImageView	 imageView=new ImageView(getActivity());
	      imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),hello));
	        return imageView;  
	  
	    }  
}
