package com.feng.wanandroid.view.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.ArticleAdapter;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.eventbus.CollectionEvent;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.presenter.CollectionPresenter;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.widget.LoadMoreScrollListener;
import com.feng.wanandroid.widget.dialog.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements ICollectionContract.View,
        BasePagingLoadAdapter.LoadMoreListener, IHomeContract.View {

    private static final int DELAY_TIME = 2 * 500;
    private static final String TAG = "CollectionActivity";

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_collection)
    ProgressBar mProgressBar;
    @BindView(R.id.rv_collection_recycler_view)
    RecyclerView mCollectionRv;
    @BindView(R.id.tv_collection_no_article)
    TextView mNoArticleTv;
    @BindView(R.id.srv_collection_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ArticleAdapter mAdapter = null;
    private int currentPage = 0;
    private List<ArticleData> mArticleDataList = new ArrayList<>();
    private HomePresenter mHomePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected CollectionPresenter getPresenter() {
        return new CollectionPresenter();
    }

    @Override
    protected void initView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getCollectList(currentPage++);

        mCollectionRv.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_tab_one));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(this::refresh, DELAY_TIME);
        });
    }

    @Override
    protected void doInOnCreate() {
        mHomePresenter = new HomePresenter();
        mHomePresenter.attachView(this);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("我的收藏");
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
    protected void onDestroy() {
        super.onDestroy();
        if (mHomePresenter != null) {
            mHomePresenter.detachView();
        }
    }

    @Override
    public void getCollectListSuccess(List<ArticleData> articleDataList) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        //总的来说就是根据articleDataList和mAdapter是否为null分为4种情况
        if (articleDataList == null && mAdapter == null) {
            mNoArticleTv.setVisibility(View.VISIBLE);   //第一次载入没有数据时提示‘无收藏文章’
        } else {
            if (articleDataList !=  null && mAdapter == null) {
                mNoArticleTv.setVisibility(View.GONE);  //这时才隐藏‘无文章提示’
                mArticleDataList = articleDataList;     //在articleDataList !=  null时才设置mList
                //初始化adapter
                initAdapter();
                mCollectionRv.addOnScrollListener(new LoadMoreScrollListener(mAdapter));
                mCollectionRv.setAdapter(mAdapter);
            } else {
                if (articleDataList == null) {
                    mAdapter.setLastedStatus(); //显示没有更多
                } else {
                    mArticleDataList = articleDataList;  //同上，在返回的list不是null时才设置mList，
                                                        // 保证mList不会在有item的时候被置为空，不然取消收藏会失败
                    mAdapter.updateList();  //更新列表
                }
            }
        }
    }

    @Override
    public void getCollectListError(String errorMsg) {
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mAdapter.setErrorStatus();
        }
    }

    @Override
    public void loadMore() {
        mPresenter.getCollectList(currentPage++);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusEvent(Event<CollectionEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.ShowArticle2Collection:
                if (event.getData().isRefresh()) {
                    refresh();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getHomeArticleSuccess(List<ArticleData> articleDataList) {

    }

    @Override
    public void getHomeArticleError(String errorMsg) {

    }

    @Override
    public void collectSuccess(int position) {
    }

    @Override
    public void collectError(String errorMsg) {

    }

    @Override
    public void unCollectSuccess(int position) {
        if (mArticleDataList != null) {
            //更新收藏列表
            mArticleDataList.remove(position);
            mAdapter.notifyItemRemoved(position);
            if (position != mArticleDataList.size()) {
                mAdapter.notifyItemRangeChanged(position, mArticleDataList.size() - position);
            }
            //当没有收藏文章时提示文字
            if (mArticleDataList.size() == 0) {
                mNoArticleTv.setVisibility(View.VISIBLE);
            }
        }
        //更新首页文章
        Event<HomeEvent> event = new Event<>(EventBusCode.Collection2Home, new HomeEvent(true));
        EventBusUtil.sendEvent(event);
    }

    @Override
    public void unCollectError(String errorMsg) {
        showShortToast(errorMsg);
    }

    /**
     * 更新adapter
     */
    private void refresh() {
        //更新列表（重新获取数据）
        currentPage = 0;
        mAdapter = null;     //重置adapter，等于重头再来
        mPresenter.getCollectList(currentPage++);
    }

    private void initAdapter() {
        mAdapter = new ArticleAdapter(this, mArticleDataList, this);
        mAdapter.setClickListener(new ArticleAdapter.OnClickListener() {
            @Override
            public void clickCollect(boolean collect, int id, int position) {
                if (collect) {
                    TipDialog tipDialog = new TipDialog.Builder(CollectionActivity.this)
                            .setContent("确定要取消收藏？")
                            .setOnClickListener(new TipDialog.OnClickListener() {
                                @Override
                                public void clickEnsure() {
                                    mHomePresenter.unCollect(id, position);
                                }

                                @Override
                                public void clickCancel() {

                                }
                            })
                            .build();
                    tipDialog.show();
                }
            }

            @Override
            public void clickItem(String link, String title, boolean isCollect, int id, int position) {
                Event<ShowArticleEvent> event = new Event<>(EventBusCode.Collection2ShowArticle, new ShowArticleEvent(link,
                        title, isCollect, id, position, true));
                EventBusUtil.sendStickyEvent(event);
                jumpToNewActivity(ShowArticleActivity.class);
            }
        });
    }
}
