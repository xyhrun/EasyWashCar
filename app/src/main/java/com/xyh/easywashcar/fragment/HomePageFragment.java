package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.Gridview_1;
import com.xyh.easywashcar.adapter.GridViewAdapter;
import com.xyh.easywashcar.adapter.ViewPagerAdapter;
import com.xyh.easywashcar.model.GridItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class HomePageFragment extends Fragment {
    private static final String TAG = "HomePageFragment";
    private Context context;
    private int i = 0;
    public HomePageFragment(Context context) {
        this.context = context;
        Log.d(TAG, "-------HomePageFragment: "+context);
    }

    @Bind(R.id.gridview_id)
    GridView gridView;
    @Bind(R.id.point_id)
    LinearLayout linearPonit;
    @Bind(R.id.view_pager_inner_id)
    ViewPager viewpager_inner;

    //设置网格布局信息
    private ArrayList<GridItem> gridItems = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    //设置ViewPager信息
    private ArrayList<ImageView> imageviews;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] imgs = {R.mipmap.wash_car01,R.mipmap.wash_car02,R.mipmap.wash_car03,R.mipmap.wash_car04};
    private int pointIndex;
    private boolean isPlaying = true;
    private ViewPagerListener viewPagerListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);
        initGridViewData();
        initViewPagerData();
        initAction();
        gridViewAdapter = new GridViewAdapter(gridItems, context);
        gridView.setAdapter(gridViewAdapter);
        setGridViewOnClickListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void setGridViewOnClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), Gridview_1.class));
            }
        });
    }

    private void initGridViewData() {
        if (gridItems.size() == 0) {
            Log.d(TAG, "!!-----initGridViewData: 执行了"+ i);
            gridItems.add(new GridItem("上门洗车", R.mipmap.car_1));
            gridItems.add(new GridItem("紧急救援", R.mipmap.car_repair));
            gridItems.add(new GridItem("汽车保养", R.mipmap.protection_1));
            gridItems.add(new GridItem("违章查询", R.mipmap.query));
            gridItems.add(new GridItem("司机交流", R.mipmap.driver));
            gridItems.add(new GridItem("车险推荐", R.mipmap.secuity));
        }

    }

    private void initViewPagerData() {
        imageviews = new ArrayList<>();
        View view;
        ViewGroup.LayoutParams params;
        for (int i = 0; i < imgs.length; i++) {
            //设置图片
            ImageView img = new ImageView(context);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            img.setBackgroundResource(imgs[i]);
            imageviews.add(img);

            //设置小圆点
            view = new View(context);
            params = new ViewGroup.LayoutParams(40, 40);
            view.setBackgroundResource(R.drawable.selector_drawable_point);
            view.setLayoutParams(params);
            view.setEnabled(false);
            //添加圆点
            linearPonit.addView(view);
        }
        viewPagerAdapter = new ViewPagerAdapter(imageviews, context);
        viewpager_inner.setAdapter(viewPagerAdapter);
    }

//    private void autoViewGager() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isPlaying) {
//                    SystemClock.sleep(2000);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//                        }
//                    });
//                }
//            }
//        }).start();
//    }

    private void initAction() {
        viewPagerListener = new ViewPagerListener();
        viewpager_inner.setOnPageChangeListener(viewPagerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % imageviews.size());
        //用来出发监听器
        viewpager_inner.setCurrentItem(index);
        linearPonit.getChildAt(pointIndex).setEnabled(true);
    }

    class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int currentPosition = position % imgs.length;
            linearPonit.getChildAt(currentPosition).setEnabled(true);
            linearPonit.getChildAt(pointIndex).setEnabled(false);
            pointIndex = currentPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
