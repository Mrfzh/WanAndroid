package com.feng.wanandroid.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.TreeDetailedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeDetailedActivity extends BaseActivity {

    private String mFirstLevelCatalogName;
    private List<String> mSecondLevelCatalogNames;
    private List<Integer> mSecondLevelCatalogIds;

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_tree_detailed_second_level_directory)
    TabLayout mSecondLevelDirectoryTabLayout;
    @BindView(R.id.vp_tree_detailed_article_catalog)
    ViewPager mArticleCatalogViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tree_detailed;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {

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
    protected void initView() {

    }

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle(mFirstLevelCatalogName);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventCome(Event<TreeDetailedEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.Tree2TreeDetailed:
                mFirstLevelCatalogName = event.getData().getFirstLevelCatalogName();
                mSecondLevelCatalogNames = event.getData().getSecondLevelCatalogNames();
                mSecondLevelCatalogIds = event.getData().getSecondLevelCatalogIds();
                break;
            default:
                break;
        }
    }
}
