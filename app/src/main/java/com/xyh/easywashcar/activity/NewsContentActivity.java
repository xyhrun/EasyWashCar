package com.xyh.easywashcar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        showContent(sourceLink);
    }

    private void showContent(String sourceLink) {
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
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //显示webview跳转页面的进度条
//        mWebView.setWebChromeClient(new WebChromeClient(){
//            ProgressDialog dialog = null;
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                // newProgress 1-100的整数，表示进度
//                if(newProgress==100){
//                    closeDialog();
//                }
//                else{
//                    openDialog(newProgress);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//
//            private void openDialog(int newProgress) {
//                if(dialog==null){
//                    dialog=new ProgressDialog(NewsContentActivity.this);
//                    dialog.setTitle("玩命加载中.....");
//                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    dialog.setProgress(newProgress);
//                    dialog.show();
//                }
//                else{
//                    dialog.setProgress(newProgress);
//                }
//            }
//
//            private void closeDialog() {
//                if(dialog!=null&&dialog.isShowing()){
//                    dialog.dismiss();
//                }
//
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
