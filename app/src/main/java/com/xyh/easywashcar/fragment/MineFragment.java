package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.LoginActivity;
import com.xyh.easywashcar.base.MyAppcation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.mine_login_id)
    Button mine_login;
    @Bind(R.id.mine_head_layout_id)
    LinearLayout mine_head_layout;
    @Bind(R.id.mine_body_layout_id)
    LinearLayout mine_body_layout;
    private static final String TAG = "MineFragment";
    private Context context;
    public MineFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "------MineFragment onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnClickListener();
    }

    private void setOnClickListener() {
        mine_login.setOnClickListener(this);
        mine_head_layout.setOnClickListener(this);
        mine_body_layout.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "------ MineFragment onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "------ MineFragment onDestroy: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "------ MineFragment onDestroyView: ");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, LoginActivity.class);
        switch (v.getId()) {
            case R.id.mine_login_id:
                startActivity(intent);
                break;
            case R.id.mine_head_layout_id:
                MyAppcation.myToast("请先登录");
                break;
            case R.id.mine_body_layout_id:
                MyAppcation.myToast("请先登录");
                break;

        }
    }
}
