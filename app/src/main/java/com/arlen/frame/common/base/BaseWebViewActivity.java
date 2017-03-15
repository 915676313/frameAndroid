package com.arlen.frame.common.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arlen.frame.R;

import butterknife.Bind;


@SuppressLint("SetJavaScriptEnabled")
public class BaseWebViewActivity extends BaseActivity {

    private String mUrl;
    private String mTitle;
    @Bind(R.id.webView)
    WebView mWebView;

    private static final String INTENT_URL = "url";
    private static final String INTENT_TITLE = "title";

    public static void startBaseWebViewActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(INTENT_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mUrl = getIntent().getStringExtra(INTENT_URL);
        mTitle = getIntent().getStringExtra(INTENT_TITLE);
        getTitleBar().setHeaderTitle(mTitle);
        getTitleBar().setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();// 返回前一个页面
                } else {
                    finish();
                }
            }
        });
        loadData();
    }

    protected void loadData() {
        initWebView();
        mWebView.loadUrl(mUrl);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        // 设置支持JavaScript脚本
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true); // 允许webView执行JavaScript

        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", MODE_PRIVATE).getPath();
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setAllowFileAccess(true);//设置启用或禁止访问文件数据
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportZoom(false);// 支持缩放
        webSettings.setBuiltInZoomControls(false);// 设置出现缩放工具
        webSettings.setUseWideViewPort(false);// 扩大比例的缩放
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        /**
         * 自己处理点击事件,返回true
         */
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    if (url.contains("http:")) {
                        return false;
                    }
                    if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getTitleBar().setHeaderTitle(title);
            }

//            @Override
//            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
//                geolocationPermissionsCallback.invoke(s, true, false);
//                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
//            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }
}
