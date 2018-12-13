package com.feng.wanandroid.view.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.ArticleAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.ArticleData;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.view.activity.ShowArticleActivity;
import com.feng.wanandroid.widget.LoadMoreScrollListener;

import java.util.ArrayList;
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
    private static final int REFRESH_TIME = 500;

    @BindView(R.id.rv_home_article_list)
    RecyclerView mArticleRv;
    @BindView(R.id.pb_home)
    ProgressBar mProgressBar;
    @BindView(R.id.srv_home_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ArticleAdapter mArticleAdapter;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

    @Override
    protected void doInOnCreate() {
        mPresenter.getHomeArticle(currentPage++);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_tab_one));   //设置颜色
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            //刷新时的操作
            new Handler().postDelayed(this::refresh, REFRESH_TIME);
        });

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;   //禁止垂直滑动
//            }
//        };
//        mArticleRv.setLayoutManager(linearLayoutManager);
        mArticleRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    /**
     * 获取首页文章成功
     *
     * @param articleDataList
     */
    @Override
    public void getHomeArticleSuccess(List<ArticleData> articleDataList) {
        mArticleDataList = articleDataList;
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if (mArticleAdapter == null) {
            if (mArticleDataList == null) {
                showShortToast("列表没有内容");
            } else {    //初始化adapter并进行RV的配置
                mArticleAdapter = new ArticleAdapter(getContext(), mArticleDataList, this);
                mArticleAdapter.setClickListener(new ArticleAdapter.OnClickListener() {
                    @Override
                    public void clickCollect(boolean collect, int id, int position) {
                        if (!collect) { //收藏
                            mPresenter.collect(id, position);
                        }
                    }

                    @Override
                    public void clickItem(String link, String title) {
                        Intent intent = new Intent(getContext(), ShowArticleActivity.class);
                        intent.putExtra(ShowArticleActivity.Link_TAG, link);
                        intent.putExtra(ShowArticleActivity.TITLE_TAG, title);
                        startActivity(intent);
                    }
                });
                mArticleRv.setAdapter(mArticleAdapter);
                mArticleRv.addOnScrollListener(new LoadMoreScrollListener(mArticleAdapter));
            }
        } else {
            if (mArticleDataList == null) {
                mArticleAdapter.setLastedStatus();
            } else {
                mArticleAdapter.updateList();
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
        mSwipeRefreshLayout.setRefreshing(false);
        if (mArticleAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mArticleAdapter.setErrorStatus();
        }
    }

    /**
     * 收藏成功
     *
     * @param position
     */
    @Override
    public void collectSuccess(int position) {
        mArticleDataList.get(position).setCollect(true);    //更新集合信息
        mArticleAdapter.notifyDataSetChanged(); //更新列表
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

    @Override
    public void loadMore() {
        mPresenter.getHomeArticle(currentPage++);
    }

    /**
     * 下拉刷新的操作
     */
    private void refresh() {
        //更新列表（重新获取数据）
        currentPage = 0;
        mArticleAdapter = null;     //重置adapter，等于重头再来
        mPresenter.getHomeArticle(currentPage++);
    }
}
