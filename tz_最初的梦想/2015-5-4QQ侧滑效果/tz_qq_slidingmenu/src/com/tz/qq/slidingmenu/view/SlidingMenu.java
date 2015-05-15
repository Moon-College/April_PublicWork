package com.tz.qq.slidingmenu.view;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
/**
 * 自定义侧滑菜单
 * @author 姚坤
 *
 */
public class SlidingMenu extends HorizontalScrollView {
	
	private int windWidth;
	private ViewGroup mMenu;
	private ViewGroup mMain;
	private int mMenuRightOffset=100;
	private int mMenuWidth;
	private VelocityTracker vt=null;
	/**
	 * 速度临界值
	 */
	private float criticalSpeed=1200;
	//手指滑动的速度
	private float xSpeed;

	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int srcollX=(Integer) msg.obj;
			//缓慢滚动scrollX的距离
			SlidingMenu.this.smoothScrollTo(srcollX, 0);
		};
	};
	
	
	public SlidingMenu(Context context) {
		super(context);
	}
	
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	private void init(Context context) {
		WindowManager winManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics=new DisplayMetrics();
		winManager.getDefaultDisplay().getMetrics(displayMetrics);
		windWidth=displayMetrics.widthPixels;
		mMenuRightOffset=windWidth/4;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//在此方法中设置子控件的宽度和高度
		
		LinearLayout wrapper=(LinearLayout) this.getChildAt(0);
		//获取左边的相对布局菜单容器
		mMenu=(ViewGroup) wrapper.getChildAt(0);
		//获取右边的线性容器
		mMain=(ViewGroup) wrapper.getChildAt(1);
		mMenuWidth=windWidth-mMenuRightOffset;
		//设置宽度和高度
		mMenu.getLayoutParams().width=mMenuWidth;
		mMain.getLayoutParams().width=windWidth;
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//先保持默认布局放好
		super.onLayout(changed, l, t, r, b);
		//再调整成自己想要的布局
		if(changed){
			this.scrollTo(mMenuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action=ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if(vt==null){
				//初始化VelocityTracker的对象，用来检测MotionEvent的动作
				vt=VelocityTracker.obtain();
			}else{
				vt.clear();
			}
			vt.addMovement(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			vt.addMovement(ev);
			//检测每1000毫秒手指移动的距离
			vt.computeCurrentVelocity(1000);
			xSpeed=vt.getXVelocity();
			//System.out.println("move："+xSpeed);
			break;
		case MotionEvent.ACTION_UP:
			//获取滚动的offset
			int scrollX=this.getScrollX();
			//获取中间值 左边菜单width-屏幕宽度的一半=滑到临界点时向左滚共的距离
			int span=mMenuWidth-windWidth/2;
			Message msg=handler.obtainMessage();
			//System.out.println("move："+xSpeed);
			
			if(xSpeed>0){//手指向右滑动
				if(xSpeed>criticalSpeed||scrollX<span){
					msg.obj=0;
				}else{
					msg.obj=mMenuWidth;
				}
			}else{//手指向左滑动
				if(scrollX>=span||xSpeed<-criticalSpeed){
					msg.obj=mMenuWidth;
				}else{
					msg.obj=0;
				}
			}
			//发送消息给handler
			msg.sendToTarget();
			break;
		case MotionEvent.ACTION_CANCEL:
			vt.recycle();
			System.out.println("ACTION_CANCEL");
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//l表示向左滚动出屏幕的距离
		//设置动画 假设向左移动
		//获取缩放变化比例
		float scale=l/(mMenuWidth*1f);//从0变化到1 注意这里要用float类型不能用int类型
		//System.out.println("scale"+scale);
		float leftScale=1-0.3f*scale;//从1变化到0.7
		//缩放动画
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		//透明度改变
		ViewHelper.setAlpha(mMenu,1-0.7f*scale);//透明度从1变化到0.3
		//平移动画 做抗拒运动 translationX为正增大表示向右平移
		ViewHelper.setTranslationX(mMenu,l*0.7f);//不完全强烈抗拒
		
		float rightScale=0.8f+0.2f*scale; //从0.8变化到1
		//缩放动画
		ViewHelper.setScaleX(mMain, rightScale);
		ViewHelper.setScaleY(mMain, rightScale);
		
		super.onScrollChanged(l, t, oldl, oldt);
	}

}
