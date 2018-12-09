package com.feng.wanandroid.view.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.presenter.CollectionPresenter;

import java.util.Objects;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements ICollectionContract.View {

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_collection_content)
    TextView mContentTv;
    @BindView(R.id.pb_collection)
    ProgressBar mProgressBar;

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
        mPresenter.getCollectList(0);
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
    protected void initData() {

    }

    @Override
    public void getCollectListSuccess(CollectArticleBean collectArticleBean) {
        mProgressBar.setVisibility(View.GONE);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < collectArticleBean.getData().getDatas().size(); i++) {
            builder.append(collectArticleBean.getData().getDatas().get(i).getTitle());
            builder.append("\n");
        }
        mContentTv.setText(builder.toString());
    }

    @Override
    public void getCollectListError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(errorMsg);
    }
}
