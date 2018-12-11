package com.feng.wanandroid.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowArticleActivity extends BaseActivity {
    public static final String Link_TAG = "link_tag";
    public static final String TITLE_TAG = "title_tag";
    private String mLink;
    private String mTitle;

    @BindView(R.id.wv_show_article_web_view)
    WebView mShowArticleWebView;
    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_show_article_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_article;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mShowArticleWebView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //页面开始加载
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载完成
                mProgressBar.setVisibility(View.GONE);
            }
        };
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //设置加载进度
                mProgressBar.setProgress(newProgress);
            }
        };
        mShowArticleWebView.setWebViewClient(webViewClient);
        mShowArticleWebView.setWebChromeClient(webChromeClient);
        mShowArticleWebView.loadUrl(mLink);
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mLink = intent.getStringExtra(Link_TAG);
        mTitle = intent.getStringExtra(TITLE_TAG);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle(mTitle);
        mToolbar.setTitleTextAppearance(this, R.style.ArticleToolbarTitle);
        mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }
}
