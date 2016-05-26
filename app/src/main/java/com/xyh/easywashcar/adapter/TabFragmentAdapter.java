package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 向阳湖 on 2016/5/25.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

//    private InformationCommend informationCommend;
//    private CarCyclopaedia carCyclopaedia;
//    private NecessarySupply necessarySupply;
    private Context mContext;

    private int tabNum;
    private FragmentManager manager;
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public TabFragmentAdapter(FragmentManager fm, Context context, int tabNum, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);
        this.mContext = context;
        this.manager = fm;
        this.tabNum = tabNum;
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
//        //隐藏所有fragment
//        hideFragments(manager.beginTransaction());
//        switch (position) {
//            case 0:
//                if (informationCommend == null) {
//                    informationCommend = new InformationCommend(mContext);
//                    Logger.d("-----"+informationCommend);
//                }
//                return informationCommend;
//            case 1:
//                if (carCyclopaedia == null) {
//                    carCyclopaedia = new CarCyclopaedia(mContext);
//                    Logger.d("-----"+informationCommend);
//                }
//                return carCyclopaedia;
//            case 2:
//                if (necessarySupply == null) {
//                    necessarySupply = new NecessarySupply(mContext);
//                    Logger.d("-----"+informationCommend);
//                }
//                return necessarySupply;
//        }
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabNum;
    }

    /**
     * 设置标题栏文本，必须复写
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    /**
     * 隐藏所有fragment，提高复用率
     *
     * @param transaction
     */
//    public void hideFragments(FragmentTransaction transaction) {
//
//        if (informationCommend != null) {
//            transaction.hide(informationCommend);
//        }
//        if (carCyclopaedia != null) {
//            transaction.hide(carCyclopaedia);
//        }
//        if (necessarySupply != null) {
//            transaction.hide(necessarySupply);
//        }
//    }
}
