package com.tz.ontouchlab;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Choreographer.FrameCallback;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener {
	//Minimum image zoom scale
	private static final float MIN_ZOOM = 0.5f;
	//Maximum image zoom scale
	private static final float MAX_ZOOM = 2;
	
	private RelativeLayout mFrame;
	
	private ImageView iv;
	private Bitmap image;
	private Matrix matrix;
	//the X position when user first click on screen
	private float startX;
	//the Y position when user first click on screen
	private float startY;
	
	private GestureDetector gestureDetector;
	
	private long lastTouchTime = -1;
	
	private long lastBackClickTime = -1;
	
	private int originalImageWidth;
	private int originalImageHeight;
	
	private int ivWidth;
	private int ivHeight;
	
	//The distance between the two touch points before zoom start
	private float oldDis;
	//The distance between the two touch points after zoom finish
	private float newDis;
	
	private PointF midPoint;
	
	//The 9 values of the current matrix
	private float[] matrixValues = new float[9];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFrame = (RelativeLayout) findViewById(R.id.main_frame);

		gestureDetector = new GestureDetector(this, new ImageViewGestureListener());
		
		iv = (ImageView) findViewById(R.id.image_id);
		image = BitmapFactory.decodeResource(getResources(), R.drawable.pic11);
		//image.getHeight()
		iv.setImageBitmap(image);
		
		originalImageWidth = image.getWidth();
		originalImageHeight = image.getHeight();
		
		iv.setOnTouchListener(new OnImageViewTouchListener());
		
		//iv.seton 	
		/*iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			//OnDoubleTapListener
			
		});*/
		
		//Need to wait for layout pass before we can access width and height, otherwise always 0, can't even get the values in onResume
		//This will post a runnable to the event UI queue so that process will occur after layout pass 
		
		iv.post(new Runnable() {
			@Override
			public void run() {
				ivWidth = iv.getWidth();
				ivHeight = iv.getHeight();
				resetImage(false);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		long newTouchTime = System.currentTimeMillis();
		
		if (newTouchTime - lastBackClickTime < 300) {
			super.onBackPressed();
			finish();
		}
		else {
			lastBackClickTime = newTouchTime;
			Toast.makeText(this, "Pressing Back again will exit the app", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				Log.d("ontouch", "main action down");
				break;
			case MotionEvent.ACTION_MOVE :
				Log.d("ontouch", "main action move");
				break;
			case MotionEvent.ACTION_UP:
				Log.d("ontouch", "main action up");
				break;
			default:
				break;
		}
		
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				Log.d("ontouch", "action down");
				break;
			case MotionEvent.ACTION_MOVE :
				Log.d("ontouch", "action move");
				break;
			case MotionEvent.ACTION_UP:
				Log.d("ontouch", "action up");
				break;
			default:
				break;
		}
		
		return false;
	}
	
	/**
	 * Reset the image to its original size and centre the image
	 * @param resizeImage Whether to reset the image to its original size
	 */
	private void resetImage(boolean resizeImage) {
		if (resizeImage) {
			//resize
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, originalImageWidth, originalImageHeight, true);
			iv.setImageBitmap(resizedBitmap);
		}
		
		//centre image
		matrix = new Matrix();//have to create a new matrix, otherwise it cannot centre the ImageView
		matrix.postTranslate((ivWidth - originalImageWidth) / 2, (ivHeight - originalImageHeight) /2);
		iv.setImageMatrix(matrix);
	}
	
	/**
	 * Move image to a new position based on matrix
	 * @param newX
	 * @param newY
	 */
	private void moveImage(float newX, float newY) {
		matrix.postTranslate(newX - startX, newY - startY);//move 
		iv.setImageMatrix(matrix);//set a new matrix
		startX = newX;
		startY = newY;
	}
	
	/**
	 * Get distance between two points
	 * @param e
	 * @return
	 */
	private float getDistance(MotionEvent e) {
		float x = e.getX() - e.getX(1);
		float y = e.getY() - e.getY(1);

		
		return (float) Math.sqrt(x * x + y * y);
	}
	
	private PointF getMidPoint(MotionEvent e) {
		PointF p = new PointF();
		// 2 = (1 + 3) / 2
		p.x = (e.getX() + e.getX(1)) / 2;
		p.y = (e.getY() + e.getY(1)) / 2;
		
		return p;
	}
	
	private void zoom(MotionEvent e) {
		newDis = getDistance(e);
		float scale = newDis / oldDis;
		
		matrix.getValues(matrixValues);//populate the matrixValues
		
		float currentScale = matrixValues[Matrix.MSCALE_X];
		
		if (scale * currentScale > MAX_ZOOM) {
			scale = MAX_ZOOM / currentScale;
		}
		else if (scale * currentScale < MIN_ZOOM) {
			scale = MIN_ZOOM / currentScale;
		}
		
		matrix.postScale(scale, scale, midPoint.x, midPoint.y);
		iv.setImageMatrix(matrix);
	}
	
	private class OnImageViewTouchListener implements View.OnTouchListener {
		private int fingers;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			fingers = event.getPointerCount();
			
			if (fingers == 1) {//move image
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						//Log.d("ontouch", "f1 action down");
						//initialised the starting positions
						startX = event.getX();
						startY = event.getY();

						//double click handling
						long newTouchTime = System.currentTimeMillis();
						if (newTouchTime - lastTouchTime < 300) {//we have a double touch
							Log.d("ontouch", "double click");
							resetImage(true);
							lastTouchTime = -1;
						}
						else {
							lastTouchTime = newTouchTime;
						}
						break;
					case MotionEvent.ACTION_MOVE :
						//Log.d("ontouch", "f1 action move");
						moveImage(event.getX(), event.getY());
						break;
					case MotionEvent.ACTION_UP:
						//Log.d("ontouch", "f1 action up");
						break;
					default:
						break;
				}
				//use GestureDetector to intercept the event
				//gestureDetector.onTouchEvent(event);			
			}
			else if (fingers == 2) {//resize image
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDis = getDistance(event);
						midPoint = getMidPoint(event);
						break;
					case MotionEvent.ACTION_MOVE:
						zoom(event);
						break;
					default:
						break;
				}
			}
			
			//Log.d("ontouch", fingers + " fingers");
			
			//mFrame.invalidate();
			
			//gestureDetector.onTouchEvent(event);
			return true;
		}
		
	}
	
	private class ImageViewGestureListener extends GestureDetector.SimpleOnGestureListener {
		
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			startX = e1.getX();
			startY = e1.getY();
			
			Log.d("ontouch", "start x " + startX);
			Log.d("ontouch", "start y " + startY);
			
			float newX = e2.getX();
			float newY = e2.getY();
			Log.d("ontouch", "new x " + newX);
			Log.d("ontouch", "new y " + newY);
			matrix.postTranslate(newX - startX, newY - startY);
			iv.setImageMatrix(matrix);
			
			return true;//super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			//Log.d("ontouch", "gesture down");
			//startX = e.getX();
			//startY = e.getY();
			return false;//super.onDown(e);
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			//Log.d("ontouch", "gesture double tap");
			return super.onDoubleTap(e);
		}

	}
}
