package com.example.buaa.slidingmeun.view;

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

import com.nineoldandroids.view.ViewHelper;


/**
 * Created by Alex on 2015/5/14.
 */
public class MySlidingMenu extends HorizontalScrollView {
    /**
     * 屏幕的宽度
     */
    private int widthPixels;
    /**
     * 左边视图菜单
     */
    private ViewGroup mMenu;
    /**
     * 右边主要视图
     */
    private ViewGroup mMain;
    /**
     * 左边视图 距离 屏幕右边 的边距
     */
    private int mMenurightOffset = 100;
    /**
     * 左边视图菜单的宽度
     */
    private int mMenuWidth;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int scrollDistance = (int) msg.obj;
//            MySlidingMenu.this.scrollTo(scrollDistance,0);
            //平滑的滑动
            MySlidingMenu.this.smoothScrollTo(scrollDistance,0);
            super.handleMessage(msg);
        }
    };
    public MySlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;
        mMenurightOffset = widthPixels/4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到HorizontalScrollView的子控件，并且可以在这里设置子控件的宽高
        LinearLayout wrapper = (LinearLayout) getChildAt(0);
        //获取mMenu
        mMenu = (ViewGroup) wrapper.getChildAt(0);
        //获取mMain
        mMain = (ViewGroup) wrapper.getChildAt(1);
        mMenuWidth = widthPixels - mMenurightOffset;
        //设置mMenu的宽度
        mMenu.getLayoutParams().width = mMenuWidth;
        //设置mMain的宽度  =  屏幕的宽度
        mMain.getLayoutParams().width = widthPixels;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //设置初始默认的位置
        if (changed){
            //如果视图改变，或者说是一开始默认是mMain视图
            scrollTo(widthPixels, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){

            case MotionEvent.ACTION_UP:
                Message msg = handler.obtainMessage();
                //判断已经滑出去的距离
                int scrollX = this.getScrollX();
                //中介线
                int span = mMenuWidth - widthPixels/2;
                if (scrollX < span){
                    //滑到左边的菜单界面
                    msg.obj = 0;
                }else {
                    //滑到右边的主界面
                    msg.obj = mMenuWidth;
                }
                handler.sendMessage(msg);
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    //这个方法里面可以写动画效果
    @Override
    protected void onScrollChanged(int length, int t, int oldl, int oldt) {
        //做动画效果的监听事件   属性动画
        //动画比率，会随着距离length的移动而变化 ,从0增到1
        float leftMenuScale = (float)length/mMenuWidth;

        //1.动画大小缩放
        //动画大小缩放的比率  从1缩小到0.7
        float leftScale = (float)(1.0f - 0.3*leftMenuScale);
        //将menu从1缩小到0.7倍
        ViewHelper.setScaleX(mMenu,leftScale);
        ViewHelper.setScaleY(mMenu,leftScale);

        //2.透明度
        //透明度变化的比率  从1增大到0.2
        float alphaScale = (float)(1.0f - 0.8*leftMenuScale);
        ViewHelper.setAlpha(mMenu,alphaScale);

        //3.位移动画
        //1*0.7f 是 保持不被移除出去
        ViewHelper.setTranslationX(mMenu,length*0.7f);

        //主界面的缩放
        //主界面缩放的比率，跟随平移的距离length，从0.8增加到1
        float rightMainScale = 0.8f + 0.2f*leftMenuScale;
        ViewHelper.setScaleX(mMain,rightMainScale);
        ViewHelper.setScaleY(mMain, rightMainScale);

        super.onScrollChanged(length, t, oldl, oldt);
    }
}