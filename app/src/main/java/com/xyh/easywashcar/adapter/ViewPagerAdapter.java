package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xyh.easywashcar.base.MyAppcation;

import java.util.ArrayList;

/**
 * Created by 向阳湖 on 2016/5/19.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<ImageView> imageViews;
    private Context context;

    public ViewPagerAdapter(ArrayList<ImageView> imageViews, Context context) {
        this.imageViews = imageViews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position%imageViews.size()));
        View view = imageViews.get(position % imageViews.size());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAppcation.myToast("欢迎使用爱易洗车APP!");
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position % imageViews.size()));
    }
}
