package com.feng.wanandroid.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IMainContract;
import com.feng.wanandroid.presenter.MainPresenter;
import com.feng.wanandroid.utils.Preferences;
import com.feng.wanandroid.view.fragment.TestFragment;
import com.feng.wanandroid.widget.LogoutDialog;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements View.OnClickListener, IMainContract.View {

    private static final String TAG = "fzh";
    public static final String UPDATE_ACTION = "com.feng.main.update";

    @BindView(R.id.dv_main_draw_layout)
    DrawerLayout mMainDrawLayout;
    @BindView(R.id.base_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.nb_main_bottom_navigation_bar)
    BottomNavigationBar mMainBottomNavigationBar;
    @BindView(R.id.nv_main_navigation_view)
    NavigationView mMainNavigationView;

    ImageView mHeadImage;
    TextView mUserName;

    @BindColor(R.color.color_tab_one)
    int mTabOneColor;
    @BindColor(R.color.color_tab_two)
    int mTabTwoColor;
    @BindColor(R.color.color_tab_three)
    int mTabThreeColor;
    @BindColor(R.color.color_tab_four)
    int mTabFourColor;
    @BindView(R.id.pb_main)
    ProgressBar mProgressBar;

    private boolean mIsLogin = false;
    private ArrayList<BaseFragment> mFragments;     //fragment集合
    private int mLastFgIndex;                       //记录上一个Fragment的索引
    private UpdateReceiver mUpdateReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initView() {
        initBottomBar();
        initNavigationView();
    }


    @Override
    protected Toolbar getToolbar() {
        return mMainToolbar;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle(Constant.TAB_TEXT[0]);   //设置标题
        mMainToolbar.setNavigationIcon(R.mipmap.main_menu);    //设置左上角图标
        //设置NavigationIcon的点击事件（注意：需在setSupportActionBar()后面）
        mMainToolbar.setNavigationOnClickListener(v -> {
            mMainDrawLayout.openDrawer(Gravity.START);  //打开侧滑菜单
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mUpdateReceiver);
        Preferences.clearSharedPreferences(this, Constant.COOKIES_SHARE_PRE);   //退出清除cookies
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomBar() {
        //初始化底部导航栏
        mMainBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.tab_one_icon, Constant.TAB_TEXT[0]).setActiveColor(mTabOneColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_two_icon, Constant.TAB_TEXT[1]).setActiveColor(mTabTwoColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_three_icon, Constant.TAB_TEXT[2]).setActiveColor(mTabThreeColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_four_icon, Constant.TAB_TEXT[3]).setActiveColor(mTabFourColor))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)    //显示文字
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)    //设置背景
                .initialise();
        //设置tab点击监听
        mMainBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                setToolbarTitle(Constant.TAB_TEXT[position]);
                setStateBarColor(Constant.STATE_BAR_COLOR[position]);
                setToolbarColor(mMainToolbar, Constant.TAB_COLOR[position]);
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 初始化侧边菜单栏
     */
    private void initNavigationView() {
        mMainNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_main_navigation_collection:
                    if (mIsLogin)
                        jumpToNewActivity(CollectionActivity.class);
                    else
                        showShortToast("请先登录");
                    break;
                case R.id.menu_main_navigation_logout:
                    LogoutDialog logoutDialog = new LogoutDialog(this);
                    logoutDialog.show();
//                    mMainDrawLayout.closeDrawers();
//                    mPresenter.logout();
//                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            return true;
        });

        View headerView = mMainNavigationView.getHeaderView(0); //获取header布局
        mHeadImage = headerView.findViewById(R.id.iv_nav_main_header_head_image);
        mHeadImage.setOnClickListener(this);
        mUserName = headerView.findViewById(R.id.tv_nav_main_header_user_name);
        mUserName.setOnClickListener(this);
        if (mIsLogin) {
            mHeadImage.setImageResource(R.drawable.head_image);
        } else {
            mHeadImage.setImageResource(R.drawable.head_image_unlogin);
            mUserName.setText("登录");
        }
    }

    @Override
    protected void initData() {
        initReceiver();
        initFragment();
        switchFragment(0);
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mUpdateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_ACTION);
        mLocalBroadcastManager.registerReceiver(mUpdateReceiver, intentFilter);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(TestFragment.newInstance("Fragment " + i));
        }
    }

    private void switchFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        mLastFgIndex = position;
        ft.hide(lastFg);
        if (!targetFg.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(targetFg).commitAllowingStateLoss();
            ft.add(R.id.fv_main_container, targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_main_header_head_image:
            case R.id.tv_nav_main_header_user_name:
                if (!mIsLogin) {
                    //如果没有登录，就跳转到登录活动
                    jumpToNewActivity(LoginActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void logoutSuccess() {
        mProgressBar.setVisibility(View.GONE);
        showShortToast("已退出登录");
        mIsLogin = false;
        Preferences.clearSharedPreferences(this, Constant.COOKIES_SHARE_PRE);   //清除cookies
        mHeadImage.setImageResource(R.drawable.head_image_unlogin);
        mUserName.setText("登录");
    }

    @Override
    public void logoutError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(errorMsg);
    }

    class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mHeadImage.setImageResource(R.drawable.head_image);
            mUserName.setText(intent.getStringExtra(LoginActivity.UPDATE_TAG));
            mIsLogin = true;
        }
    }
}
