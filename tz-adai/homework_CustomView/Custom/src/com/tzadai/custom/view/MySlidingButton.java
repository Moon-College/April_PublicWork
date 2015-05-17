package com.tzadai.custom.view;

import com.tzadai.custom.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MySlidingButton extends View {
	
	private Bitmap gray_bg;
	private Bitmap green_bg;
	private Bitmap btn;
	private Bitmap num_price;
	private int bg_height;
	private float scale_h;
	private Paint paint;
	private final float REAL_PER = 0.95f;
	private float btn_x;
	private int price_u;
	private int price_d;
	private float span;
	private float half_round;
	private float y_u;
	private float y_d;
	private boolean isUpTouched,isDownTouched;
	
	public MySlidingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		gray_bg = BitmapFactory.decodeResource(getResources(), R.drawable.axis_before);
		green_bg = BitmapFactory.decodeResource(getResources(),R.drawable.axis_after);
		btn = BitmapFactory.decodeResource(getResources(), R.drawable.btn);
		num_price = BitmapFactory.decodeResource(getResources(), R.drawable.bg_number);
		paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setAntiAlias(true);
		price_u = 1000;
		price_d = 200;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		bg_height = gray_bg.getHeight();
		int measuredHeight = (modeHeight == MeasureSpec.EXACTLY)?sizeHeight:bg_height;
		int measuredWidth = (int)(measuredHeight* 0.618f);
		scale_h = (float)measuredHeight/bg_height;
		span = bg_height*REAL_PER/4;
		half_round = bg_height*(1-REAL_PER)/2;
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.scale(scale_h, scale_h);
		
		int bg_x = (int) ((this.getWidth()/scale_h - gray_bg.getWidth())/2);
		canvas.drawBitmap(gray_bg, bg_x, 0, null);
		
		
		String [] numbers = new String[]{"不限","御姐","萌妹子","萝莉","女汉子"};
		paint.setTextSize(20/scale_h);
		for(int i = 0;i<numbers.length;i++){
			float text_y = i*span+half_round+paint.descent();
			canvas.drawText(numbers[i], 5*bg_x/4, text_y, paint);
		}
		
		//画拖动的大圆
		btn_x = (this.getWidth()/scale_h-btn.getWidth())/2;
		y_u = getBtnLocationByPrice(price_u);
		canvas.drawBitmap(btn, btn_x, y_u-btn.getHeight()/2, null);
		y_d = getBtnLocationByPrice(price_d);
		canvas.drawBitmap(btn, btn_x, y_d-btn.getHeight()/2, null);
		
		//画绿色价格滑竿
		Rect src = new Rect(0,(int)(y_u+btn.getHeight()/2),green_bg.getWidth(),(int)(y_d-btn.getHeight()/2));
		Log.i("INFO", "src的 左:" + 0 + " 上:"+ (int)(y_u+btn.getHeight()/2) + " 右:" +(green_bg.getWidth())+ " 下:"+(int)(y_d-btn.getHeight()/2));
		Rect dst = new Rect(bg_x, (int)(y_u+btn.getHeight()/2), bg_x+green_bg.getWidth(), (int)(y_d-btn.getHeight()/2));
		Log.i("INFO", "dst的 左:" + bg_x + " 上:"+ (int)(y_u+btn.getHeight()/2) + " 右:" +(bg_x+green_bg.getWidth())+ " 下:"+(int)(y_d-btn.getHeight()/2));
		canvas.drawBitmap(green_bg, src, dst, paint);
		
		//画坐标的价格游标矩形
		Rect rect_u = getRectByMidLine(y_u);
		canvas.drawBitmap(num_price, null, rect_u, paint);
		Rect rect_d = getRectByMidLine(y_d);
		canvas.drawBitmap(num_price, null, rect_d, paint);
		
		//找到价格游标上文本的x,y坐标
		float text_u_x = (3*rect_u.width()/4-paint.measureText(String.valueOf(price_u)))/2;
		float text_u_y = rect_u.top - paint.ascent();
		float text_d_x = (3*rect_u.width()/4-paint.measureText(String.valueOf(price_d)))/2;
		float text_d_y = rect_d.top - paint.ascent();
		//画价格游标上的文本
		canvas.drawText(String.valueOf(price_u), text_u_x, text_u_y, paint);
		canvas.drawText(String.valueOf(price_d), text_d_x, text_d_y, paint);
		
		canvas.restore();
		super.onDraw(canvas);
	}
	
	/**
	 * 根据价格获取大圆的Y坐标
	 * @author tzadai
	 * @param price 价格
	 * @return 大圆的Y坐标
	 */
	public float getBtnLocationByPrice(int price){
		float y = 0;
		if (price < 0) {
			price = 0;
		}
		if (price > 10000) {
			price = 10000;
		}
		if (0<=price && price<200) {
			y = bg_height - span*price/200-half_round;
		}else if(200<=price && price<500){
			y = bg_height - span*(price-200)/(500-200)-span-half_round;
		}else if (500<=price && price<1000) {
			y = bg_height - span*(price-500)/(1000-500)-2*span-half_round;
		}else {
			y = span*(10000-price)/(10000-1000)+half_round;
		}
		return y;
	}
	
	/**
	 * 根据大圆的中线确定矩形价格游标的坐标
	 * @author tzadai
	 * @param y 大圆的中线Y坐标
	 * @return 矩形价格游标
	 */
	public Rect getRectByMidLine(float y){
		//计算文本的高
		float text_height = paint.descent()-paint.ascent();
		//矩形的左、上、右、下
		Rect rect = new Rect();
		rect.left = 0; //左
		rect.top =(int) (y-text_height/2);//上
		rect.right = (int) btn_x;//右
		rect.bottom = (int) (y+text_height/2);//下
		return rect;
	}
	
	public int getPriceByPosition(float y){
		int price;
		float span = REAL_PER*this.getHeight()/4;
		float halt_Height = this.getHeight()*(1-REAL_PER)/2;
		if (y<halt_Height) {
			price = 10000;			
		}else if(y>=halt_Height&&y<halt_Height+span){
			price = (int) (10000-(10000-1000)*(y-halt_Height)/span);
		}else if (y>=halt_Height+span&&y<halt_Height+2*span) {
			price = (int) (1000-(1000-500)*(y-halt_Height-span)/span);
		}else if (y>=halt_Height+2*span&&y<halt_Height+3*span) {
			price = (int) (500-(500-200)*(y-halt_Height-2*span)/span);
		}else {
			price = (int) (200-(200-0)*(y-halt_Height-3*span)/span);
		}
		if (price<0) {
			price = 0;
		}
		if (price<=1000) {
			int mol = price%20;
			if (mol>=10) {
				price = price-mol+20;
			}else {
				price = price - mol;
			}
		}
		if (price>1000) {
			int mol = price%1000;
			if (mol>=500) {
				price = price -mol + 1000;
			}else {
				price = price - mol;
			}
		}
		return price;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			float x = event.getX()/scale_h;
			float y = event.getY()/scale_h;
			if (x>=btn_x&&x<=btn_x+btn.getWidth()) {
				if (y>=y_u-btn.getHeight()/2&&y<=y_u+btn.getHeight()/2) {
					isUpTouched = true;
					isDownTouched = false;
				}
				if (y>=y_d-btn.getHeight()/2&&y<=y_d+btn.getHeight()/2) {
					isDownTouched = true;
					isUpTouched = false;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float move_y = event.getY();
			if (isUpTouched) {
				price_u = getPriceByPosition(move_y);
				if (price_u<price_d) {
					price_u=price_d;
				}
			}
			if (isDownTouched) {
				price_d = getPriceByPosition(move_y);
				if (price_d>price_u) {
					price_d = price_u;
				}
			}
			this.invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isUpTouched = false;
			isDownTouched = false;
			break;
		default:
			break;
		}
		return true;
	}
}
