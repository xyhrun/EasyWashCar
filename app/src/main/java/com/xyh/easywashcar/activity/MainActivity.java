package com.xyh.easywashcar.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.fragment.HomePageFragment;
import com.xyh.easywashcar.fragment.MarketFragment;
import com.xyh.easywashcar.fragment.MessageFragment;
import com.xyh.easywashcar.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.view_pagerlayout_id)
    ViewPager viewPager;
    @Bind(R.id.radiogroup_id)
    RadioGroup radioGroup;
    @Bind(R.id.market_id)
    RadioButton market;
    @Bind(R.id.me_id)
    RadioButton my_order;
    @Bind(R.id.home_id)
    RadioButton home;
    @Bind(R.id.contact_id)
    RadioButton contact;

    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPagerData();
        viewPager.setOnPageChangeListener(new PageChangeListener());
        radioGroup.setOnCheckedChangeListener(new CheckedChangeListener());

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_main_out);
    }


    class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.home_id:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.market_id:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.me_id:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.contact_id:
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    radioGroup.check(R.id.home_id);
                    break;
                case 1:
                    radioGroup.check(R.id.market_id);
                    break;
                case 2:
                    radioGroup.check(R.id.me_id);
                    break;
                case 3:
                    radioGroup.check(R.id.contact_id);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void initViewPagerData() {
        Log.d(TAG, "--------initViewPagerData: 你他妈执行了吗");
        HomePageFragment homePageFragment = new HomePageFragment(MainActivity.this);
        MarketFragment marketFragment = new MarketFragment();
        MineFragment mineFragment = new MineFragment();
        MessageFragment messageFragment = new MessageFragment();
        fragments.add(homePageFragment);
        fragments.add(marketFragment);
        fragments.add(mineFragment);
        fragments.add(messageFragment);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        //有值
        Log.d(TAG, "------initViewPagerData: " + myPagerAdapter);
        viewPager.setAdapter(myPagerAdapter);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
