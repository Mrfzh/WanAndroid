package com.feng.wanandroid.view.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.HomeArticleAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.HomeArticleData;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.view.activity.ShowArticleActivity;
import com.feng.wanandroid.widget.LoadMoreScrollListener;

import java.util.List;

import butterknife.BindView;

/**
 * 首页
 *
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeContract.View, BasePagingLoadAdapter.LoadMoreListener {

    private static int currentPage = 0;

    @BindView(R.id.rv_home_article_list)
    RecyclerView mArticleRv;
    @BindView(R.id.pb_home)
    ProgressBar mProgressBar;

    private HomeArticleAdapter mHomeArticleAdapter;

    @Override
    protected void doInOnCreate() {
        mPresenter.getHomeArticle(currentPage);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;   //禁止垂直滑动
//            }
//        };
//        mArticleRv.setLayoutManager(linearLayoutManager);
        mProgressBar.setVisibility(View.VISIBLE);
        mArticleRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    /**
     * 获取首页文章成功
     *
     * @param homeArticleDataList
     */
    @Override
    public void getHomeArticleSuccess(List<HomeArticleData> homeArticleDataList) {
        mProgressBar.setVisibility(View.GONE);
        if (mHomeArticleAdapter == null) {
            if (homeArticleDataList == null) {
                showShortToast("列表没有内容");
            } else {
                mHomeArticleAdapter = new HomeArticleAdapter(getContext(), homeArticleDataList, this);
                mHomeArticleAdapter.setClickListener(new HomeArticleAdapter.OnClickListener() {
                    @Override
                    public void clickCollect(boolean collect) {
                        showShortToast("is collect ? " + collect);
                    }

                    @Override
                    public void clickItem(String link, String title) {
                        Intent intent = new Intent(getContext(), ShowArticleActivity.class);
                        intent.putExtra(ShowArticleActivity.Link_TAG, link);
                        intent.putExtra(ShowArticleActivity.TITLE_TAG, title);
                        startActivity(intent);
                    }
                });
                mArticleRv.setAdapter(mHomeArticleAdapter);
                mArticleRv.addOnScrollListener(new LoadMoreScrollListener(mHomeArticleAdapter));
            }
        } else {
            if (homeArticleDataList == null) {
                mHomeArticleAdapter.setLastedStatus();
            } else {
                mHomeArticleAdapter.updateList();
            }
        }
    }

    /**
     * 获取首页文章失败
     *
     * @param errorMsg
     */
    @Override
    public void getHomeArticleError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if (mHomeArticleAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mHomeArticleAdapter.setErrorStatus();
        }
    }

    @Override
    public void loadMore() {
        mPresenter.getHomeArticle(currentPage++);
    }
}
