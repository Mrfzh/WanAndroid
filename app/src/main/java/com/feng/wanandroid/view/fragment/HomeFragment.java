package com.feng.wanandroid.view.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.feng.wanandroid.utils.rv.WrapRecyclerView;
import com.feng.wanandroid.view.activity.ShowArticleActivity;
import com.feng.wanandroid.widget.LoadMoreScrollListener;
import com.feng.wanandroid.widget.MyImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

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

//    private Banner mBanner;
//    private View mBannerView;

    private ArticleAdapter mArticleAdapter;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

    @Override
    protected void doInOnCreate() {
        mPresenter.getHomeArticle(currentPage++);
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

//
//        List<String> imageList = new ArrayList<>();
//        imageList.add("http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png");
//        imageList.add("http://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png");
//        imageList.add("http://www.wanandroid.com/blogimgs/fb0ea461-e00a-482b-814f-4faca5761427.png");
//        imageList.add("http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png");
//        imageList.add("http://www.wanandroid.com/blogimgs/00f83f1d-3c50-439f-b705-54a49fc3d90d.jpg");
//        imageList.add("http://www.wanandroid.com/blogimgs/90cf8c40-9489-4f9d-8936-02c9ebae31f0.png");
//        imageList.add("http://www.wanandroid.com/blogimgs/acc23063-1884-4925-bdf8-0b0364a7243e.png");
//        List<String> titleList = new ArrayList<>();
//        titleList.add("一起来做个App吧");
//        titleList.add("看看别人的面经，搞定面试~");
//        titleList.add("兄弟，要不要挑个项目学习下?");
//        titleList.add("我们新增了一个常用导航Tab~");
//        titleList.add("公众号文章列表强势上线");
//        titleList.add("JSON工具");
//        titleList.add("微信文章合集");

//        mBannerView = LayoutInflater.from(getContext()).inflate(R.layout.header_home_banner, null);
//        mBanner = mBannerView.findViewById(R.id.bn_home_banner);
//
//        mBanner.setImageLoader(new MyImageLoader())  //设置图片加载器
//                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) //指定样式
//                .setImages(imageList)   //设置图片url集合
//                .setBannerTitles(titleList)     //设置title集合
//                .setDelayTime(3000)     //设置轮播时间
//                .start();   //最后才start
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
