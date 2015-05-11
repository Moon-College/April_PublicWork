package com.tz.slidingmenu;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenuView extends HorizontalScrollView {
	private int widthPixels;
	private ViewGroup ll_menu, ll_main;
	private int menuWidth;
	private int scrollDis;

	public SlidingMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager manager = (WindowManager) context
				.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 父控件的宽高已给出，子控件的宽高需要设置
		LinearLayout ll_all = (LinearLayout) this.getChildAt(0);
		// 得到menu的容器 并设置宽度
		ll_menu = (ViewGroup) ll_all.getChildAt(0);
		menuWidth = (int) (0.75 * widthPixels);
		ll_menu.getLayoutParams().width = menuWidth;
		// 得到main的容器
		ll_main = (ViewGroup) ll_all.getChildAt(1);
		ll_main.getLayoutParams().width = widthPixels;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b); //要放前面，否则changed的不生效
		if(changed){
			this.scrollTo(menuWidth, 0);
		}
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int scrollMsg=(Integer) msg.obj;
			SlidingMenuView.this.smoothScrollTo(scrollMsg, 0);
		};
	};
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action=ev.getAction();
		//移动的距离
		int scrollX=this.getScrollX();
		int span=menuWidth-widthPixels/2;
		switch (action) {
		case MotionEvent.ACTION_UP:
			Message msg=handler.obtainMessage();
			
			if(scrollX<span){
				//显示menu页面
				scrollDis=0;
			}else{
				//显示是主界面
				scrollDis=menuWidth;
			}
			msg.what=0;
			msg.obj=scrollDis;
			msg.sendToTarget();
			//handler.handleMessage(msg);
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		//滑动条的监听事件,l为滑出去的距离
		float scale=(float)l/menuWidth;                  //从0到1
		float leftScale=(float) (1.0f-0.3*scale);         //缩放比例1到0.7
		float leftAlpha =(float) (1.0f-0.8*scale);              //透明度比例1到0.2
		ViewHelper.setScaleX(ll_menu, leftScale);
		ViewHelper.setScaleY(ll_menu, leftScale);
		ViewHelper.setAlpha(ll_menu, leftAlpha);
		ViewHelper.setTranslationX(ll_menu, l*0.7f);
		
		//主界面的缩放
		float rightScale=(float) (0.8+0.2f*scale);
		ViewHelper.setScaleX(ll_main, rightScale);
		ViewHelper.setScaleY(ll_main, rightScale);
	}
}
