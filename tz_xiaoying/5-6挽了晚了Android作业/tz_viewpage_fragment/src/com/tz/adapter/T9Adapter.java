package com.tz.adapter;

import java.util.List;

import com.tz.activity.R;
import com.tz.bean.CdrMoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class T9Adapter extends BaseAdapter {
	private static String TAG = "Marketing_undialAdapter";
	private Context mContext;
	// xml转View对象
	private LayoutInflater mInflater;
	// 单行的布局
	private int mResource;
	// 列表展现的数据
	private List mData;
	// Map中的key
//	private String[] mFrom;
	// view 的id
	private int[] mTo;
	private int imgflag;

	public int getImgflag() {
		return imgflag;
	}

	public void setImgflag(int imgflag) {
		this.imgflag = imgflag;
	}

	public List getmData() {
		return mData;
	}

	public void setmData(List mData) {
		this.mData = mData;
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param data
	 *            列表展现的数据
	 * @param resource
	 *            单行的布局
	 * @param from
	 *            Map中的key
	 * @param to
	 *            view的id
	 */
	public T9Adapter(Context context, List data, int resource,
			int[] to) {
		this.mContext = context;
		this.mData = data;
		this.mResource = resource;
//		this.mFrom = from;
		this.mTo = to;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.t9adapter_item, null);
			// 减少findView的次数
			holder = new ViewHolder();
			holder.iv_phone_status = (ImageView) convertView
					.findViewById(mTo[0]);
			holder.tv_customer_name = (TextView) convertView
					.findViewById(mTo[1]);
			holder.tv_custome_phone = (TextView) convertView
					.findViewById(mTo[2]);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 获得该行的数据
		final CdrMoc obj = (CdrMoc) mData.get(position);
		if (obj.getCalltype() == 0) {
			if (obj.getCustomer().getDialStatus() == 0) {// 呼入未接听
				holder.iv_phone_status
						.setBackgroundResource(R.drawable.weijieting);
			} else {
				holder.iv_phone_status
						.setBackgroundResource(R.drawable.hurujieting);
			}
		} else {
			holder.iv_phone_status
					.setBackgroundResource(R.drawable.huchujietong); 
		}
		holder.tv_customer_name.setText(obj.getCustomer().getName());
		holder.tv_custome_phone.setText(obj.getCustomer().getPhone());  
		return convertView;
	}

	/**
	 * View元素
	 * */
	static class ViewHolder {
		ImageView iv_phone_status;
		TextView tv_customer_name;
		TextView tv_custome_phone;
	}
}
