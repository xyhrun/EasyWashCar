package com.xyh.easywashcar.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xyh.easywashcar.R;


public class BaseActivity extends Activity {
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void finish() {
        super.finish();
        //复写活动过渡界面，进去从右到左，出来从左到右
        overridePendingTransition(R.anim.anim_right_in,R.anim.anim_right_out);
    }


    public void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.commit();
    }
}
