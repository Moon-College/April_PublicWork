package com.tz.view;

import com.tz.activity.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class McustomView extends View {
	private String TAG = "McustomView";
	private Bitmap bg_gray;// 灰色滑竿
	private Bitmap bg_green;//绿色滑竿
	private Bitmap btn;// 画大饼
	private Bitmap bg_number;// 价格
	private int bgHeight;
	private Paint paint;// 画笔
	private float scale_h;
	private float REAL_PER = 0.95f;// 去掉一个球的比例
	private int price_u;// 上限
	private int price_d;// 下限
	private float HalfSphere;// 一个半球的距离
	private float span;
	private float x_u;// 绘制大饼的x坐标
	private int bg_x;
	private int PRICE_PADDING = 18;
	private float y_u;
	private float y_d;
	private boolean isUpTouched;// 记录上饼是否被按到
	private boolean isDownTouched;// 记录下饼是否被按到
	// 价格的区间价
	private final int FIRST_STAGE = 0;
	private final int SECOND_STAGE = 200;
	private final int THIRD_STAGE = 500;
	private final int FOUTH_STAGE = 1000;
	private final int FIFTH_STAGE = 10000;

	public McustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bg_gray = BitmapFactory.decodeResource(getResources(),
				R.drawable.axis_before);
		btn = BitmapFactory.decodeResource(getResources(), R.drawable.btn);
		bg_number = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_number);
		bg_green=BitmapFactory.decodeResource(getResources(),R.drawable.axis_after);
		paint = new Paint();
		paint.setColor(Color.GRAY);
		// price_u = 1000;
		// price_d = 200;
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.MyPrice);
		price_u = array.getInt(R.styleable.MyPrice_price_u, 1200);
		price_d = array.getInt(R.styleable.MyPrice_price_d, 200);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthsize = MeasureSpec.getSize(widthMeasureSpec);
		int widthmode = MeasureSpec.getMode(widthMeasureSpec);
		int heightsize = MeasureSpec.getSize(heightMeasureSpec);
		int heightmode = MeasureSpec.getMode(heightMeasureSpec);
		bgHeight = bg_gray.getHeight();
		int measureHeight = (heightmode == MeasureSpec.EXACTLY) ? heightsize
				: bgHeight;
		HalfSphere = bgHeight * (1 - REAL_PER) / 2;
		scale_h = (float) measureHeight / bgHeight;
		span = REAL_PER * bgHeight / 4;// 文字区间_去掉两个半球的距离了
		int mesureWidth = 2 * measureHeight / 3;

		setMeasuredDimension(mesureWidth, measureHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(scale_h, scale_h);
		bg_x = (int) ((this.getWidth() / scale_h - bg_gray.getWidth()) / 2);// 灰色滑竿的x
		canvas.drawBitmap(bg_gray, bg_x, 0, null);
		String[] numbers = new String[] { "不限", String.valueOf(FOUTH_STAGE),
				String.valueOf(THIRD_STAGE), String.valueOf(SECOND_STAGE),
				String.valueOf(FIRST_STAGE) };
		// 调整文字的大小
		paint.setTextSize(20 / scale_h);
		for (int i = 0; i < numbers.length; i++) {
			int x = 5 * bg_x / 4;

			// float
			// span=gray_axis.getHeight()/4-(1-REAL_PER)*gray_axis.getHeight();
			// 1-REAL_REl是指一个球的距离
			float y = i * span + HalfSphere + paint.descent();// 第i个文本的y坐标
			canvas.drawText(numbers[i], x, y, paint);   
		}

		y_u = getBtnLocationByPrice(price_u);//得到上饼的Y坐标
		x_u = (this.getWidth() / scale_h - btn.getWidth()) / 2;//得到上饼的X坐标
		canvas.drawBitmap(btn, x_u, y_u - btn.getHeight() / 2, null);//得到下饼的X坐标
		y_d = getBtnLocationByPrice(price_d);//得到下饼饼的Y坐标
		float x_d = (this.getWidth() / scale_h - btn.getWidth()) / 2;
		canvas.drawBitmap(btn, x_d, y_d - btn.getHeight() / 2, null);
         
		//画绿色滑竿
		//画绿色滑竿
		Rect src = new Rect(0, (int)(y_u+btn.getHeight()/2), (bg_green.getWidth()), (int)(y_d-btn.getHeight()/2));
		Rect dst = new Rect(bg_x,(int)(y_u+btn.getHeight()/2),bg_x+bg_green.getWidth(),(int)(y_d-btn.getHeight()/2));
		canvas.drawBitmap(bg_green, src, dst, paint);
		
		// 绘制上限价格
		Rect rect_u = getMyRect(y_u);
		canvas.drawBitmap(bg_number, null, rect_u, null);
		// 绘制下限价格
		Rect rect_d = getMyRect(y_d);
		canvas.drawBitmap(bg_number, null, rect_d, null);

		// 绘制左边上限价格的文字
		float text_ux = ((3 * rect_u.width() / 4) - paint.measureText(String
				.valueOf(price_u))) / 2;
		float text_uy = rect_u.top - paint.ascent() + PRICE_PADDING;
		canvas.drawText(String.valueOf(price_u), text_ux, text_uy, paint);

		// 绘制左边下限价格的文字
		float text_dx = ((3 * rect_u.width() / 4) - paint.measureText(String
				.valueOf(price_d))) / 2;
		float text_dy = rect_d.top - paint.ascent() + PRICE_PADDING;
		canvas.drawText(String.valueOf(price_d), text_dx, text_dy, paint);
		canvas.restore();
	}

	/**
	 * 根本价格来获得Y坐标
	 * 
	 * @param price
	 * @return
	 */
	private float getBtnLocationByPrice(int price) {
		// TODO Auto-generated method stub
		float y = 0;
		if (price > FIFTH_STAGE) {
			y = FIFTH_STAGE;
		}
		if (price < FIRST_STAGE) {
			y = FIRST_STAGE;
		}
		if (FIRST_STAGE <= price && price < SECOND_STAGE) {
			y = bgHeight - span * price / SECOND_STAGE - HalfSphere;
		} else if (SECOND_STAGE <= price && price < THIRD_STAGE) {
			y = bgHeight - span * (price - SECOND_STAGE)
					/ (THIRD_STAGE - SECOND_STAGE) - span - HalfSphere;
		} else if (THIRD_STAGE <= price && price < FOUTH_STAGE) {
			y = bgHeight - span * (price - 500) / (FOUTH_STAGE - THIRD_STAGE)
					- 2 * span - HalfSphere;
		} else {
			// y=bg_gray.getHeight()-span*(price-1000)/(10000-1000)-3*span-bg_gray.getHeight()*(1-REAL_PER)/2;
			y = span * (FIFTH_STAGE - price) / (FIFTH_STAGE - FOUTH_STAGE)
					+ HalfSphere;
		}
		return y;
	}

	/**
	 * 根据大饼的中线确定坐标价格游标的矩形
	 * 
	 * @param y
	 * @return
	 */
	private Rect getMyRect(float y) {
		// TODO Auto-generated method stub
		Rect r = new Rect();
		// 计算文本的高
		float text_h = paint.descent() - paint.ascent();
		r.left = 0;
		r.right = (int) x_u;
		r.top = (int) (y - text_h / 2) - PRICE_PADDING;
		r.bottom = (int) (y + text_h / 2) + PRICE_PADDING;
		return r;
	}

	/**
	 * 根据y坐标获取价格
	 * 
	 * @param price_u2
	 * @return
	 */
	private int getBtnPriceByLocation(float y) {
		// TODO Auto-generated method stub
		int price = 0;
		float span = REAL_PER * this.getHeight() / 4;// 分区间显示
		float half_height = this.getHeight() * (1 - REAL_PER) / 2;
		if (y < half_height) {
			price = FIFTH_STAGE;
		} else if (y > half_height && y < span) {
			price = (int) (FIFTH_STAGE - (FIFTH_STAGE - FOUTH_STAGE)
					* (y - half_height) / span);
		} else if (y > half_height + span && y < half_height + 2 * span) {
			price = (int) (FOUTH_STAGE - (FOUTH_STAGE - THIRD_STAGE)
					* (y - half_height - span) / span);
		} else if (y > half_height + 2 * span && y < half_height + 3 * span) {
			price = (int) (THIRD_STAGE - (THIRD_STAGE - SECOND_STAGE)
					* (y - half_height - 2 * span) / span);
		} else {
			// 0-200区间
			price = (int) ((SECOND_STAGE - FIRST_STAGE) * ((this.getHeight()
					- y - half_height) / span));
		}
		if (price < FIRST_STAGE) {
			price = FIRST_STAGE;
		}
		// 对价格进行精度计算
		if (price <= 1000) {
			int mol = price % 20;
			if (mol >= 10) {
				price = price - mol + 20;
			} else {
				price = price - mol;
			}
		}
		if (price > 1000) {
			int mol = price % 1000;
			if (mol >= 500) {
				price = price - mol + 1000;
			} else {
				price = price - mol;
			}
		}
		return price;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			float x = event.getX() / scale_h;
			float y = event.getY() / scale_h;
			if (y >= y_u - btn.getHeight() / 2
					&& y <= (y_u + btn.getHeight() / 2)) {
				isUpTouched = true;
				isDownTouched = false;
			}
			if (y >= y_d - btn.getHeight() / 2
					&& y <= (y_d + btn.getHeight() / 2)) {
				isDownTouched = true;
				isUpTouched = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			isUpTouched = false;
			isDownTouched = false;

			break;
		case MotionEvent.ACTION_MOVE:
			float y2 = event.getY();
			if (isUpTouched) {
				price_u = getBtnPriceByLocation(y2);
				if (price_u < price_d) {
					price_u = price_d;
				}
			}
			if (isDownTouched) {
				price_d = getBtnPriceByLocation(y2);
				if (price_d > price_u) {
					price_d = price_u;
				}
			}

			this.invalidate();// 重绘
			break;

		default:
			Log.i(TAG, "误操作");
			break;
		}
		return true;
	}

}
