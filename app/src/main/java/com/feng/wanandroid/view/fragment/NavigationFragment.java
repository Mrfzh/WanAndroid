package com.feng.wanandroid.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.NaviChapterDataAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.contract.INavigationContract;
import com.feng.wanandroid.entity.data.NavigationData;
import com.feng.wanandroid.presenter.NavigationPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class NavigationFragment extends BaseFragment<NavigationPresenter> implements INavigationContract.View {

    @BindView(R.id.vtv_navigation_chapter_vertical_tab)
    VerticalTabLayout mChapterVerticalTabVtv;
    @BindView(R.id.rv_navigation_chapter_data)
    RecyclerView mChapterDataRv;

    private List<String> mChapterNames = new ArrayList<>();
    private List<NavigationData.ChapterData> mChapterData = new ArrayList<>();

    @Override
    protected void doInOnCreate() {
        mPresenter.getNavigationData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void initView() {

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
        mChapterNames = navigationData.getChapterNames();
        mChapterData = navigationData.getChapterData();

        initChapterTabLayout();
        initChapterDataList();
    }

    /**
     * 获取导航数据失败
     *
     * @param errorMsg
     */
    @Override
    public void getNavigationDataError(String errorMsg) {
        showShortToast(errorMsg);
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
        mChapterDataRv.setLayoutManager(new LinearLayoutManager(getContext()));
        NaviChapterDataAdapter adapter = new NaviChapterDataAdapter(getContext(), mChapterNames, mChapterData);
        mChapterDataRv.setAdapter(adapter);
    }

}
