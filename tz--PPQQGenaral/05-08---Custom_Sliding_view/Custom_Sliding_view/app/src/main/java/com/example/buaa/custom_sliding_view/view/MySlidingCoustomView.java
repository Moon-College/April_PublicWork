package com.example.buaa.custom_sliding_view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.buaa.custom_sliding_view.R;
import com.example.buaa.custom_sliding_view.utils.MyLog;

/**
 * Created by Alex on 2015/5/17.
 * QQ:XXXXXXXXX  Mobile:12345678910
 */
public class MySlidingCoustomView extends View{
    /**
     * The axis's background.
     */
    private Bitmap axis_bg;
    /**
     * Sliding Button on the axis.
     */
    private Bitmap sliding_btn;
    /**
     *Sliding Number Rectangle.
     */
    private Bitmap sliding_num_rect;
    /**
     * The axis's after background.
     */
    private Bitmap axis_green;
    /**
     * The height size of axis.
     */
    private int axis_bg_height;

    /**
     * Zoom Scale of height, also is the scale of width.It equals canvas's height / axis's height.
     */
    private float scale_h;
    private Paint paint;

    /**
     * Percent of text on the axis right side.
     */
    private final float REAL_PER = (float)19/20f;
    /**
     * X coordinate of the sliding_btn of the axis.
     */
    private float sliding_btn_x;

    /**
     * Height of each range.
     */
    private float span;
    /**
     * Height of half Circle.
     */
    private float semiCircle_height;

    public int getPrice_t() {
        return price_t;
    }

    public void setPrice_t(int price_t) {
        this.price_t = price_t;
    }

    /**
     * Price Top
     */
    private int price_t;

    public int getPrice_b() {
        return price_b;
    }

    public void setPrice_b(int price_b) {
        this.price_b = price_b;
    }

    /**
     * Price Bottom
     */
    private int price_b;
    /**
     * Y coordinate of slidingBtn_top.
     */
    private float btn_y_t;
    /**
     * Y coordinate of slidingBtn_bottom.
     */
    private float btn_y_b;
    /**
     * State of slidingButton Top and Bottom.
     */
    private boolean isTopTouched,isBottomTouched;
    /**
     * Padding of Sliding Number Rectangle and PriceText.
     */
    private static final int PRICE_PADDING = 20;

    //Each state of axis
    private static final int FIRST_STATE = 0;
    private static final int SECOND_STATE = 200;
    private static final int THIRD_STATE = 500;
    private static final int FOURTH_STATE = 1000;
    private static final int FIFTH_STATE = 10000;

    public MySlidingCoustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        axis_bg = BitmapFactory.decodeResource(getResources(), R.drawable.axis_before);
        sliding_btn = BitmapFactory.decodeResource(getResources(),R.drawable.btn);
        sliding_num_rect = BitmapFactory.decodeResource(getResources(),R.drawable.bg_number);
        axis_green = BitmapFactory.decodeResource(getResources(),R.drawable.axis_after);

        paint = new Paint();
        paint.setColor(Color.RED);

        //Parse XML style of custom.
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MySlidingView);
        price_t = typedArray.getInt(R.styleable.MySlidingView_price_top,500);
        price_b = typedArray.getInt(R.styleable.MySlidingView_price_bottom,200);

//        price_t = 500;
//        price_b = 200;

        MyLog.isDebug = true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取父容器设置的固定值，当设置为fill_parent或者固定宽高的时候
        //Get the specific height,when mode is MeasureSpec.EXACTLY.
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //Get mode of height
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //Get the specific width,when mode is MeasureSpec.EXACTLY.
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //Get mode of width
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        //Calculate the height,when mode is not MeasureSpec.EXACTLY.
        axis_bg_height = axis_bg.getHeight();

        int measuredHeight = (modeHeight == MeasureSpec.EXACTLY)?sizeHeight:axis_bg_height;
        int measuredWidth = 2*measuredHeight/3;

        scale_h = (float)measuredHeight/axis_bg_height;

        span = (REAL_PER * axis_bg_height) / 4;
        semiCircle_height = axis_bg_height * (1 - REAL_PER) / 2;

        //Set view's dimension
        setMeasuredDimension(measuredWidth, measuredHeight + 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Save this state.
        canvas.save();
        canvas.scale(scale_h, scale_h);

        //Set coordinate x of axis's background. this.getWidth() is width of the View.
        int axis_bg_x = (int)((this.getWidth()/scale_h - axis_bg.getWidth())/2);
        //Draw the axis.
        canvas.drawBitmap(axis_bg,axis_bg_x,0,paint);

        String[] numbers = new String[]{
            "Unlimited","1000","500","200","0"
        };

        //Set text size depend on the scale_h.
        paint.setTextSize(16 / scale_h);

        //Draw priceText besides axis's right.
        for(int i=0;i< numbers.length;i++){
            //Range of text on the axis right side.
            float span = REAL_PER*axis_bg_height / 4 ;
            //Y coordinate of each text
            float text_y = i*span + (1 - REAL_PER)*axis_bg_height/2 + paint.descent();
            //paint.descent is the baseLine of text
            MyLog.i("INFO", "text_y---" + text_y + "paint.descent---" + paint.descent());
            canvas.drawText(numbers[i],5*axis_bg_x/4,text_y,paint);
        }
        MyLog.i("INFO", "(float)(19/20)--" + (float) (19 / 20) + "+++(float)(1-1/20)--" + (float) (1 - 1 / 20));

        paint.setTextSize(20 / scale_h);
        canvas.drawText("Author:Alex", 0, axis_bg.getHeight()+paint.ascent(),paint);

        sliding_btn_x = (this.getWidth()/scale_h - sliding_btn.getWidth())/2;

        //Draw top sliding_btn
        btn_y_t = getSlidingBtnLocationByPrice(price_t);
        canvas.drawBitmap(sliding_btn, sliding_btn_x, btn_y_t - sliding_btn.getHeight() / 2, paint);
        //Draw bottom sliding_btn
        btn_y_b = getSlidingBtnLocationByPrice(price_b);
        canvas.drawBitmap(sliding_btn, sliding_btn_x, btn_y_b - sliding_btn.getHeight() / 2, paint);

        //Draw axis_green.
        Rect src = new Rect(0,(int)(btn_y_t + sliding_btn.getHeight()/2),axis_green.getWidth(),(int)(btn_y_b - sliding_btn.getHeight()/2));
        Rect dst = new Rect(axis_bg_x,(int)(btn_y_t + sliding_btn.getHeight()/2),axis_bg_x + axis_green.getWidth(),(int)(btn_y_b - sliding_btn.getHeight() / 2));
        canvas.drawBitmap(axis_green,src,dst,paint);

        //Draw top Sliding_num_rectangle
        Rect rect_t = getRectByMidLine(btn_y_t);
        canvas.drawBitmap(sliding_num_rect, null, rect_t, paint);
        //Draw bottom Sliding_num_rectangle
        Rect rect_b = getRectByMidLine(btn_y_b);
        canvas.drawBitmap(sliding_num_rect, null, rect_b, paint);

        //draw top priceText on the Sliding_num_rectangle
        float textPrice_t_x = (3*rect_t.width()/4 - paint.measureText(String.valueOf(price_t)))/2;
        float textPrice_t_y = rect_t.top - paint.ascent() + PRICE_PADDING;
        canvas.drawText(String.valueOf(price_t),textPrice_t_x,textPrice_t_y,paint);
        //draw bottom priceText on the Sliding_num_rectangle
        float textPrice_b_x = (3*rect_t.width()/4 - paint.measureText(String.valueOf(price_b)))/2;
        float textPrice_b_y = rect_b.top - paint.ascent() + PRICE_PADDING;
        canvas.drawText(String.valueOf(price_b), textPrice_b_x, textPrice_b_y, paint);

        //Reset canvas.
        canvas.restore();
        super.onDraw(canvas);
    }

    /**
     * Get Y coordinate of the SlidingButton by price.
     * @param price int price
     * @return float Y
     */
    private float getSlidingBtnLocationByPrice(int price) {
        float sliding_btn_y = 0;

        if(price < FIRST_STATE){
            price = FIRST_STATE;
        }
        if(price > FIFTH_STATE){
            price = FIFTH_STATE;
        }

        if(FIRST_STATE <= price && price < SECOND_STATE){
            //first range  From bottom to top.
            sliding_btn_y = axis_bg_height - (span * price)/(SECOND_STATE-FIRST_STATE) - semiCircle_height;
        }else if(SECOND_STATE <= price && price < THIRD_STATE){
            //second range
            sliding_btn_y = axis_bg_height - span * (price - SECOND_STATE)/(THIRD_STATE - SECOND_STATE) - span - semiCircle_height;
        }else if(THIRD_STATE <= price && price < FOURTH_STATE){
            //third range
            sliding_btn_y = axis_bg_height - span * (price - THIRD_STATE)/(FOURTH_STATE - THIRD_STATE) - 2*span - semiCircle_height;
        }else if(FOURTH_STATE <= price && price <= FIFTH_STATE){
            //fourth range
            sliding_btn_y = span * (FIFTH_STATE - price)/(FIFTH_STATE - FOURTH_STATE) + semiCircle_height;
        }

        MyLog.i("INFO","sliding_btn_y ---" + sliding_btn_y);

        return sliding_btn_y;
    }

    /**
     * Get a Rectangle by midLine's y coordinate.
     * @param y y coordinate of mindLine
     * @return rectangle
     */
    private Rect getRectByMidLine(float y){
        Rect rect = new Rect();
        rect.left = 0;
        rect.right = (int) sliding_btn_x;
        //Get text's height
        float text_h = paint.descent() - paint.ascent();
        rect.top = (int) (y - (text_h/2)) - PRICE_PADDING;
        rect.bottom = (int) (y + (text_h/2)) + PRICE_PADDING;
        return rect;
    }

    /**
     * Get price by Y roordinate.
     * @param y
     * @return
     */
    public int getPriceByPosition(float y){
        int price;
        float span = REAL_PER*this.getHeight()/4;
        float half_circle_height = this.getHeight()*(1 - REAL_PER)/2;
        if(y < half_circle_height){
            price = FIFTH_STATE;
        }else if(y >= half_circle_height && y < half_circle_height + span){
            //range from 10000 to 1000
            price = (int) (FIFTH_STATE - (FIFTH_STATE - FOURTH_STATE)*(y - half_circle_height)/span);
        }else if(y >= half_circle_height + span && y < half_circle_height + 2*span){
            //range from 1000 to 500
            price = (int) (FOURTH_STATE -(FOURTH_STATE - THIRD_STATE)*(y - half_circle_height - span)/span);
        }else if(y >= half_circle_height + 2*span && y < half_circle_height + 3*span){
            //range from 500 to 200
            price = (int) (THIRD_STATE - (THIRD_STATE - SECOND_STATE)*(y - half_circle_height - 2*span)/span);
        }else {
            //range from 200 to 0
            price = (int) ((SECOND_STATE - FIRST_STATE)*(this.getHeight() - y - half_circle_height)/span);
        }

        if(price < FIRST_STATE){
            price = FIRST_STATE;
        }

        //Format price
        if(price < 1000){
            int mol= price%20;
            if(mol >= 10){
                price = price - mol + 20;
            }else{
                price = price - mol;
            }
        }
        if(price >= 1000){
            int mol = price%1000;
            if(mol >= 500){
                price = price - mol + 1000;
            }else{
                price = price - mol;
            }
        }

        MyLog.i("INFO",":price+++++"+price);
        return price;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX() / scale_h;
                float y = event.getY() / scale_h;
                //Assert x and y on the SlidingBtn_Top.
                if(x >= sliding_btn_x && x <= sliding_btn_x + sliding_btn.getWidth()){
                    if(y >= btn_y_t -sliding_btn.getHeight()/2 && y <= btn_y_t + sliding_btn.getHeight() -sliding_btn.getHeight()/2){
                        isTopTouched = true;
                        isBottomTouched = false;
                    }
                    if(y >= btn_y_b -sliding_btn.getHeight()/2 && y <= btn_y_b + sliding_btn.getHeight() -sliding_btn.getHeight()/2){
                        isBottomTouched = true;
                        isTopTouched = false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float y2 = event.getY();
                if(isTopTouched){
                    price_t = getPriceByPosition(y2);
                    if(price_t < price_b){
                        price_t = price_b;
                    }
                }
                if(isBottomTouched){
                    price_b = getPriceByPosition(y2);
                    if(price_b > price_t){
                        price_b = price_t;
                    }
                }
                this.invalidate();
                MyLog.i("INFO","price_TOP--"+price_t+";price_BOTTOM---"+price_b);
                break;
            case MotionEvent.ACTION_UP:
                isTopTouched = false;
                isBottomTouched = false;
                break;
            default:
                break;
        }
        return true;
    }
}
