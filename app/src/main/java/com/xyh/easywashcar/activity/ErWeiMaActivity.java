package com.xyh.easywashcar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xyh.easywashcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/6/2.
 */
public class ErWeiMaActivity extends Activity {

    @Bind(R.id.erWeiMa_webView_id)
    WebView mWebView;
    @Bind(R.id.erWeiMa_result_id)
    TextView erWeiMa_result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.erweima);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("result");
        if (url.contains("http://weixin.qq.com")) {
            url = "https://weixin.qq.com/cgi-bin/readtemplate?t=weixin";
        }
        if (!url.startsWith("http")) {
            erWeiMa_result.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            erWeiMa_result.setText(url);
        }
        showResult(url);
    }

    private void showResult(String sourceLink) {
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
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
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
