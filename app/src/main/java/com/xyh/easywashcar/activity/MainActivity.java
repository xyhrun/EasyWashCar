package com.xyh.easywashcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.fragment.HomePageFragment;
import com.xyh.easywashcar.fragment.InformationFragment;
import com.xyh.easywashcar.fragment.MarketFragment;
import com.xyh.easywashcar.fragment.MineFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//记得继承FragmentActivity
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    @Bind(R.id.view_pagerlayout_id)
    ViewPager viewPager;
    @Bind(R.id.radiogroup_id)
    RadioGroup radioGroup;
    @Bind(R.id.market_id)
    RadioButton market;
    @Bind(R.id.resource_id)
    RadioButton my_order;
    @Bind(R.id.home_id)
    RadioButton home;
    @Bind(R.id.mine_id)
    RadioButton contact;
    @Bind(R.id.left_login_id)
    Button login;
    //绑定左拉菜单
    @Bind(R.id.left_menu_listView_id)
    ListView left_menu_listView;

    private long exitTime;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    //左拉菜单listview适配器
    private SimpleAdapter leftMenuAdapter;
    private int[] imgs;
    private String[] imgNames;
    private ArrayList<HashMap<String, Object>> leftMenuDatas = new ArrayList<>();
    private String[] from;
    private int[] to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPagerData();
        initLeftMenuData();
        //滑动界面时候,按钮也被选中
        viewPager.setOnPageChangeListener(new PageChangeListener());
        viewPager.setOffscreenPageLimit(3);
        //按钮选中时候,界面也被滑动
        radioGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        leftMenuAdapter = new SimpleAdapter(this, leftMenuDatas, R.layout.item_listview, from, to);
        left_menu_listView.setAdapter(leftMenuAdapter);
        setOnClickListener();
    }

    private void setOnClickListener() {
        login.setOnClickListener(this);
    }


    //初始化左拉菜单数据
    private void initLeftMenuData() {
        imgs = new int[]{R.mipmap.activity, R.mipmap.collect};
        imgNames = new String[]{"活动", "收藏"};
        from = new String[]{"img", "imgName"};
        to = new int[]{R.id.left_menu_img_id, R.id.left_menu_imgName_id};
        for(int i = 0 ; i < imgs.length; i++) {
            HashMap<String, Object> hashmap = new HashMap<>();
            hashmap.put("img",imgs[i]);
            hashmap.put("imgName", imgNames[i]);
            leftMenuDatas.add(hashmap);
        }
    }

    //设置结束页面动画
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_main_out);
    }

    //两秒内按两次及以上返回就退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondExitTime = System.currentTimeMillis();
            if ((secondExitTime - exitTime) > 2000) {
                Snackbar sb = Snackbar.make(radioGroup, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                sb.show();
//                MyAppcation.myToast("再按一次退出程序");
                exitTime = secondExitTime;
            } else {
                finish();
            }
            //不返回true, 按一次就退出.因为它会执行下面的return super.onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.left_login_id:
                startActivity(intent);
                break;
        }
    }


    //按钮点击时候绑定滑动
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
                case R.id.resource_id:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.mine_id:
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    }

    //滑动时候绑定按钮事件
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
                    radioGroup.check(R.id.resource_id);
                    break;
                case 3:
                    radioGroup.check(R.id.mine_id);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //初始化四个碎片数据
    public void initViewPagerData() {
        HomePageFragment homePageFragment = new HomePageFragment(MainActivity.this);
        MarketFragment marketFragment = new MarketFragment(MainActivity.this);
        MineFragment mineFragment = new MineFragment(MainActivity.this);
        InformationFragment messageFragment = new InformationFragment(MainActivity.this);
        fragments.add(homePageFragment);
        fragments.add(marketFragment);
        fragments.add(messageFragment);
        fragments.add(mineFragment);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        //有值
        Log.d(TAG, "------initViewPagerData: " + myPagerAdapter);
        viewPager.setAdapter(myPagerAdapter);
    }

    //设置滑动适配器,滑动哪里加载哪个碎片
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
