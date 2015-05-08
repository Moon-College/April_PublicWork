package com.ckview.qqslidingmenu.layout;

import com.ckview.tools.log.BaseLog;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

public class MyHorizontalScrollView extends HorizontalScrollView{
	
	private ViewGroup mainLinearLayout;
	private ViewGroup menu;
	private ViewGroup mainView;
	private int windowWidth;
	private int menuWidth;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int scrollDis = (int) msg.obj;
			MyHorizontalScrollView.this.smoothScrollTo(scrollDis, 0);
			Toast.makeText(getContext(), ""+scrollDis, 1000).show();
		}
	};
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mainLinearLayout = (ViewGroup) this.getChildAt(0);
		menu = (ViewGroup) mainLinearLayout.getChildAt(0);
		mainView =  (ViewGroup) mainLinearLayout.getChildAt(1);
		menuWidth = (int) (windowWidth*0.75);
		menu.getLayoutParams().width = menuWidth;
		mainView.getLayoutParams().width = windowWidth;
		BaseLog.debug("debug", "menuWidth="+menuWidth);
		BaseLog.debug("debug", "mainView="+windowWidth);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		windowWidth = displayMetrics.widthPixels;
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(menuWidth, 0);
			BaseLog.debug("debug", "scrollTo"+menuWidth);
			BaseLog.debug("debug", "scrollTo"+this.getScrollX());
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		BaseLog.debug("debug", ""+l);
		float scale = (float)l/menuWidth;
		float leftScale = (float) (1 - (0.3*scale));
		BaseLog.debug("debug", ""+leftScale);
		ViewHelper.setScaleX(menu, leftScale);
		ViewHelper.setScaleY(menu, leftScale);
		ViewHelper.setAlpha(menu, 1-scale);
		ViewHelper.setTranslationX(menu, (float) (l*0.8));
		
		float scale1 = (float)(menuWidth-l)/menuWidth;
		float rightScale = (float) (1 - (0.2*scale1));
		BaseLog.debug("debug1", ""+l);
		BaseLog.debug("debug1", ""+scale1);
		BaseLog.debug("debug1", ""+rightScale);
		ViewHelper.setScaleX(mainView, rightScale);
		ViewHelper.setScaleY(mainView, rightScale);
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			Message msg = handler.obtainMessage();
			if(MyHorizontalScrollView.this.getScrollX() < windowWidth/4){
				msg.obj = 0;
			}else{
				msg.obj = menuWidth;
			}
			handler.sendMessage(msg);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
}
