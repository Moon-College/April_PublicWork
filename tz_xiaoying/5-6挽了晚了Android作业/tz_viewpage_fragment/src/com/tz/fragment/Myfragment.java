package com.tz.fragment;

import com.tz.activity.R;
import com.tz.adapter.T9Adapter;
import com.tz.tools.Utils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Myfragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_dial, null);
		ListView lv = (ListView) v.findViewById(R.id.lv);
		T9Adapter t9Adapter = new T9Adapter(getActivity(), Utils.getCdrMocs(),
				R.layout.t9adapter_item, new int[] { R.id.iv_phone_status,
						R.id.tv_customer_name, R.id.tv_custome_phone });
		lv.setAdapter(t9Adapter);

		return v;
	}
}
