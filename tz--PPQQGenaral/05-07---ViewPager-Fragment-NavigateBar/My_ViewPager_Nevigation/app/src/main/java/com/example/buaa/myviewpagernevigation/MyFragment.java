package com.example.buaa.myviewpagernevigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Alex on 2015/5/16.
 */
public class MyFragment extends Fragment{

    private int[] imageId = new int[]{  R.drawable.xishi,
                                        R.drawable.diaochan,
                                        R.drawable.wangzhaojun,
                                        R.drawable.yangguifei,
                                        R.drawable.linhuiyin,
                                        R.drawable.luxiaoman,
                                        R.drawable.zhouxuan,
                                        R.drawable.ruanlingyu
                                        };

    private int[] layoutId = new int[]{ R.layout.xishi,
                                        R.layout.diaochan,
                                        R.layout.wangzhaojun,
                                        R.layout.yangguifei,
                                        R.layout.linhuiyin,
                                        R.layout.luxiaoman,
                                        R.layout.zhouxuan,
                                        R.layout.ruanlingyu
                                        };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int position = bundle.getInt("position");

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(imageId[position]);

        //show  imageView
//        return imageView;
        //based on position ,show the xml
        return inflater.inflate(layoutId[position],null);
    }
}
