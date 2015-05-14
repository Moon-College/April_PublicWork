package com.tz.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tz.slidingbar.R;

public class SlidingBarView extends View {
	private final static int FIRST_STAGE = 0;
	private final static int SECOND_STAGE = 200;
	private final static int THIRD_STAGE = 500;
	private final static int FOURTH_STAGE = 1000;
	private final static int FIFTH_STAGE = 10000;
	private Bitmap slidingBar, priceBar, slidingBall, greenSlidlingBar;
	private float mScale; // 画布缩放比例
	private int bg_height; // 滑竿的高度
	private Paint paint;
	private final float mBallScale = 0.05f;// 滑竿小球直径所占滑竿的比率
	private final float xBallScale = 5 / 8.0f; // 文本横坐标坐标占控件宽度的比率
	private float span;
	private int price_up;
	private int price_down;
	private float radius; // 小圆的半径
	private int slidingBallRadius;
	private int x_bg;
	private float dx; // 大饼的x坐标
	private final int PADDING = 15;
	private float dy_up;
	private float dy_down;
	boolean isUpSlidingBall = false;
	boolean isDownSlidingBall = false;

	public SlidingBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化时，把图片加载进来，计算出宽高
		slidingBar = BitmapFactory.decodeResource(getResources(),
				R.drawable.axis_before);
		priceBar = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_number);
		slidingBall = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn);
		greenSlidlingBar = BitmapFactory.decodeResource(getResources(),
				R.drawable.axis_after);
		paint = new Paint();
		paint.setColor(Color.GRAY);
		bg_height = slidingBar.getHeight(); // 杆子高度
		radius = mBallScale / 2 * bg_height; // 小球半径
		span = (bg_height - mBallScale * bg_height) / 4; // 相邻小圆之间的距离
		slidingBallRadius = slidingBall.getHeight() / 2;

		// 把自定义的属性加载出来
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.slidingbar);
		price_up = array.getInt(R.styleable.slidingbar_price_up, 1000);
		price_down = array.getInt(R.styleable.slidingbar_price_down, 300);
	}

	public int getPrice_up() {
		return price_up;
	}

	public void setPrice_up(int price_up) {
		this.price_up = price_up;
	}

	public int getPrice_down() {
		return price_down;
	}

	public void setPrice_down(int price_down) {
		this.price_down = price_down;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取父容器设定的宽高和模式
		int mWidthMeasure = MeasureSpec.getSize(widthMeasureSpec);
		int mHeightMeasure = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 根据用户在xml文件中设置的模式来设置控件宽高
		int mHeight = (heightMode == MeasureSpec.EXACTLY) ? mHeightMeasure
				: bg_height;
		int mWidth = 2 * mHeight / 3;
		// 画布缩放的比例
		mScale = (float) mHeight / bg_height;
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 保存这一次的状态
		canvas.save();
		// 画布缩放
		canvas.scale(mScale, mScale);
		// 计算滑竿居中时x的坐标
		x_bg = (int) ((this.getWidth() / mScale - slidingBar.getWidth()) / 2);
		canvas.drawBitmap(slidingBar, x_bg, 0, null);
		// 文本内容
		String[] contents = new String[] { String.valueOf(FIFTH_STAGE),
				String.valueOf(FOURTH_STAGE), String.valueOf(THIRD_STAGE),
				String.valueOf(SECOND_STAGE), String.valueOf(FIRST_STAGE) };
		// 文本内容也需要缩放
		paint.setTextSize(20 / mScale);
		// 把文本画上去，计算文本的坐标
		// 文本x坐标
		float x_ball = xBallScale * (this.getWidth() / mScale);
		for (int i = 0; i < contents.length; i++) {
			// 文本y坐标
			float y_ball = radius + i * span + paint.descent();
			canvas.drawText(contents[i], x_ball, y_ball, paint);
		}

		// 计算大饼x坐标
		dx = (this.getWidth() / mScale - slidingBall.getWidth()) / 2;
		// 计算上面大饼y坐标并画出
		// Log.i("INFO", "上下价格分别是：" + price_up + "--" + price_down);
		dy_up = getSlidingBallLocationByPrice(price_up);
		Log.i("INFO", "dy_up：" + dy_up + isUpSlidingBall);
		canvas.drawBitmap(slidingBall, dx, dy_up - slidingBallRadius, paint);
		dy_down = getSlidingBallLocationByPrice(price_down);
		canvas.drawBitmap(slidingBall, dx, dy_down - slidingBallRadius, paint);

		// 上矩形游标并画出
		Rect rect_up = getRectByBallLocation(dy_up);
		canvas.drawBitmap(priceBar, null, rect_up, paint);
		// 下矩形游标并画出
		Rect rect_down = getRectByBallLocation(dy_down);
		canvas.drawBitmap(priceBar, null, rect_down, paint);
		// 将上文本画到矩形当中
		float text_upX = (dx * 3 / 4 - paint.measureText(String
				.valueOf(price_up))) / 2;
		float text_upY = rect_up.top - paint.ascent() + PADDING;
		canvas.drawText(String.valueOf(price_up), text_upX, text_upY, paint);
		// 将下文本画到游标矩形中
		float text_downX = (dx * 3 / 4 - paint.measureText(String
				.valueOf(price_down))) / 2;
		float text_downY = rect_down.top - paint.ascent() + PADDING;
		canvas.drawText(String.valueOf(price_down), text_downX, text_downY,
				paint);

		// 计算出绿色滑竿的截图矩形坐标
		Rect src = new Rect(0, (int) dy_up, greenSlidlingBar.getWidth(),
				(int) (dy_down));
		// 计算出绿色滑竿放置的矩形区域
		Rect dst = new Rect(x_bg, (int) dy_up, x_bg
				+ greenSlidlingBar.getWidth(), (int) dy_down);
		canvas.drawBitmap(greenSlidlingBar, src, dst, paint);

		// 清除画布的状态
		canvas.restore();
		super.onDraw(canvas);
	}

	/**
	 * 根据价格获得大饼圆心坐标
	 * 
	 * @param price
	 * @return
	 */
	public float getSlidingBallLocationByPrice(int price) {
		float dy = 0;
		if (price < FIRST_STAGE) {
			price = FIRST_STAGE;
		}
		if (price > FIFTH_STAGE) {
			price = FIFTH_STAGE;
		}
		// 根据价格来确定坐标
		if (price >= FIRST_STAGE && price < SECOND_STAGE) {
			dy = bg_height - span * price / SECOND_STAGE - FIRST_STAGE - radius;
		} else if (price >= SECOND_STAGE && price < THIRD_STAGE) {
			dy = bg_height - span * (price - SECOND_STAGE)
					/ (THIRD_STAGE - SECOND_STAGE) - span - radius;
		} else if (price >= THIRD_STAGE && price < FOURTH_STAGE) {
			dy = bg_height - span * (price - THIRD_STAGE)
					/ (FOURTH_STAGE - THIRD_STAGE) - 2 * span - radius;
		} else {
			dy = bg_height - span * (price - FOURTH_STAGE)
					/ (FIFTH_STAGE - FOURTH_STAGE) - 3 * span - radius;
		}
		// Log.i("INFO", "y坐标：" + dy);
		return dy;
	}

	public Rect getRectByBallLocation(float dy) {
		Rect rect = new Rect();
		rect.left = 0;
		rect.right = (int) dx;
		// 文本的中线与饼心中线一致
		float midText = paint.descent() - paint.ascent();
		rect.top = (int) (dy - midText / 2 - PADDING);
		rect.bottom = (int) (dy + midText / 2 + PADDING);
		return rect;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float point_X = 0;
		float point_Y = 0;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			point_X = event.getX() / mScale;
			point_Y = event.getY() / mScale;
			// 判断点击的坐标是否落在圆饼内
			if (point_X >= dx && point_X <= dx + slidingBall.getWidth()) {

				if (point_Y >= dy_down - slidingBall.getHeight() / 2
						&& point_Y <= dy_down + slidingBall.getHeight() / 2) {
					// 落在下饼内
					isDownSlidingBall = true;
					isUpSlidingBall = false;
				}
				if (point_Y >= dy_up - slidingBall.getHeight() / 2
						&& point_Y <= dy_up + slidingBall.getHeight() / 2) {
					// 落在上饼内
					isUpSlidingBall = true;
					isDownSlidingBall = false;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			point_Y = event.getY() / mScale;
			if (isUpSlidingBall) {
				// 根据坐标的变化获取相应的price
				price_up = getPriceBySlidingBallLocation(point_Y);
				// 上饼不能超过下饼
				if (price_up < price_down) {
					price_up = price_down;
				}

			}
			if (isDownSlidingBall) {
				// 根据坐标的变化获取相应的price
				price_down = getPriceBySlidingBallLocation(point_Y);
				// 下饼不能超过上饼
				if (price_down > price_up) {
					price_down = price_up;
				}
				Log.i("INFO", "dy_down：" + dy_down);
			}
			this.invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isDownSlidingBall = false;
			isUpSlidingBall = false;
			break;
		default:
			break;
		}
		return true;
	}

	public int getPriceBySlidingBallLocation(float dy) {
		int price = 0;
		if (price > FIFTH_STAGE) {
			price = FIFTH_STAGE;
		}
		if (price < FIRST_STAGE) {
			price = FIRST_STAGE;
		}
		if (dy >= radius && dy < radius + span) {
			price = (int) (FIFTH_STAGE - (dy - radius) / span
					* (FIFTH_STAGE - FOURTH_STAGE));
		} else if (dy >= radius + span && dy < radius + 2 * span) {
			price = (int) (FOURTH_STAGE - (dy - radius - span) / span
					* (FOURTH_STAGE - THIRD_STAGE));
		} else if (dy >= radius + 2 * span && dy < radius + 3 * span) {
			price = (int) (THIRD_STAGE - (dy - radius - 2 * span) / span
					* (THIRD_STAGE - SECOND_STAGE));
		} else if (dy >= radius + 3 * span && dy <= radius + 4 * span) {
			price = (int) ((bg_height - dy - radius) / span * (SECOND_STAGE - FIRST_STAGE));
		} else if (dy < radius) {
			price = FIFTH_STAGE;
		} else {
			price = FIRST_STAGE;
		}

		return priceTransform(price);
	}

	public int priceTransform(int price) {
		int mol;
		if (price < 1000) {
			mol = price % 20;
			price = mol > 20 / 2 ? price - mol + 20 : price - mol;
		}
		if (price > 1000) {
			mol = price % 1000;
			price = mol > 1000 / 2 ? price - mol + 1000 : price - mol;
		}
		return price;
	}
}
