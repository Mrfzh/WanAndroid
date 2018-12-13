package com.feng.wanandroid.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.ArticleAdapter;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.ArticleData;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.presenter.CollectionPresenter;
import com.feng.wanandroid.widget.LoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements ICollectionContract.View,
        BasePagingLoadAdapter.LoadMoreListener {

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_collection)
    ProgressBar mProgressBar;
    @BindView(R.id.rv_collection_recycler_view)
    RecyclerView mCollectionRv;
    @BindView(R.id.tv_collection_no_article)
    TextView mNoArticleTv;

    private ArticleAdapter mAdapter = null;
    private int pageIndex = 0;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

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
        mPresenter.getCollectList(pageIndex++);

        mCollectionRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void doInOnCreate() {

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
    public void getCollectListSuccess(List<ArticleData> articleDataList) {
        mArticleDataList = articleDataList;
        mProgressBar.setVisibility(View.GONE);
        //总的来说就是根据articleDataList和mAdapter是否为null分为4种情况
        if (articleDataList == null && mAdapter == null) {
            mNoArticleTv.setVisibility(View.VISIBLE);
        } else {
            mNoArticleTv.setVisibility(View.GONE);
            if (articleDataList !=  null && mAdapter == null) { //初始化adapter
                mAdapter = new ArticleAdapter(this, mArticleDataList, this);
                mAdapter.setClickListener(new ArticleAdapter.OnClickListener() {
                    @Override
                    public void clickCollect(boolean collect, int id, int position) {
                        showShortToast("is collect ? " + collect);
                    }

                    @Override
                    public void clickItem(String link, String title) {
                        Intent intent = new Intent(CollectionActivity.this, ShowArticleActivity.class);
                        intent.putExtra(ShowArticleActivity.Link_TAG, link);
                        intent.putExtra(ShowArticleActivity.TITLE_TAG, title);
                        startActivity(intent);
                    }
                });
                mCollectionRv.addOnScrollListener(new LoadMoreScrollListener(mAdapter));
                mCollectionRv.setAdapter(mAdapter);
            } else {
                if (articleDataList == null) {
                    mAdapter.setLastedStatus(); //显示没有更多
                } else {
                    mAdapter.updateList();  //更新列表
                }
            }
        }
    }

    @Override
    public void getCollectListError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mAdapter.setErrorStatus();
        }
    }

    @Override
    public void loadMore() {
        mPresenter.getCollectList(pageIndex++);
    }
}
