package com.mxz.vp.fragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import com.mxz.vp.R;
import com.mxz.vp.adapter.LVAdapter;
import com.mxz.vp.bean.City;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyFragment extends Fragment {
	private String contries[]=new String[]{"�й�","�ձ�","����","Ӣ��","����","����"};
	private String chinacityName[]=new String[]{"�Ϻ�","�㶫","����","����","�ӱ�","����"};
	private String japancityName[]=new String[]{"����","����","�ձ���","С��","����","���"};
	private String usacityName[]=new String[]{"ŦԼ","������","�����","�ϵ»�","����","����"};
	private String ukcityName[]=new String[]{"�׶�","����","���ƶ�","����","С��","����"};
	private String koracityName[]=new String[]{"�׶�","����","����","��Ů","����","С����"};
	private String nkoracityName[]=new String[]{"ƽ��","С��","�ٶ�","����","����","һ��"};
	
	private int cityimg[]=new int[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	private List<City> data;
	private City city;
	private String cityName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle=this.getArguments();
		int position=bundle.getInt("position");
		TextView tv=new TextView(this.getActivity());
		tv.setText(contries[position]);
		LinearLayout linear=new LinearLayout(this.getActivity());
		initData(position);
		ListView lv=new ListView(this.getActivity());
		LVAdapter adapter=new LVAdapter(this.getActivity(),data);
		lv.setAdapter(adapter);
		return lv;
	}
	public void initData(int position){
		data=new ArrayList<City>();
		for (int i = 0; i < chinacityName.length; i++) {
			city=new City();
			Bitmap bitmap=BitmapFactory.decodeResource(this.getResources(), cityimg[i]);
			city.setBitmap(new SoftReference<Bitmap>(bitmap));
			
			cityName=null;
			switch(position){
			case 0:
				cityName=chinacityName[i];
				break;
			case 1:
				cityName=japancityName[i];
				break;
			case 2:
				cityName=usacityName[i];
				break;
			case 3:
				cityName=ukcityName[i];
				break;
			case 4:
				cityName=koracityName[i];
				break;
			case 5:
				cityName=nkoracityName[i];
				break;
			default: 
				break;
			}
			city.setName(cityName);
			data.add(city);
		}
	}
}
