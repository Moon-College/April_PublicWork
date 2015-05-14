package com.fyf.slidingmenu;

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

public class SlidingMenu extends HorizontalScrollView{
	private int widthPixels;          
	private ViewGroup mLift;
	private ViewGroup mMain;
	private int mLiftOffset;
	private int mMainWidth;	
	float downX = 0;   //按下时x坐标
	float upX = 0;     //松开时x坐标
	
	int viewId = 1;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			int scrollDis = (Integer) msg.obj;
		    SlidingMenu.this.smoothScrollTo(scrollDis, 0);
		};
	};
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		widthPixels = displayMetrics.widthPixels;
		mLiftOffset=widthPixels*3/4;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//设置子控件宽高
		LinearLayout wrapper = (LinearLayout) this.getChildAt(0);
		//获取左边视图
		mLift = (ViewGroup) wrapper.getChildAt(0);
		//获取主视图
		mMain = (ViewGroup) wrapper.getChildAt(1);
				
		mMainWidth = widthPixels - mLiftOffset;
		//设置宽度
		mLift.getLayoutParams().width = mLiftOffset;
		mMain.getLayoutParams().width = widthPixels;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);//调用父类的方法摆放控件
		if(changed){
			//假设视图发生改变
			this.scrollTo(mLiftOffset, 0);
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollx = this.getScrollX();
			int span = widthPixels/16;
			Message msg = mHandler.obtainMessage();
			if(scrollx<span){
				msg.obj = 0;
			}else{
				msg.obj = mLiftOffset;
			}
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//滑动条滑动的监听事件
		//做动画
		float scale = (float)l/mLiftOffset;//向左滑动时从0慢慢到1
		float leftScale = (float)(1.0f - 0.3*scale);//从1缩小到0.7
		//缩放
		ViewHelper.setScaleX(mLift, leftScale);
		ViewHelper.setScaleY(mLift, leftScale);
		//透明度
		ViewHelper.setAlpha(mLift, (float)(1.0f-0.8*scale));//将透明度从1增加到0.2
		ViewHelper.setTranslationX(mLift, l);//保持不被左边移出去
		super.onScrollChanged(l, t, oldl, oldt);
		
		//主界面的动画
		float mainScale = 0.8f + scale*0.2f;
		ViewHelper.setScaleX(mMain, mainScale);
		ViewHelper.setScaleY(mMain, mainScale);
		
	}
	
	

}
