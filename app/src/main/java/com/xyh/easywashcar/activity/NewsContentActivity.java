package com.xyh.easywashcar.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/6/2.
 */
public class NewsContentActivity extends AppCompatActivity {
    private static final String TAG = "NewsContentActivity";
    private ProgressDialog dialog = null;
    @Bind(R.id.webView_id)
    ProgressWebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        ButterKnife.bind(this);
        String sourceLink = getIntent().getStringExtra("sourceLink");
        String washCarLink = getIntent().getStringExtra("washCarLink");
        //有数据
//        String html = getIntent().getStringExtra("html");
//        Log.i(TAG, "--onCreate: "+html);
        if (sourceLink != null) {
            showContent(sourceLink);
        }

        if (washCarLink != null) {
            showContent(washCarLink);
        }
    }

    private void showContent(String sourceLink) {
        mWebView.loadUrl(sourceLink);


        //d对webView进行优化
        mWebView.getSettings().setJavaScriptEnabled(true);
//        //39适应竖屏,57适应横屏,效果并不好
////        mWebView.setInitialScale(39);


//        //网页全屏
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //设置WebView可触摸放大缩小：
        mWebView.getSettings().setBuiltInZoomControls(true);
//        WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置：
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

    }
        //对返回进行判断,防止一次退出
        @Override
        public boolean onKeyDown (int keyCode, KeyEvent event){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //如果可以返回就返回到上一个网页位置,否则退出程序
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                } else {
                    this.finish();
                }
            }
            return super.onKeyDown(keyCode, event);
        }

}
