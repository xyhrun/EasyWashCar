package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.adapter.InformationPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class InformationFragment extends Fragment {
    private static final String TAG = "InformationFragment";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> titleList;
    public InformationFragment(Context context) {
        this.context = context;
    }

    private List<Fragment> fragmentList;
    private InformationCommend informationCommend;
    private CarCyclopaedia carCyclopaedia;
    private NecessarySupply necessarySupply;
    private InformationPagerAdapter mInformationPagerAdapter;
    @Bind(R.id.information_tabLayout_id)
    TabLayout mTabLayout;
    @Bind(R.id.information_viewPager_id)
    ViewPager mViewPager;
    @Bind(R.id.information_content_id)
    FrameLayout mContent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "------InformationFragment onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, view);
        initData();
        //设置一开始加载几个碎片, 默认是一个. 未来保证数据不丢失,设置为(碎片个数 - 1)
        mViewPager.setOffscreenPageLimit(2);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initData();
    }

    //初始化数据
    private void initData() {
        //添加标题数据
        titleList = new ArrayList<>();
        titleList.add("资讯推荐");
        titleList.add("汽车百科");
        titleList.add("汽车用品");

//        添加碎片
        fragmentList = new ArrayList<>();
        informationCommend = new InformationCommend(context);
        carCyclopaedia = new CarCyclopaedia(context);
        necessarySupply = new NecessarySupply(context);
        fragmentList.add(informationCommend);
        fragmentList.add(carCyclopaedia);
        fragmentList.add(necessarySupply);

        mInformationPagerAdapter = new InformationPagerAdapter(getFragmentManager(), titleList, fragmentList);
        mViewPager.setAdapter(mInformationPagerAdapter);
        //TabLayout与ViewPager关联
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "------ InformationFragment onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "------ InformationFragment onDestroy: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "------ InformationFragment onDestroyView: ");
    }
}
