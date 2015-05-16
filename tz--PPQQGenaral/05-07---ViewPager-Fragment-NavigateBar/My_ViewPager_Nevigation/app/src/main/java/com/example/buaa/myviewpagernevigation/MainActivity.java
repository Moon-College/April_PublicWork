package com.example.buaa.myviewpagernevigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Study Advanced View ==== Viewpager and  Fragment
 * @author Alex on 2015/5/16.
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private HorizontalScrollView hsv;
    private RadioGroup rg;
    private RadioButton xishi, diaochan, yangguifei,
            wangzhaojun, luxiaoman, linhuiyin, zhouxuan, ruanlingyu;
    private View view_line;
    private ViewPager vp;
    /**
     * translateAnimation from x
     */
    private int fromX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * initial View
     */
    private void initView() {
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        rg = (RadioGroup) findViewById(R.id.rg);
        //radioGroup set changerListener to change viewPager's currentItem
        rg.setOnCheckedChangeListener(this);

        xishi = (RadioButton) findViewById(R.id.xishi);
        diaochan = (RadioButton) findViewById(R.id.diaochan);
        wangzhaojun = (RadioButton) findViewById(R.id.wangzhaojun);
        yangguifei = (RadioButton) findViewById(R.id.yangguifei);
        linhuiyin = (RadioButton) findViewById(R.id.linhuiyin);
        luxiaoman = (RadioButton) findViewById(R.id.luxiaoman);
        zhouxuan = (RadioButton) findViewById(R.id.zhouxuan);
        ruanlingyu = (RadioButton) findViewById(R.id.ruanlingyu);

        view_line = findViewById(R.id.view_line);
        vp = (ViewPager) findViewById(R.id.vp);
        MyAdapter adapter = new MyAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adapter);
        //set viewPager changeListener to change radiobutton on the hsv
        vp.setOnPageChangeListener(this);

        xishi.setTextColor(Color.GREEN);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int checkedItem =  0;
        switch (checkedId){
            case R.id.xishi:
                checkedItem = 0;
                break;
            case R.id.diaochan:
                checkedItem = 1;
                break;
            case R.id.wangzhaojun:
                checkedItem = 2;
                break;
            case R.id.yangguifei:
                checkedItem = 3;
                break;
            case R.id.linhuiyin:
                checkedItem = 4;
                break;
            case R.id.luxiaoman:
                checkedItem = 5;
                break;
            case R.id.zhouxuan:
                checkedItem = 6;
                break;
            case R.id.ruanlingyu:
                checkedItem = 7;
                break;
            default:
                break;
        }
        //viewPager  set currentItem
        vp.setCurrentItem(checkedItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //get total distance to scrolled   positionOffset need to be plus
        int total = (int) ((position + positionOffset)* rg.getChildAt(position).getWidth());
        //get range of greenGrid,when the chosen radiobutton on screnn center
        int range = (vp.getWidth() - rg.getChildAt(position).getWidth()) /2;
        //get dx to scrolled
        int dx = total - range;
        hsv.scrollTo(dx,0);
        Log.i("INFO", "onPageScrolled");
        lineScroll(position,positionOffset);
    }

    /**
     * set red Line scroll
     */
    private void lineScroll(int position, float positionOffset) {
        RadioButton button = (RadioButton) rg.getChildAt(position);

        //get coordinate of current radioButton
        int[] location = new int[2];
        button.getLocationInWindow(location);

        TranslateAnimation animation = new TranslateAnimation(
                fromX,
                location[0] + positionOffset*rg.getChildAt(position).getWidth(),
                0,
                0
        );
        animation.setDuration(50);
        //when animation is done, view_line stay the position
        animation.setFillAfter(true);
        //set view_line translate animation
        view_line.startAnimation(animation);
        //reset fromX
        fromX = (int) (location[0] + positionOffset*rg.getChildAt(position).getWidth());
    }

    //set textColor is green of the chosen radioButton
    @Override
    public void onPageSelected(int position) {
        for(int i = 0; i<rg.getChildCount(); i++){
            RadioButton radioButton = (RadioButton) rg.getChildAt(i);
            if(i == position){
                radioButton.setTextColor(Color.GREEN);
            }else{
                radioButton.setTextColor(Color.BLACK);
            }
        }
        Log.i("INFO", "onPageSelected----------" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Log.i("INFO","onPageScrollStateChanged"+ state);

    }

    private class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * each Fragment on the ViewPager by position
         * @param position current Position of viewPAger
         * @return fragment
         */
        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            myFragment.setArguments(bundle);
            return myFragment;
        }

        /**
         * children count on the ViewPager
         * @return count
         */
        @Override
        public int getCount() {
            return rg.getChildCount();
        }
    }


}
