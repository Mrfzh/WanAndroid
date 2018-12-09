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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.entity.LoginBean;
import com.feng.wanandroid.http.api.AccountService;
import com.feng.wanandroid.http.api.CollectionService;
import com.feng.wanandroid.presenter.LoginPresenter;
import com.feng.wanandroid.view.fragment.TestFragment;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "fzh";
    public static final String UPDATE_ACTION = "com.feng.main.update";

    @BindView(R.id.dv_main_draw_layout)
    DrawerLayout mMainDrawLayout;
    @BindView(R.id.tb_main_toolbar)
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

    private boolean mIsLogin = false;
    private ArrayList<BaseFragment> mFragments;     //fragment集合
    private int mLastFgIndex;                       //记录上一个Fragment的索引
    private UpdateReceiver mUpdateReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    HashSet<String> cookies;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
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
                    showShortToast("点击了我的收藏");
                    break;
                case R.id.menu_main_navigation_logout:
                    showShortToast("点击了退出登录");
                    break;
                default:
                    break;
            }
            mMainDrawLayout.closeDrawers();     //关闭侧边菜单

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

    class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mHeadImage.setImageResource(R.drawable.head_image);
            mUserName.setText(intent.getStringExtra(LoginActivity.UPDATE_TAG));
            mIsLogin = true;
        }
    }

    class SaveCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                    Log.d(TAG, "intercept: " + header);
                }
            }
            return originalResponse;
        }
    }

    class ReadCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
//            HashSet<String> preferences = (HashSet) Preferences.getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
            HashSet<String> preferences = cookies;
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
            }

            return chain.proceed(builder.build());
        }
    }

    private void loginTest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);   //连接超时时间（10秒）
        builder.writeTimeout(10, TimeUnit.SECONDS);     //写操作超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);      //读操作超时时间
        builder.addInterceptor(new SaveCookiesInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())    //加入okhttp配置
                .addConverterFactory(GsonConverterFactory.create())     //Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //配合RxJava2
                .baseUrl(Constant.BASE_URL)	//基本路径
                .build();

        AccountService accountService = retrofit.create(AccountService.class);

        accountService.login("feng1216", "ktpr8621030")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        showShortToast("登录成功");
                        Log.d(TAG, "user: " + loginBean.getData().getUsername()
                            + ", password: " + loginBean.getData().getPassword());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showShortToast("登录失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getCl() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);   //连接超时时间（10秒）
        builder.writeTimeout(10, TimeUnit.SECONDS);     //写操作超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);      //读操作超时时间
        builder.addInterceptor(new ReadCookiesInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())    //加入okhttp配置
                .addConverterFactory(GsonConverterFactory.create())     //Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //配合RxJava2
                .baseUrl(Constant.BASE_URL)	//基本路径
                .build();

        CollectionService collectionService = retrofit.create(CollectionService.class);

        collectionService.getCollectArticleList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectArticleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CollectArticleBean collectArticleBean) {
                        if (!collectArticleBean.getErrorMsg().equals("")) {
                            showShortToast(collectArticleBean.getErrorMsg());
                        } else {
                            showShortToast("获取收藏列表成功");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showShortToast("获取收藏列表失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
