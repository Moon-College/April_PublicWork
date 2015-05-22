package com.mxz.vp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mxz.vp.adapter.PVAdapter;
import com.mxz.vp.util.ReflexUtil;

public class ViewPager_ProActivity extends FragmentActivity implements OnCheckedChangeListener, OnPageChangeListener {
	private HorizontalScrollView hsv;
	private RadioButton china,japan,usa,uk,kora,nkora;
	private RadioGroup rg;
	private ViewPager vp;
	private View scrollBar;
	private int fromX;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			ReflexUtil.initView(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
        initView();
    }

	private void initView() {
		rg.setOnCheckedChangeListener(this);
		PVAdapter adapter=new PVAdapter(this.getSupportFragmentManager(),rg.getChildCount());
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(this);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int vpPage=0;
		switch(checkedId){
		case R.id.china:
			vpPage=0;
			break;
		case R.id.japan:
			vpPage=1;
			break;
		case R.id.usa:
			vpPage=2;
			break;
		case R.id.uk:
			vpPage=3;
			break;
		case R.id.kora:
			vpPage=4;
			break;
		case R.id.nkora:
			vpPage=5;
			break;
		default:
			break;
		}
		vp.setCurrentItem(vpPage);
	}

	public void onPageScrollStateChanged(int arg0) {
	}
	public void onPageSelected(int arg0) {
	}
	
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		float rBwidth = rg.getChildAt(position).getWidth();
		float total=(position+positionOffset)*rBwidth;
		WindowManager wm=(WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int winwidth=dm.widthPixels;
		float green=(winwidth-rBwidth)/2;
		int dx=(int) (total-green);
		Log.i("INFO", total+"----"+green+"==="+dx);
		this.hsv.scrollTo(dx, 0);
		this.lineScroll(position, positionOffset);
	}
	public void lineScroll(int position,float positionOffset){
		RadioButton rb=(RadioButton) rg.getChildAt(position);
		
		int xy[]=new int[2];
		rb.getLocationInWindow(xy);//取得当前按钮的x跟y坐标，赋给xy[]
		TranslateAnimation ta=new TranslateAnimation(fromX, xy[0]+(positionOffset*rb.getWidth()), 0, 0);//构造方法设置移动位置
		Log.i("INFO", "line_position:"+position+" ##xy[0]:"+xy[0]+"    ##rb.getWidth():"+rb.getWidth()+"   ##fromX:"+fromX+" ##positionOffset："+ positionOffset+"  ##rb.getWidth()："+rb.getWidth());
		ta.setDuration(50);//设置动画执行时间
		ta.setFillAfter(true);//设置动画移动后停留在那个位置
		fromX=(int) (xy[0]+(positionOffset*rb.getWidth()));
		scrollBar.startAnimation(ta);//设置scrollBar开始动画
		
		
	}

	
	
	
}