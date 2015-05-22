package com.mxz.scrollview.scrollImpl;

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

public class ScrollImpl extends HorizontalScrollView {

	private int widthPixels;
	private int mMenuRightOffset=100;
	private ViewGroup mMenu;
	private ViewGroup mMain;
	private int mMenuWidth;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int scrollDis=(Integer) msg.obj;
			ScrollImpl.this.smoothScrollTo(scrollDis, 0);
		};
	};
	
	public ScrollImpl(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		widthPixels = displayMetrics.widthPixels;
		mMenuRightOffset = widthPixels/4;
		
	}
	
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LinearLayout linear=(LinearLayout) this.getChildAt(0);
		mMenu=(ViewGroup) linear.getChildAt(0);
		mMain=(ViewGroup) linear.getChildAt(1);
		mMenuWidth=widthPixels-mMenuRightOffset;
		mMenu.getLayoutParams().width=mMenuWidth;
		mMain.getLayoutParams().width=widthPixels;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(mMenuWidth, 0);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action=ev.getAction();
		switch(action){
		case MotionEvent.ACTION_UP:
			Message msg=handler.obtainMessage();
			int scrollX=this.getScrollX();
			int span=mMenuWidth-widthPixels/2;
			if(scrollX<span){
				msg.obj=0;
			}else{
				msg.obj=mMenuWidth;
			}
			handler.sendMessage(msg);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//滚动监听，可以做动画
	    float scale=(float)l/mMenuWidth;
	    float leftScale=(float) (1.0f-0.3f*scale);
	    ViewHelper.setScaleX(mMenu, leftScale);
	    ViewHelper.setScaleY(mMenu, leftScale);
//	    float alpha=(float)(1.0f-0.8*scale);
	    ViewHelper.setAlpha(mMenu, (float)(1.0f-0.8*scale));
	    ViewHelper.setTranslationX(mMenu, l*0.7f);
	    
	    float rightScale=0.8f+scale*0.2f;
	    ViewHelper.setScaleX(mMain, rightScale);
	    ViewHelper.setScaleY(mMain, rightScale);
		super.onScrollChanged(l, t, oldl, oldt);
		
		
	}

}
