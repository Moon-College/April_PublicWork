package com.example.myview.view;


import com.example.myview.R;
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
/**
 * 自定义控件的步骤   函数运行顺序1.构造函数  2.onMeasure 3.onDraw
 * 1.重写有两个参数的构造函数
 * 2.重写 onMeasure函数
 * 3.onDraw
 * @author Administrator
 *
 */
public class SlidingButton extends View {
	private Bitmap gray_bg;//灰色滑竿
	private Bitmap green_bg;//灰色滑竿
	private Paint paint;    //画笔
	private Bitmap btn;   //杆上面的圈圈
	private Bitmap text_bg;   //文本的背景图片
	private float scale_h;    //画布的缩放比率
	private final float REAL_PER = 0.95f;  //每个小球高度占1/20
	private int gray_bg_height;    //竖杆的高度
	private final int PRICE_PADDING = 15;
	private int max_price;   //价格的上限
	private int min_price;   //价格的下限
	private float span;   //两个球中心的距离
	private float half_boll;   //半球的高度
	private float btn_x;    //圆圈的x坐标
	private float circle_up_y;
	private float circle_down_y;
	
	private boolean isMaxPriced,isMinPriced;    //两个boolean的变量来确定是触摸了那一个圆圈
	
	private final int FIRST_STAGE = 0;
	private final int SECOND_STAGE = 200;
	private final int THIRD_STAGE = 500;
	private final int FOUTH_STAGE = 1000;
	private final int FIFTH_STAGE = 10000;

	
	public SlidingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.myviewattrs);
		max_price=array.getInt(R.styleable.myviewattrs_max_price, 1000);
		min_price=array.getInt(R.styleable.myviewattrs_min_price, 200);
	}
	
	private void initView() {
		gray_bg=BitmapFactory.decodeResource(getResources(), R.drawable.axis_before);
		green_bg=BitmapFactory.decodeResource(getResources(), R.drawable.axis_after);
		btn=BitmapFactory.decodeResource(getResources(), R.drawable.btn);
		text_bg=BitmapFactory.decodeResource(getResources(), R.drawable.bg_number);
		paint = new Paint();
		paint.setColor(Color.BLACK);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//默认的都为fill_parent
		int sizeWidth=MeasureSpec.getSize(widthMeasureSpec);     //得到控件的实际宽
		int sizeHeight=MeasureSpec.getSize(heightMeasureSpec);	//得到控件的实际高
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        
        //wrap_content  则为图片的高度
        gray_bg_height = gray_bg.getHeight();
        
        
        //MeasureSpec.EXACTLY  代表fill_parent 和具体的xx dip值
        // MeasureSpec.AT_MOST   代表wrap_content
        int measuredHeight=heightMode==MeasureSpec.EXACTLY?sizeHeight:gray_bg_height;  //我想设置的高度
    
        scale_h=(float)measuredHeight/gray_bg_height;
        span=REAL_PER*gray_bg_height/4;
        half_boll=gray_bg_height*(1-REAL_PER)/2;
        int measuredWidth=2*measuredHeight/3;   //设置的宽度
        setMeasuredDimension(measuredWidth, measuredHeight);
	//	super.onMeasure(widthMeasureSpec, heightMeasureSpec);   //调用父类默认的测量方法
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.save() 和canvas.restore()总是成对
		canvas.save();
		canvas.scale(scale_h, scale_h);
		
		//背景图片 竖杆的x坐标
		int x=(int)((this.getWidth()/scale_h-gray_bg.getWidth())/2);
		
		//画滑竿右边的刻度文本
		String [] numbers = new String[]{
				String.valueOf(FIFTH_STAGE),
				String.valueOf(FOUTH_STAGE),
				String.valueOf(THIRD_STAGE),
				String.valueOf(SECOND_STAGE),
				String.valueOf(FIRST_STAGE)
		};
		
		canvas.drawBitmap(gray_bg, x, 0, null);
		
		//调画笔文本字体大小
		paint.setTextSize(20/scale_h);
		
		for(int i=0;i<numbers.length;i++){
			float scan_y=REAL_PER*gray_bg_height/4;
			//计算文本的y坐标
			
			//paint.descent()画笔基线以下的距离  类似于四线三格
			//paint.ascent()  画笔基线以上的距离
			float text_y=scan_y*i+(1-REAL_PER)*gray_bg_height/2+paint.descent();
			canvas.drawText(numbers[i], 5*x/4, text_y, paint);
		}
		
		btn_x = (this.getWidth()/scale_h-btn.getWidth())/2;
		
		circle_up_y = getYbyPrice(max_price);
		circle_down_y = getYbyPrice(min_price);
		//画圆圈
		canvas.drawBitmap(btn, btn_x, circle_up_y-btn.getHeight()/2, null);
		canvas.drawBitmap(btn, btn_x, circle_down_y-btn.getHeight()/2, null);
		
		//src是相对于des而言  所以left=0
		//画max_price和min_price之间的绿色条
		//这种画图的方法，相当于图片的裁剪
		Rect src=new Rect(0, (int)circle_up_y, green_bg.getWidth(), (int)circle_down_y);
		Rect des=new Rect(x, (int)circle_up_y, x+green_bg.getWidth(), (int)circle_down_y);
		canvas.drawBitmap(green_bg, src, des, null);
		
		
		//画左边的价格矩形
		Rect rect_up = getRectByMidLine(circle_up_y);
		canvas.drawBitmap(text_bg, null, rect_up, paint);
		Rect rect_down = getRectByMidLine(circle_down_y);
		canvas.drawBitmap(text_bg, null, rect_down, paint);
		
		//画左边价格上文本
		//上文本的xy坐标
		float text_u_x = (3*rect_up.width()/4 - paint.measureText(String.valueOf(max_price)))/2;
		float text_u_y = rect_up.top - paint.ascent()+PRICE_PADDING;
		//下文本的xy坐标
		float text_d_x = (3*rect_down.width()/4 - paint.measureText(String.valueOf(min_price)))/2;
		float text_d_y = rect_down.top - paint.ascent()+PRICE_PADDING;
		
		//画左边文本
		canvas.drawText(String.valueOf(max_price), text_u_x, text_u_y, paint);
		canvas.drawText(String.valueOf(min_price), text_d_x, text_d_y, paint);
		canvas.restore();
		super.onDraw(canvas);
	}
	/**
	 * 根据价格的变换 来得到画圆圈的y坐标
	 * @param price
	 * @return
	 */
	public float getYbyPrice(int price){
		float y=0;
		if(price<FIRST_STAGE){
			y=0;
		}
		if(price>FIFTH_STAGE){
			y=FIFTH_STAGE;
		}
		if(price>=FIRST_STAGE && price < SECOND_STAGE){
			y=gray_bg_height-half_boll-span*price/(SECOND_STAGE-FIRST_STAGE);
		}else if(price >= SECOND_STAGE && price <THIRD_STAGE){
			y=gray_bg_height-span-half_boll-span*(price-SECOND_STAGE)/(THIRD_STAGE-SECOND_STAGE);
		}else if(price >= THIRD_STAGE && price < FOUTH_STAGE){
			y=gray_bg_height-2*span-half_boll-span*(price-THIRD_STAGE)/(FOUTH_STAGE-THIRD_STAGE);
		}else{
			y=gray_bg_height-3*span-half_boll-span*(price-FOUTH_STAGE)/(FIFTH_STAGE-FOUTH_STAGE);
		}
		return y;
	}
	
	/**
	 * 根据大圈的中线确定坐标价格游标的矩形
	 * @param y
	 * @return
	 */
	public Rect getRectByMidLine(float y){
		Rect rect = new Rect();
		rect.left = 0;//左
		rect.right = (int) btn_x;
		//计算文本的高
		float text_h = paint.descent()-paint.ascent();
		rect.top = (int) (y - text_h/2)-PRICE_PADDING;
		rect.bottom = (int) (y+ text_h/2)+PRICE_PADDING;
		return rect;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float x=event.getX()/scale_h;  //event.getX()得到的是控件中的x坐标，要判断x的左边范围必须将其缩放
			float y=event.getY()/scale_h;
			
			if(x >= btn_x && x <= btn_x+btn.getWidth()){
				//触摸了上面的大圆圈
				if(y >= circle_up_y-btn.getHeight()/2 && y <= circle_up_y+btn.getHeight()/2){
					isMaxPriced=true;
					isMinPriced=false;
				}
				//触摸了下面的大圆圈
				if(y >= circle_down_y-btn.getHeight()/2 && y <= circle_down_y+btn.getHeight()/2){
					isMinPriced=true;
					isMaxPriced=false;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float y2 = event.getY();
			if(isMaxPriced){
				max_price = getPriceByPosition(y2);
				if(max_price<min_price){
					max_price = min_price;
				}
			}
			if(isMinPriced){
				min_price = getPriceByPosition(y2);
				if(min_price>max_price){
					min_price = max_price;
				}
			}
			this.invalidate();//重绘
			break;
		case MotionEvent.ACTION_UP:
			isMinPriced=false;
			isMaxPriced=false;
			break;
		default:
			break;
		}
		return true;
	}
	/**
	 * 根据y的坐标得到对应的价格,此处得注意坐标的选取
	 * @param y
	 * @return
	 */
	private int getPriceByPosition(float y) {
		//因为拿到的y坐标是控件的y坐标，所有下面的span和half_height是用this.getHeigth()
		float span = REAL_PER*this.getHeight()/4;
		float half_height = this.getHeight()*(1-REAL_PER)/2;
//		float span = REAL_PER*gray_bg_height/4;
//		float half_height = gray_bg_height*(1-REAL_PER)/2;
		int price=0;
		if(y<half_height){
			price=FIFTH_STAGE;
		}else if(y >= half_height && y < half_height+span){
			//1000-10000
			price=(int) (FIFTH_STAGE-(FIFTH_STAGE-FOUTH_STAGE)*(y-half_height)/span);
		}else if(y >= half_height+span && y< half_height+2*span){
			//500-1000
			price=(int) (FOUTH_STAGE-(FOUTH_STAGE-THIRD_STAGE)*(y-half_height-span)/span);
		}else if(y >= half_height+2*span && y< half_height+3*span){
			//200-500
			price=(int) (THIRD_STAGE-(THIRD_STAGE-SECOND_STAGE)*(y-half_height-2*span)/span);
		}else if(y >= half_height+3*span && y< half_height+4*span){
			//0-200
			price=(int) (SECOND_STAGE-(SECOND_STAGE-FIRST_STAGE)*(y-half_height-3*span)/span);
		}
		if(price < 0){
			price=0;
		}
		
		if(price >1000){ //1000-10000以下，每滑动一次改变1000
			int mod=price % 1000;
			if(mod >= 500){
				price=price-mod+1000;
			}else{
				price=price-mod;
			}
		}
		if(price <1000){  //1000以下，每滑动一次改变20
			int mod=price % 20;
			if(mod >= 10){
				price=price-mod+20;
			}else{
				price=price-mod;
			}
		}
		return price;
	}
}
