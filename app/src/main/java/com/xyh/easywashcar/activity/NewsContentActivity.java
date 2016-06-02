package com.xyh.easywashcar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xyh.easywashcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/6/2.
 */
public class NewsContentActivity extends AppCompatActivity {
    private static final String TAG = "NewsContentActivity";

    @Bind(R.id.webView_id)
    WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        ButterKnife.bind(this);
        String sourceLink = getIntent().getStringExtra("sourceLink");
        //有数据
//        String html = getIntent().getStringExtra("html");
//        Log.i(TAG, "--onCreate: "+html);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

//        String mHtml = "<html><head>" + "</head><body>" + html + "</body></html>";
//        html = html.replace("<div class=\"img-place-holder\">", "");
//        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        mWebView.loadUrl(sourceLink);

    }
}
