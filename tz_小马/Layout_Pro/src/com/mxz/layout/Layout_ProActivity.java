package com.mxz.layout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Layout_ProActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout bodylinear=new LinearLayout(this);
        LinearLayout.LayoutParams bodyparams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        bodylinear.setOrientation(LinearLayout.VERTICAL);
        bodylinear.setLayoutParams(bodyparams);
        
        
        LinearLayout.LayoutParams topparams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        LinearLayout toplinear=new LinearLayout(this);
        toplinear.setOrientation(LinearLayout.HORIZONTAL);
        toplinear.setLayoutParams(topparams);
        
        EditText et=new EditText(this);
        et.setHint("«Î ‰»Î...");
        et.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
        
        Button but=new Button(this);
        but.setText("∞¥≈•");
        but.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,0));
        
        toplinear.addView(et);
        toplinear.addView(but);
        
        
        LinearLayout.LayoutParams contentparams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        LinearLayout contentlinear=new LinearLayout(this);
        contentlinear.setOrientation(LinearLayout.VERTICAL);
        contentlinear.setBackgroundColor(Color.BLUE);
        contentlinear.setLayoutParams(contentparams);
        
        ImageView iv=new ImageView(this);
        LinearLayout.LayoutParams iv_params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        iv.setImageResource(R.drawable.ic_launcher);
        iv_params.gravity=Gravity.CENTER;
        iv.setLayoutParams(iv_params);
        
        contentlinear.addView(iv);
        
        bodylinear.addView(toplinear);
        bodylinear.addView(contentlinear);
        
        setContentView(bodylinear);
        
    }
}