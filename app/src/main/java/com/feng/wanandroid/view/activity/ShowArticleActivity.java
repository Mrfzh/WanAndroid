package com.feng.wanandroid.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.eventbus.CollectionEvent;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.utils.IntentUtil;
import com.feng.wanandroid.utils.ShareUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class ShowArticleActivity extends BaseActivity<HomePresenter> implements IHomeContract.View {

    private String mLink;
    private String mTitle;
    private boolean mIsCollect;
    private int mPosition;
    private int mId;
    private boolean mIsFromCollect;     //是否从收藏页面跳转来

    private Menu mMenu;

    @BindView(R.id.wv_show_article_web_view)
    WebView mShowArticleWebView;
    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
//    @BindView(R.id.pb_show_article_progress_bar)  //用Butterknife绑定后多次点击item出错，显示空指针异常
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_article;
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mShowArticleWebView.getSettings().setJavaScriptEnabled(true);

        mProgressBar = findViewById(R.id.pb_show_article_progress_bar);
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
    protected void doInOnCreate() {

    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected boolean setToolbarBackIcon() {
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle(mTitle);
//        mToolbar.setTitleTextAppearance(this, R.style.ArticleToolbarTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_show_article, menu);   //设置菜单
        //设置收藏还是取消收藏
        if (mIsCollect) {
            setMenuTitle(menu, 0, "取消收藏");
        } else {
            setMenuTitle(menu, 0, "收藏");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //处理菜单项的点击
        switch (item.getItemId()) {
            case R.id.menu_show_article_collect_or_uncollect:
                if (mIsCollect) {
                    //取消收藏
                    mPresenter.unCollect(mId, mPosition);
                } else {
                    //收藏
                    mPresenter.collect(mId, mPosition);
                }
                break;
            case R.id.menu_show_article_share:
                String shareText = "【WanAndroid分享】" + mTitle + ",链接" + mLink;
                ShareUtil.shareText(shareText, ShowArticleActivity.this);
                break;
            case R.id.menu_show_article_open_in_browser:
                IntentUtil.callLocalBrowser(ShowArticleActivity.this, mLink);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void getHomeArticleSuccess(List<ArticleData> articleDataList) {

    }

    @Override
    public void getHomeArticleError(String errorMsg) {

    }

    /**
     * 收藏成功
     *
     * @param position
     */
    @Override
    public void collectSuccess(int position) {
        showShortToast("收藏成功");
        setMenuTitle(mMenu, 0, "取消收藏");
        mIsCollect = true;
        if (!mIsFromCollect) {
            Event<HomeEvent> event = new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(position, true));
            EventBusUtil.sendEvent(event);
        } else {
            //直接刷新首页文章
            Event<HomeEvent> event = new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(true));
            EventBusUtil.sendEvent(event);
        }
    }

    /**
     * 收藏失败
     *
     * @param errorMsg
     */
    @Override
    public void collectError(String errorMsg) {
        showShortToast(errorMsg);
    }

    /**
     * 取消收藏成功
     *
     * @param position
     */
    @Override
    public void unCollectSuccess(int position) {
        showShortToast("取消收藏");
        setMenuTitle(mMenu, 0, "收藏");
        mIsCollect = false;
        if (!mIsFromCollect) {
            Event<HomeEvent> event = new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(position, false));
            EventBusUtil.sendEvent(event);
        } else {
            //直接刷新首页文章
            Event<HomeEvent> event = new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(true));
            EventBusUtil.sendEvent(event);
            //刷新收藏页面
            Event<CollectionEvent> event1 = new Event<>(EventBusCode.ShowArticle2Collection, new CollectionEvent(true));
            EventBusUtil.sendEvent(event1);
        }
    }

    /**
     * 取消收藏失败
     *
     * @param errorMsg
     */
    @Override
    public void unCollectError(String errorMsg) {
        showShortToast(errorMsg);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event<ShowArticleEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.Home2ShowArticle:
            case EventBusCode.Collection2ShowArticle:
                mLink = event.getData().getLink();
                mTitle = event.getData().getTitle();
                mIsCollect = event.getData().isCollect();
                mId = event.getData().getId();
                mPosition = event.getData().getPosition();
                mIsFromCollect = event.getData().isFromCollect();
                break;
            default:
                break;
        }
    }

    /**
     * 设置菜单项的title
     *
     * @param menu 当前menu
     * @param position 菜单项索引
     * @param title 要设置的title
     */
    private void setMenuTitle(Menu menu, int position, String title) {
        menu.getItem(position).setTitle(title);
    }

}
