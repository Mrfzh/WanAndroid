package com.feng.wanandroid.view.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.IMainContract;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.MainEvent;
import com.feng.wanandroid.presenter.MainPresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.utils.Preferences;
import com.feng.wanandroid.view.fragment.HomeFragment;
import com.feng.wanandroid.view.fragment.NavigationFragment;
import com.feng.wanandroid.view.fragment.ProjectFragment;
import com.feng.wanandroid.view.fragment.TreeFragment;
import com.feng.wanandroid.widget.dialog.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements View.OnClickListener, IMainContract.View {

    @BindView(R.id.dv_main_draw_layout)
    DrawerLayout mMainDrawLayout;
    @BindView(R.id.base_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.nb_main_bottom_navigation_bar)
    BottomNavigationBar mMainBottomNavigationBar;
    @BindView(R.id.nv_main_navigation_view)
    NavigationView mMainNavigationView;
    @BindView(R.id.pb_main)
    ProgressBar mProgressBar;
    @BindView(R.id.fab_main_floating_action_btn)
    FloatingActionButton mBackToTopFab;

    private ImageView mHeadImage;
    private TextView mUserName;

    @BindColor(R.color.color_tab_one)
    int mTabOneColor;
    @BindColor(R.color.color_tab_two)
    int mTabTwoColor;
    @BindColor(R.color.color_tab_three)
    int mTabThreeColor;
    @BindColor(R.color.color_tab_four)
    int mTabFourColor;

    private String mOldUserName;    //之前登录的用户名
    private Boolean mIsAutoLogin;   //是否自动登录
    private ArrayList<BaseFragment> mFragments;     //fragment集合
    private int mLastFgIndex;                       //记录上一个Fragment的索引
    private int mCurrentFgIndex = 0;            //当前Fragment的索引

    public static final String TAG = "fzh";

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
    protected void doInOnCreate() {

    }

    @Override
    protected Toolbar getToolbar() {
        return mMainToolbar;
    }

    @Override
    protected boolean setToolbarBackIcon() {
        return false;
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
        Preferences.clearSharedPreferences(this, Constant.COOKIES_SHARE_PRE);   //退出清除cookies
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomBar() {
        //初始化底部导航栏
        mMainBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.tab_one_icon, Constant.TAB_TEXT[0]).setActiveColor(mTabOneColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_two_icon, Constant.TAB_TEXT[1]).setActiveColor(mTabOneColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_three_icon, Constant.TAB_TEXT[2]).setActiveColor(mTabOneColor))
                .addItem(new BottomNavigationItem(R.drawable.tab_four_icon, Constant.TAB_TEXT[3]).setActiveColor(mTabOneColor))
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
                mCurrentFgIndex = position;
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
        if (!getIsLogin()) {
            //未登录时隐藏退出登录和切换账号菜单项
            setLogoutItemVisible(false);
            setSwitchAccountItemVisible(false);
        }
        mMainNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_main_navigation_collection:
                    if (getIsLogin())
                        jumpToNewActivity(CollectionActivity.class);
                    else
                        showShortToast("请先登录");
                    break;
                //退出登录
                case R.id.menu_main_navigation_logout:
                    TipDialog tipDialog = new TipDialog.Builder(MainActivity.this)
                            .setContent("确定要退出登录？")
                            .setOnClickListener(new TipDialog.OnClickListener() {
                                @Override
                                public void clickEnsure() {
                                    mMainDrawLayout.closeDrawers();
                                    mPresenter.logout();
                                    mProgressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void clickCancel() {
                                }
                            })
                            .build();
                    tipDialog.show();
                    break;
                //切换账号
                case R.id.menu_main_navigation_switch_account:
                    jumpToNewActivity(LoginActivity.class);
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
        if (mIsAutoLogin) {
            mHeadImage.setImageResource(R.drawable.head_image);
            mUserName.setText(mOldUserName);
        } else {
            mHeadImage.setImageResource(R.drawable.head_image_unlogin);
            mUserName.setText("登录");
        }
    }

    @Override
    protected void initData() {
        mOldUserName = getIntent().getStringExtra(EnterActivity.INTENT_USER);
        mIsAutoLogin = !mOldUserName.equals("");
        initFragment();
        switchFragment(0);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new TreeFragment());
        mFragments.add(new NavigationFragment());
        mFragments.add(new ProjectFragment());
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
                if (!getIsLogin()) {
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
        setIsLogin(false);
        Preferences.clearSharedPreferences(this, Constant.COOKIES_SHARE_PRE);   //清除cookies
        setLogoutItemVisible(false);    //隐藏退出登录菜单项
        setSwitchAccountItemVisible(false);     //隐藏切换账号菜单项
        //重写设置信息
        mHeadImage.setImageResource(R.drawable.head_image_unlogin);
        mUserName.setText("登录");
        //更新首页文章
        Event<HomeEvent> homeEvent = new Event<>(EventBusCode.Main2Home, new HomeEvent(true));
        EventBusUtil.sendEvent(homeEvent);
    }

    @Override
    public void logoutError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(errorMsg);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<MainEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.Login2Main:
            case EventBusCode.Register2Main:
                updateAfterLoginOrRegister(event.getData().getUserName());  //更新用户信息
                break;
            default:
                break;
        }
    }

    /**
     * 设置退出登录菜单项是否可见（未登录时不可见）
     *
     * @param visible
     */
    private void setLogoutItemVisible(boolean visible) {
        mMainNavigationView.getMenu().getItem(1).setVisible(visible);
    }

    /**
     * 设置切换账号菜单项是否可见（未登录时不可见）
     *
     * @param visible
     */
    private void setSwitchAccountItemVisible(boolean visible) {
        mMainNavigationView.getMenu().getItem(2).setVisible(visible);
    }

    /**
     * 登陆或注册后主界面的更新
     *
     * @param userName 用户名
     */
    private void updateAfterLoginOrRegister(String userName) {
        mHeadImage.setImageResource(R.drawable.head_image);
        mUserName.setText(userName);
        setIsLogin(true);
        setLogoutItemVisible(true);     //显示退出登录菜单项
        setSwitchAccountItemVisible(true);     //显示切换账号菜单项
    }

    @OnClick(R.id.fab_main_floating_action_btn)
    public void onViewClicked() {
        backToTop();
    }

    /**
     * 返回顶部
     */
    private void backToTop() {
        Event<HomeEvent> homeEvent = null;
        //根据当前Fragment页面发送不同的事件
        switch (mCurrentFgIndex) {
            case 0:
                homeEvent = new Event<>(EventBusCode.Main2Home, new HomeEvent());
                break;
            case 1:
                homeEvent = new Event<>(EventBusCode.Main2Tree, new HomeEvent());
                break;
            default:
                break;
        }
        if (homeEvent != null) {
            EventBusUtil.sendEvent(homeEvent);
        }
    }

}
