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
import com.feng.wanandroid.contract.IShowArticleContract;
import com.feng.wanandroid.entity.eventbus.CollectionEvent;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.entity.eventbus.TreeArticleCatalogEvent;
import com.feng.wanandroid.presenter.ShowArticlePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.utils.IntentUtil;
import com.feng.wanandroid.utils.ShareUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class ShowArticleActivity extends BaseActivity<ShowArticlePresenter> implements IShowArticleContract.View {

    private String mLink;
    private String mTitle;
    private boolean mIsCollect;
    private int mPosition;
    private int mId;
    private boolean mIsHideCollect = false;     //是否隐藏收藏菜单
    private int mFrom;          //活动跳转来源

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
    protected ShowArticlePresenter getPresenter() {
        return new ShowArticlePresenter();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mProgressBar = findViewById(R.id.pb_show_article_progress_bar);

        mShowArticleWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mShowArticleWebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mShowArticleWebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mShowArticleWebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mShowArticleWebView.getSettings().setDisplayZoomControls(false);//隐藏缩放工具
        mShowArticleWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mShowArticleWebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mShowArticleWebView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        mShowArticleWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_show_article, menu);   //设置菜单
        if (mIsHideCollect) {   //如果要隐藏收藏菜单项
            menu.getItem(0).setVisible(false);
        } else {
            //设置收藏还是取消收藏
            if (mIsCollect) {
                setMenuTitle(menu, 0, "取消收藏");
            } else {
                setMenuTitle(menu, 0, "收藏");
            }
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

        switch (mFrom) {
            case ShowArticleEvent.FROM_HOME:    //如果是从首页跳转过来
                //更新首页文章
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(position, true)));
                break;
            case ShowArticleEvent.FROM_COLLECT: //从收藏页面跳转过来
                //直接刷新首页文章
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(true)));
                break;
            case ShowArticleEvent.FROM_TREE:    //从体系文章跳转而来
                //更新体系文章
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2TreeArticleCatalog,
                        new TreeArticleCatalogEvent(position, true)));
                break;
            default:
                break;
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

        switch (mFrom) {
            case ShowArticleEvent.FROM_HOME:    //如果是从首页跳转过来
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(position, false)));
                break;
            case ShowArticleEvent.FROM_COLLECT: //从收藏页面跳转过来
                //直接刷新首页文章
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2Home, new HomeEvent(true)));
                //刷新收藏页面
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2Collection, new CollectionEvent(true)));
                break;
            case ShowArticleEvent.FROM_TREE:
                //更新体系文章
                EventBusUtil.sendEvent(new Event<>(EventBusCode.ShowArticle2TreeArticleCatalog,
                        new TreeArticleCatalogEvent(position, false)));
                break;
            default:
                break;
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
            case EventBusCode.TreeArticle2ShowArticle:
                mLink = event.getData().getLink();
                mTitle = event.getData().getTitle();
                mIsCollect = event.getData().isCollect();
                mId = event.getData().getId();
                mPosition = event.getData().getPosition();
                mFrom = event.getData().getFrom();
                mIsHideCollect = event.getData().isHideCollect();
                break;
            case EventBusCode.HomeBanner2ShowArticle:
            case EventBusCode.Navigation2ShowArticle:
                mLink = event.getData().getLink();
                mTitle = event.getData().getTitle();
                mIsHideCollect = true;
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
