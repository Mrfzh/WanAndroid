package com.feng.wanandroid.view.fragment;

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
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.view.activity.ShowArticleActivity;
import com.feng.wanandroid.widget.LoadMoreScrollListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 *
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeContract.View,
        BasePagingLoadAdapter.LoadMoreListener {

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
        mSwipeRefreshLayout.setOnRefreshListener(() -> {    //监听下滑刷新
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
                initAdapter();
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

    /**
     * 取消收藏成功
     *
     * @param position
     */
    @Override
    public void unCollectSuccess(int position) {
        mArticleDataList.get(position).setCollect(false);
        mArticleAdapter.notifyDataSetChanged();
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
    public void loadMore() {
        mPresenter.getHomeArticle(currentPage++);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event<HomeEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.ShowArticle2Home:
                if (event.getData().isRefresh()) {
                    //刷新列表
                    refresh();
                } else {
                    //更新文章收藏信息
                    mArticleDataList.get(event.getData().getPosition()).setCollect(event.getData().isCollect());
                    mArticleAdapter.notifyDataSetChanged();
                }
                break;
            case EventBusCode.Collection2Home:
            case EventBusCode.Register2Home:
            case EventBusCode.Login2Home:
            case EventBusCode.Main2Home:
                if (event.getData().isRefresh()) {
                    //刷新列表
                    refresh();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mArticleAdapter = new ArticleAdapter(getContext(), mArticleDataList, this);
        mArticleAdapter.setClickListener(new ArticleAdapter.OnClickListener() {
            @Override
            public void clickCollect(boolean collect, int id, int position) {
                if (!collect) { //收藏
                    mPresenter.collect(id, position);
                } else {
                    mPresenter.unCollect(id, position);
                }
            }

            @Override
            public void clickItem(String link, String title, boolean isCollect, int id, int position) {
                Event<ShowArticleEvent> event = new Event<>(EventBusCode.Home2ShowArticle, new ShowArticleEvent(link,
                        title, isCollect, id, position, false));
                EventBusUtil.sendStickyEvent(event);
                jump2Activity(ShowArticleActivity.class);
            }
        });
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
