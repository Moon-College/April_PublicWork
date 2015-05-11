package com.tz.viewpager.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tz.viewpager.MainActivity;
import com.tz.viewpager.R;
import com.tz.viewpager.adapter.ListViewAdapter;

public class CommonFragment extends ListFragment {
	private ListViewAdapter listAdapter;
	private List<Map<String, Object>> datas;
	private String[] titles = new String[] { "途牛CEO:继续加大区域拓展",
			"今年8M以上宽带覆盖率要过半", "华为：诺阿合并未影响公司业务", "京东第一季收入增至366亿元",
			"中华英才网再度上演裁员风波", "问题来了！哪些明星戴苹果表？", "暴风第32次涨停 市值超优士", "大唐双龙 宝石奇兵" };
	private String[] contents = new String[] { "中华英才网被58同城并购 三大原因让它卖身",
			"京东集团发布第一季财报：收入366亿元 净亏损为7亿元", "京东3.5亿美元战略投资途牛：成其单一最大股东 ",
			"微软说想吃掉所有终端市场 但它这条大船能成功掉头吗？", "经纬创投、真格基金联手举办“创大会” 500位创业公司创始人分享经验",
			"工信部还称，今年要新建4G基站超60万个，4G网络覆盖县城和发达乡镇",
			"中华英才网原母公司爱尔兰尚龙集团将退出其在华业务并解除员工合同。",
			"郭平称，诺阿合并后的公司将更加具有竞争力，对整个行业来说是一件好事。" };
	private int[] images = new int[] { R.drawable.face1, R.drawable.face2,
			R.drawable.face3, R.drawable.face4, R.drawable.face5,
			R.drawable.face6, R.drawable.face7, R.drawable.face8,
			R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3,
			R.drawable.pic_4, R.drawable.pic_5, R.drawable.pic_6,
			R.drawable.pic_ad ,R.drawable.pic_file3};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datas = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < titles.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("img", images[i]);
				map.put("title", titles[i]);
				map.put("content", contents[i]);
				datas.add(map);
			}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listAdapter = new ListViewAdapter(getActivity(), datas);
		setListAdapter(listAdapter);
	}
}
