package com.ckview.customview.view;

import java.util.Locale;

import com.ckview.customview.R;
import com.ckview.tools.log.BaseLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi")
public class SlidingBar extends View {
	
	private Bitmap backgroundPicture;
	private Bitmap buttonPicture;
	private Bitmap tagPicture;
	private Bitmap frontPicture;
	private int backgroundWidth;
	private int backgroundHeight;
	private float canvasScale;
	private int canvasWidth;
	private int canvasHeight;
	private int canvasRealPadding;
	private Point buttonPointU;
	private Point buttonPointD;
	private int[] prices = new int[]{
			10000,
			1000,
			500,
			200,
			0
	};
	private int[] stepSize = new int[]{
			1000,
			100,
			50,
			20
	};
	private int span;
	private int baseLineOffset;
	private int priceUp;
	private int priceDown;
	private boolean moveButtonUp = false;
	private boolean moveButtonDown = false;
	private boolean isPriceChanged = false;
	
	private Point pointTouchDown = new Point(0, 0);
	
	private final float BALL_SCALE = 0.043f;
	
	// Background picture scale
	private final float VIEW_SCALE = 2f/3;
	
	// Text-all width ratio
	private final float TAG_SCALE = 0.77f;
	
	// padding of canvas top&bottom
	private final int CANVAS_PADDING = 40;
	
	private Paint paint;
	
	public SlidingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		backgroundPicture = BitmapFactory.decodeResource(getResources(), R.drawable.axis_before);
		buttonPicture = BitmapFactory.decodeResource(getResources(), R.drawable.btn);
		tagPicture = BitmapFactory.decodeResource(getResources(), R.drawable.bg_number);
		frontPicture = BitmapFactory.decodeResource(getResources(), R.drawable.axis_after);
		backgroundWidth = backgroundPicture.getWidth();
		backgroundHeight = backgroundPicture.getHeight();
		span = (int) (backgroundHeight*(1 - BALL_SCALE)/4);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		int measureHeightState = MeasureSpec.getMode(heightMeasureSpec);
		// if height is exactly set measureHeight to height of view ,else set height of picture to height of view
		int viewHeight = (measureHeightState == MeasureSpec.EXACTLY)?measureHeight:backgroundHeight;
		// width is VIEW_SCALE times height
		int viewWidth = (int) (viewHeight*VIEW_SCALE);
		// when this view pressed, its' space is not equal to picture's space,canvasScale is the scale of view/picture
		canvasScale = (float)viewHeight/(backgroundHeight);
		//super.onMeasure(viewWidth, viewHeight);
		BaseLog.debug("debug", "viewWidth:"+viewWidth);
		BaseLog.debug("debug", "viewHeight:"+viewHeight);
		super.setMeasuredDimension(viewWidth, viewHeight+2*CANVAS_PADDING);
		priceUp = 350;
		priceDown = 0;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.scale(canvasScale, canvasScale);
		canvasWidth = (int) (getWidth()/canvasScale);
		canvasHeight = (int) (getHeight()/canvasScale);
		canvasRealPadding = (int) (CANVAS_PADDING/canvasScale);
		
		//find point of picture left&top
		int picX = (canvasWidth - backgroundWidth)/2;
		BaseLog.debug("debug", "canvasWidth---"+canvasWidth);
		BaseLog.debug("debug", "backgroundWidth---"+backgroundWidth);
		BaseLog.debug("debug", "picX---"+picX);
		int picY = (int) (canvasRealPadding);
		BaseLog.debug("debug", "picY---"+picY);
		//show the middle picture
		canvas.drawBitmap(backgroundPicture, picX, picY, null);
		
		
		//show prices on background right
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(24/canvasScale);
		baseLineOffset = (int) ((paint.descent() - paint.ascent())/2 - paint.descent());
		for(int i = 0; i < prices.length; i++){
			int textX = picX + backgroundWidth + 40;
			int textY = (int) ((int) (canvasRealPadding) + backgroundHeight*BALL_SCALE/2 
					+ span*i + baseLineOffset);
			BaseLog.debug("debug", "canvasRealPadding---"+(int) (canvasRealPadding));
			BaseLog.debug("debug", "backgroundHeight*BALL_SCALE/2---"+backgroundHeight*BALL_SCALE/2);
			BaseLog.debug("debug", "span*i---"+span*i);
			BaseLog.debug("debug", "textY---"+textY);
			canvas.drawText(String.valueOf(prices[i]), textX, textY, paint);
//			canvas.drawLine(0, textY, canvasWidth, textY, paint);
		}
		
		//show two button with price
		buttonPointU = getButtonPointByPrice(priceUp);
		buttonPointD = getButtonPointByPrice(priceDown);
		// draw picture between buttonUp with buttonDown
		Rect src = new Rect(0, 
				buttonPointU.y+buttonPicture.getHeight() - canvasRealPadding, 
				backgroundWidth, 
				buttonPointD.y - canvasRealPadding);;
		Rect dst = new Rect(picX, 
				buttonPointU.y+buttonPicture.getHeight(), 
				picX+backgroundWidth, 
				buttonPointD.y);
		canvas.drawBitmap(frontPicture, src, dst, paint);
		// draw button
		canvas.drawBitmap(buttonPicture, buttonPointU.x, buttonPointU.y, paint);
		canvas.drawBitmap(buttonPicture, buttonPointD.x, buttonPointD.y, paint);
	
		// draw price tag
		Point tagPointU = new Point();
		Point tagPointD = new Point();
		tagPointU = getTagPoint(buttonPointU);
		tagPointD = getTagPoint(buttonPointD);
		canvas.drawBitmap(tagPicture, tagPointU.x, tagPointU.y, paint);
		canvas.drawBitmap(tagPicture, tagPointD.x, tagPointD.y, paint);
		
		// draw price text
		Point textPointU = new Point();
		Point textPointD = new Point();
		textPointU = getTextPoint(tagPointU.x, buttonPointU.y, priceUp);
		textPointD = getTextPoint(tagPointD.x, buttonPointD.y, priceDown);
		canvas.drawText(String.valueOf(priceUp), textPointU.x, textPointU.y, paint);
		canvas.drawText(String.valueOf(priceDown), textPointD.x, textPointD.y, paint);
		
		canvas.restore();
		super.onDraw(canvas);
	}

	/**
	 * get text base line point
	 * @param tagX
	 * @param buttonY
	 * @param price
	 * @return point
	 */
	private Point getTextPoint(int tagX, int buttonY, int price) {
		Point point = new Point();
		point.x = (int) (tagX + (tagPicture.getWidth()*TAG_SCALE - paint.measureText(String.valueOf(price)))/2);
		point.y = buttonY + buttonPicture.getHeight()/2 + baseLineOffset;
		return point;
	}

	/**
	 * get tag point by button point
	 * @param buttonPoint
	 * @return Point
	 */
	private Point getTagPoint(Point buttonPoint) {
		Point point = new Point();
		point.x = buttonPoint.x - tagPicture.getWidth();
		point.y = buttonPoint.y + buttonPicture.getHeight()/2 - tagPicture.getHeight()/2;
		return point;
	}

	/**
	 * get point by price, if price is illegal y will return 0
	 * @param price
	 * @return point
	 */
	private Point getButtonPointByPrice(int price) {
		Point point = new Point();
		point.x = (canvasWidth - buttonPicture.getWidth())/2;
		price = (price > 10000)?10000:price;
		price = (price < 0)?0:price;
		//Ignore the first price
		for(int i = 1; i < prices.length; i++){
			if(price >= prices[i]){
				// span*(i-1) is all above price block distance
				// canvasRealPadding is padding-top&padding-bottom in canvas
				// (prices[i-1]-price)/(prices[i-1]-prices[i])*span is distance of current block
				// backgroundHeight*BALL_SCALE/2 is radius of ball in top background
				point.y = (int) (span*(i-1) + canvasRealPadding + (float)(prices[i-1]-price)/(prices[i-1]-prices[i])*span 
						+ backgroundHeight*BALL_SCALE/2 - buttonPicture.getHeight()/2);
				return point;
			}
		}
		point.y = 0;
		return point;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// new a point by current (x,y)
		Point point = new Point((int)(event.getX()/canvasScale), (int)(event.getY()/canvasScale));
		// distance scale of pointer has moved
		float moveDistanceScale = Math.abs((float)(pointTouchDown.y-point.y)/span);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			pointTouchDown.set((int)(event.getX()/canvasScale), (int)(event.getY()/canvasScale));
			// if touch in button area, start action to move button
			Point buttonUpPoint = getButtonPointByPrice(priceUp);
			Point buttonDownPoint = getButtonPointByPrice(priceDown);
			int buttonWidth = buttonPicture.getWidth();
			int buttonHeight = buttonPicture.getHeight();
			if(event.getX()/canvasScale >= buttonUpPoint.x && event.getX()/canvasScale <= buttonUpPoint.x + buttonWidth
					&& event.getY()/canvasScale >= buttonUpPoint.y && event.getY()/canvasScale <= buttonUpPoint.y + buttonHeight){
				moveButtonUp = true;
				BaseLog.debug("touch", "moveButtonUp---"+moveButtonUp);
			}
			if(event.getX()/canvasScale >= buttonDownPoint.x && event.getX()/canvasScale <= buttonDownPoint.x + buttonWidth
					&& event.getY()/canvasScale >= buttonDownPoint.y && event.getY()/canvasScale <= buttonDownPoint.y + buttonHeight){
				moveButtonDown = true;
				BaseLog.debug("touch", "moveButtonDown---"+moveButtonDown);
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			point.set((int)(event.getX()/canvasScale), (int)(event.getY()/canvasScale));
//			pointTouchDown.set((int)(event.getX()/canvasScale), (int)(event.getY()/canvasScale));
			if(moveButtonUp){
				int changedPrice = getPriceByPoint(point);
				if(priceUp != changedPrice){
					priceUp = changedPrice;
					isPriceChanged = true;
				}
			}
			if(moveButtonDown){
				int changedPrice = getPriceByPoint(point);
				if(priceDown != changedPrice){
					priceDown = changedPrice;
					isPriceChanged = true;
				}
			}
			if(isPriceChanged){
				this.invalidate();
			}
			break;
			
		case MotionEvent.ACTION_UP:
			moveButtonUp = false;
			moveButtonDown = false;
			isPriceChanged = false;
		default:
			break;
		}
		return true;
	}

	/**
	 * get price by center of button point
	 * @param point point of button left|top
	 * @return price
	 */
	private int getPriceByPoint(Point point) {
		point.y = (int) (point.y - backgroundHeight*BALL_SCALE/2 - canvasRealPadding) >= (prices.length-1)*span?(int)((prices.length-1)*span + backgroundHeight*BALL_SCALE/2 + canvasRealPadding):point.y;
		point.y = (int) (point.y - backgroundHeight*BALL_SCALE/2 - canvasRealPadding) <= 0?(int)(0 + backgroundHeight*BALL_SCALE/2 + canvasRealPadding):point.y;
		int i = (int) (point.y - backgroundHeight*BALL_SCALE/2 - canvasRealPadding)/span + 1;
		int price = prices[i-1] -  (int) (((float)(point.y - (int)(backgroundHeight*BALL_SCALE/2 + canvasRealPadding))/span + 1 - i)
				*(prices[i-1] - prices[i]));
		for(int j = 1; j < prices.length; j++){
			if(price > prices[j]){
				if(price%stepSize[j-1] > stepSize[j-1]/2){
					price = price - price%stepSize[j-1] + stepSize[j-1];
				}else{
					price = price - price%stepSize[j-1];
				}
				break;
			}
		}
		// decide witch button is moving
		if(moveButtonUp){
			// if there are mixed with border of two buttons,function won't change price,return origin value of priceUp
			if(getButtonPointByPrice(price).y + buttonPicture.getHeight() > buttonPointD.y){
				return priceUp;
			}
		}else{
			if(buttonPointU.y + buttonPicture.getHeight() > getButtonPointByPrice(price).y){
				return priceDown;
			}
		}
		BaseLog.debug("debugprice", "price---"+price);
		return price;
	}
}
