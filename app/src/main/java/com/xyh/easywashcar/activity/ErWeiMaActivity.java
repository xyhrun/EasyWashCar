package com.xyh.easywashcar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/6/2.
 */
public class ErWeiMaActivity extends Activity {

    @Bind(R.id.erWeiMa_webView_id)
    ProgressWebView mWebView;
    @Bind(R.id.erWeiMa_result_id)
    TextView erWeiMa_result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.erweima);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("result");
        //若是链接含有weixin就加载下面那个链接,让他下载微信客户端
        if (url.contains("http://weixin.qq.com")) {
            url = "https://weixin.qq.com/cgi-bin/readtemplate?t=weixin";
        }
        //如果连接不是以http开头就把扫描的结果放到textview显示出来
        if (!url.startsWith("http")) {
            erWeiMa_result.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            erWeiMa_result.setText(url);
        }
        showResult(url);
    }

    private void showResult(String sourceLink) {
        //网页全屏
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //设置WebView可触摸放大缩小：
        mWebView.getSettings().setBuiltInZoomControls(true);
//        WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置：
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.loadUrl(sourceLink);
        //覆盖WebView通过第三方浏览器访问网页的行为，使得网页在WebView中打开
    }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
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
