package com.mxz.scrollview;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mxz.scrollview.adapter.MyListAdapter;
import com.mxz.scrollview.bean.MyMenu;
import com.mxz.scrollview.scrollImpl.ScrollImpl;
import com.mxz.scrollview.util.ReflexUtil;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {
	private TextView tv_1,tv_2,tv_3,tv_4,tv_5,lv_tv;
	private ImageView lv_img;
	private ScrollImpl scroll;
	private ListView lv;
	private List<MyMenu> data;
	private MyListAdapter adapter=null;
	private int img[]=new int[]{R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,R.drawable.img_5};
	private String text[]=new String[]{"我的邮件","我的相册","qq空间","我的地图","我的相机"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        try {
			ReflexUtil.initView(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
        initData();
        
    }
	private void initData() {
		data=new ArrayList<MyMenu>();
		MyMenu myMenu=null;
		for (int i = 0; i < img.length; i++) {
			myMenu=new MyMenu();
			Bitmap bitmap=BitmapFactory.decodeResource(this.getResources(), img[i]);
			myMenu.setBitmap(new SoftReference<Bitmap>(bitmap));
//			myMenu.setBitmap(new SoftReference<Bitmap>(null));
			myMenu.setImgId(img[i]);
			myMenu.setText(text[i]);
			data.add(myMenu);
		}
		adapter=new MyListAdapter(this,data);
        this.lv.setAdapter(adapter);
        this.lv.setOnItemClickListener(this);
	}
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tv_1:
			Toast.makeText(this, "tv_1", 1000).show();
			resetMain();
			break;
		case R.id.tv_2:
			Toast.makeText(this, "tv_2", 1000).show();
			resetMain();
			break;
		case R.id.tv_3:
			Toast.makeText(this, "tv_3", 1000).show();
			resetMain();
			break;
		case R.id.tv_4:
			Toast.makeText(this, "tv_4", 1000).show();
			resetMain();
			break;
		case R.id.tv_5:
			Toast.makeText(this, "tv_5", 1000).show();
			resetMain();
			break;
		default:
			break;
		}
	}
	public void resetMain(){
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		int widthPixels = displayMetrics.widthPixels;
		int mMenuRightOffset = widthPixels/4;
		scroll.scrollTo(widthPixels-mMenuRightOffset, 0);
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String text=data.get(position).getText();
		Toast.makeText(this, text, 1000).show();
		resetMain();
	}
	
    
}