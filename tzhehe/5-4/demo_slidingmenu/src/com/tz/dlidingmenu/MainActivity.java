package com.tz.dlidingmenu;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements  OnClickListener {
    /** Called when the activity is first created. */
	private SlidingMenu mMenu;
	private LinearLayout qq;
	private Button btn;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		qq=(LinearLayout) findViewById(R.id.qq);
		btn=(Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
	}
    
   
	@Override
	public void onClick(View arg0) {
		mMenu.toggle();
	}
	
	
	
}