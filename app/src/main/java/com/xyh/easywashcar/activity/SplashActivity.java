package com.xyh.easywashcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/24.
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.shimmer_viewcontainer_id)
    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acyivity_splash);
        ButterKnife.bind(this);
        shimmerFrameLayout.startShimmerAnimation();
        //两秒后启动主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
    }
}
