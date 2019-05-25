package com.feng.wanandroid.view.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.NaviChapterDataAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.cache.ACache;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.INavigationContract;
import com.feng.wanandroid.entity.data.NavigationData;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.NavigationEvent;
import com.feng.wanandroid.presenter.NavigationPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class NavigationFragment extends BaseFragment<NavigationPresenter> implements INavigationContract.View {

    private static final int REFRESH_TIME = 1000;
    private static final String KEY_SAVE_NAVI = "KEY_SAVE_NAVI";
    private static final String TAG = "fzh";

    @BindView(R.id.vtv_navigation_chapter_vertical_tab)
    VerticalTabLayout mChapterVerticalTabVtv;
    @BindView(R.id.rv_navigation_chapter_data)
    RecyclerView mChapterDataRv;
    @BindView(R.id.srv_navigation_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pb_navigation)
    ProgressBar mProgressBar;

    private List<String> mChapterNames = new ArrayList<>();
    private List<NavigationData.ChapterData> mChapterData = new ArrayList<>();

    private LinearLayoutManager mLinearLayoutManager;
    private ACache mCache;

    @Override
    protected void doInOnCreate() {
        mPresenter.getNavigationData();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mCache = ACache.get(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_tab_one));   //设置颜色
        mSwipeRefreshLayout.setOnRefreshListener(() -> {    //监听下滑刷新
            //刷新时的操作
            new Handler().postDelayed(this::refresh, REFRESH_TIME);
        });
    }

    @Override
    protected NavigationPresenter getPresenter() {
        return new NavigationPresenter();
    }

    /**
     * 获取导航数据成功
     *
     * @param navigationData
     */
    @Override
    public void getNavigationDataSuccess(NavigationData navigationData) {
        mCache.put(KEY_SAVE_NAVI, navigationData);  //缓存数据

        mChapterNames = navigationData.getChapterNames();
        mChapterData = navigationData.getChapterData();

        initChapterTabLayout();
        initChapterDataList();

        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 获取导航数据失败
     *
     * @param errorMsg
     */
    @Override
    public void getNavigationDataError(String errorMsg) {
        //读取缓存
        NavigationData navigationData = (NavigationData) mCache.getAsObject(KEY_SAVE_NAVI);
        //利用缓存数据
        if (navigationData != null) {
            mChapterNames = navigationData.getChapterNames();
            mChapterData = navigationData.getChapterData();
            initChapterTabLayout();
            initChapterDataList();
        }

        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

        showShortToast(errorMsg);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<NavigationEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.Main2Navigation:
                backToTop();
                break;
            default:
                break;
        }
    }

    /**
     * 文章和导航都返回顶部
     */
    private void backToTop() {
        mChapterDataRv.smoothScrollToPosition(0);
    }

    /**
     * 初始化左边导航栏
     */
    private void initChapterTabLayout() {
        mChapterVerticalTabVtv.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mChapterNames.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return new TabView.TabTitle.Builder()
                        .setContent(mChapterNames.get(position))
                        .setTextColor(Color.WHITE, 0xBBFFFFFF)
                        .build();
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });
        //点击事件回调
        mChapterVerticalTabVtv.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                mChapterDataRv.smoothScrollToPosition(position);    //右侧的导航数据列表移动到相应位置
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    /**
     * 初始化右边导航数据列表
     */
    private void initChapterDataList() {
        mChapterDataRv.setLayoutManager(mLinearLayoutManager);
        NaviChapterDataAdapter adapter = new NaviChapterDataAdapter(getContext(), mChapterNames, mChapterData);
        mChapterDataRv.setAdapter(adapter);
        mChapterDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:    //静止时
                        //右边列表停止滑动后，设置左边导航栏的位置
                        //寻找第一个完全可见的item
                        int position = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (position == -1) {   //没有找到完整的item时
                            //寻找第一个可见的item
                            position = mLinearLayoutManager.findFirstVisibleItemPosition();
                            if (position != -1) {   //找到第一个可见的item
                                mChapterVerticalTabVtv.setTabSelected(position);
                            }
                        } else {    //找到完整的item
                            mChapterVerticalTabVtv.setTabSelected(position);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 刷新操作
     */
    private void refresh() {
        mPresenter.getNavigationData();
    }

}
