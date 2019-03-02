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

//    private static final String TAG = "fzh";
    private static int currentPage = 0;
    private static final int REFRESH_TIME = 500;
    private static int LOAD_TIME = 1;    //加载次数（第二次加载时不需要再addOnScrollListener，不然添加了多个监听器后会导致下拉加载出错）

    @BindView(R.id.rv_home_article_list)
    RecyclerView mArticleRv;
    @BindView(R.id.pb_home)
    ProgressBar mProgressBar;
    @BindView(R.id.srv_home_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ArticleAdapter mArticleAdapter;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

    private List<String> mImageUrlList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private List<String> mUrlList = new ArrayList<>();

    @Override
    protected void doInOnCreate() {
        mPresenter.getBannerInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOAD_TIME = 1;  //重置标记位
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

        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        if (mArticleAdapter == null) {
            if (articleDataList == null) {
                showShortToast("列表没有内容");
            } else {    //初始化adapter并进行RV的配置
                mArticleDataList = articleDataList;
                initAdapter();
                mArticleRv.setAdapter(mArticleAdapter);
                if (LOAD_TIME == 1) {
                    mArticleRv.addOnScrollListener(new LoadMoreScrollListener(mArticleAdapter));
                    //刷新后再加载不需要继续添加该监听器，不然会导致下拉加载出错
                    LOAD_TIME++;
                }
            }
        } else {
            if (articleDataList == null) {
                mArticleAdapter.setLastedStatus();
            } else {
                mArticleDataList = articleDataList;
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
        showShortToast("收藏成功");
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
        showShortToast("取消收藏");
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

    /**
     * 获取banner信息成功
     *
     * @param imageUrlList
     * @param titleList
     */
    @Override
    public void getBannerInfoSuccess(List<String> imageUrlList, List<String> titleList, List<String> urlList) {
        mImageUrlList = imageUrlList;
        mTitleList = titleList;
        mUrlList = urlList;
        mPresenter.getHomeArticle(currentPage++);
    }

    /**
     * 获取banner信息失败
     *
     * @param errorMsg
     */
    @Override
    public void getBannerInfoError(String errorMsg) {
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
                if (event.getData().isBackToTop()) {
                    //返回顶部
                    mArticleRv.smoothScrollToPosition(0);
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

            @Override
            public void clickBannerItem(int position) {
                //活动跳转，跳转到显示文章活动
                Event<ShowArticleEvent> event = new Event<>(EventBusCode.HomeBanner2ShowArticle, new ShowArticleEvent(
                        mUrlList.get(position), mTitleList.get(position)));
                EventBusUtil.sendStickyEvent(event);
                jump2Activity(ShowArticleActivity.class);
            }
        });

        mArticleAdapter.setBannerInfoListener(new ArticleAdapter.BannerInfoListener() {
            @Override
            public List<String> getImageUrlList() {
                return mImageUrlList;
            }

            @Override
            public List<String> getTitleList() {
                return mTitleList;
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
