package com.tz.custom.view;

import com.tz.custom.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class CustomView  extends View{
	//背景中间的柱子
	private Bitmap gray_bg;
	private float scale_h;
	private int bg_height;
	private  int[] numbers={10000,1000,500,200,0};
	private Paint paint;
	private Bitmap btn;
	//假设一个饼的大小
	private int pic;
	//价格的区域
	private Bitmap bg_number;
	private int text_u=200;
	private int text_d=500;
	/**
	 * 初始化控件
	 * @param context
	 * @param attrs
	 */
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//首先初始化化控件
		gray_bg=BitmapFactory.decodeResource(getResources(), R.drawable.axis_after);
		btn=BitmapFactory.decodeResource(getResources(), R.drawable.btn);
		bg_number=BitmapFactory.decodeResource(getResources(), R.drawable.bg_number);
		paint = new Paint();
		paint.setColor(Color.RED);
		
	}
	
	/**
	 * 测量
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//父容易指定的

		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		//自己设置的
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		//计算当设置为wrap_content的时候控件的宽和高该是多少
		bg_height = gray_bg.getHeight();
		int measuredHeight = (modeHeight == MeasureSpec.EXACTLY)?sizeHeight:bg_height;
		int measuredWidth = 2*measuredHeight/3;
		//设置一个饼的大小
		pic= measuredHeight/20;
		
		//画布缩放
		scale_h = (float)measuredHeight/bg_height;
		//设置该控件宽高
		setMeasuredDimension(measuredWidth, measuredHeight);
		Log.i("info", "gao:"+measuredHeight);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		//保存这次的状态
		canvas.save();
		//设置画布的缩放
		canvas.scale(scale_h, scale_h);
		//设置中间柱子的放的X坐标
		int bg_x = (int) ((this.getWidth()/scale_h - gray_bg.getWidth())/2);
		canvas.drawBitmap(gray_bg, bg_x,0, null);
		//设置文字
		paint.setTextSize(20/scale_h);
		Log.d("info", "假设饼的大小："+pic);
		Log.d("info", "控件的总高度："+bg_height);
		
		for (int i = 0;i<numbers.length;i++) {
			//100/5   0 20  40 60 80 
			float text_y=((bg_height-pic)/(numbers.length-1))*i+(pic/2)+paint.descent();
			Log.d("info", "第"+i+"个饼的高度:"+text_y);
			canvas.drawText(numbers[i]+"", 5*bg_x/4, text_y, paint);
		}
		 
		canvas.drawText("你好",0,this.getHeight(), paint);
		float y_u= getBtnLocationByPrice(text_u);
		canvas.drawBitmap(btn, bg_x,y_u-btn.getHeight()/2, paint);
		canvas.drawBitmap(bg_number,0,y_u-(pic/2),paint);
		
		float y_d= getBtnLocationByPrice(text_d);
		canvas.drawBitmap(btn, bg_x,y_d-btn.getHeight()/2, paint);
		canvas.drawBitmap(bg_number,0,y_d-(pic/2),paint);
		//设置图片中文字的位置
		canvas.drawText(text_u+"" ,bg_number.getWidth()/3,y_u+(18/scale_h), paint);
		canvas.drawText(text_d+"" ,bg_number.getWidth()/3,y_d+(18/scale_h), paint);
		//重置画布
		canvas.restore();
		super.onDraw(canvas);
	}
	
	/*
	 * 根据价格得到坐标
	 */
	public float getBtnLocationByPrice(int price){
		float y = 0;
		if(price<=0){
			price = 1;
		}
		if(price>10000){
			price = 10000;
		}
		//记录每个区间的大小
		//从第一个饼开始算
		float between= (bg_height-pic)/(numbers.length-1);
		for(int i=0 ; i<numbers.length-1;i++){
			if(price<=numbers[i]&&price>numbers[i+1]){
				y= (between*i)+between*(numbers[i]-price)/(numbers[i]-numbers[i+1])+(pic/2);
			}
		}
		Log.i("INFO", "y坐标:"+y+"      price:"+price);
		return y;
	}
	/**
	 * 根据坐标得到价格
	 */
	/*
	 * 根据价格得到坐标
	 */
	public float getPriceByLocation(float y){
		
	}

}
