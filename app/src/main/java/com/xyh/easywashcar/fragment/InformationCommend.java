package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xyh.easywashcar.R;

/**
 * Created by 向阳湖 on 2016/5/25.
 */
public class InformationCommend extends Fragment {
    private Context mContext;
    private LayoutInflater layoutInflater;
    public InformationCommend(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.information_commend, container, false);
        return view;
    }
}
