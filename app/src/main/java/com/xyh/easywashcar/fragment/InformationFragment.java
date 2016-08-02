package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.adapter.TabFragmentAdapter;

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
    private TabFragmentAdapter tabFragmentAdapter;

    public InformationFragment(Context context) {
        this.context = context;
    }

    private List<Fragment> fragmentList;
    private InformationCommend informationCommend;
    private CarCyclopaedia carCyclopaedia;
    private NecessarySupply necessarySupply;
    @Bind(R.id.information_tabLayout_id)
    TabLayout mTabLayout;

    @Bind(R.id.information_content_id)
    FrameLayout mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "------InformationFragment onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, view);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        informationCommend = new InformationCommend(context);
//        fragmentTransaction.add(R.id.information_content_id, informationCommend);
        initData();
        setTabSelection(0);
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

        //添加碎片
//        fragmentList = new ArrayList<>();
//        informationCommend = new InformationCommend(context);
//        carCyclopaedia = new CarCyclopaedia(context);
//        necessarySupply = new NecessarySupply(context);
//        fragmentList.add(informationCommend);
//        fragmentList.add(carCyclopaedia);
//        fragmentList.add(necessarySupply);

        //设置tab模式，当前为系统默认模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //绑定tab标题数据
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(2)));
        //实例化适配器
        tabFragmentAdapter = new TabFragmentAdapter(getFragmentManager(), context, mTabLayout.getTabCount(), titleList, fragmentList);

        //TabLayout绑定标题栏点击适配器
        mTabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabSelection(mTabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabSelection(int selectedTabPosition) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (selectedTabPosition) {
            case 0:
                if (informationCommend == null) {
                    informationCommend = new InformationCommend(context);
                    fragmentTransaction.add(R.id.information_content_id, informationCommend);
                } else {
                    fragmentTransaction.show(informationCommend);
                }
                break;
            case 1:
                if (carCyclopaedia == null) {
                    carCyclopaedia = new CarCyclopaedia(context);
                    fragmentTransaction.add(R.id.information_content_id, carCyclopaedia);
                } else {
                    fragmentTransaction.show(carCyclopaedia);
                }
                break;
            case 2:
                if (necessarySupply == null) {
                    necessarySupply = new NecessarySupply(context);
                    fragmentTransaction.add(R.id.information_content_id, necessarySupply);
                } else {
                    fragmentTransaction.show(necessarySupply);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (informationCommend != null) {
            transaction.hide(informationCommend);
        }
        if (carCyclopaedia != null) {
            transaction.hide(carCyclopaedia);
        }
        if (necessarySupply != null) {
            transaction.hide(necessarySupply);
        }
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
