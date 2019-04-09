package com.feng.wanandroid.view.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.CollectedArticleAdapter;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.eventbus.CollectionEvent;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.presenter.CollectionPresenter;
import com.feng.wanandroid.presenter.HomePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.widget.custom.LoadMoreScrollListener;
import com.feng.wanandroid.widget.dialog.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements ICollectionContract.View,
        BasePagingLoadAdapter.LoadMoreListener, IHomeContract.View {

    private static final int DELAY_TIME = 2 * 500;
    private static final String TITLE_EDIT = "编辑";
    private static final String TITLE_CAL = "取消";
    private static final String TAG = "fzh";
    private static int LOAD_TIME = 1;   //标记位

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_collection)
    ProgressBar mProgressBar;
    @BindView(R.id.rv_collection_recycler_view)
    RecyclerView mCollectionRv;
    @BindView(R.id.tv_collection_no_article)
    TextView mNoArticleTv;
    @BindView(R.id.srv_collection_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.cb_collection_choose_all)
    CheckBox mChooseAllCb;
    @BindView(R.id.tv_collection_multi_uncollect)
    TextView mMultiUnCollectTv;
    @BindView(R.id.rv_collection_bottom_bar)
    RelativeLayout mBottomBarRv;

    private CollectedArticleAdapter mAdapter = null;
    private int currentPage = 0;
    private List<ArticleData> mArticleDataList = new ArrayList<>();
    private HomePresenter mHomePresenter;

    private List<Boolean> mCheckList = new ArrayList<>();   //存储item是否被选中
    private List<Integer> mIdList = new ArrayList<>();   //存储选中的item的id
    private Menu mMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected CollectionPresenter getPresenter() {
        return new CollectionPresenter();
    }

    @Override
    protected void initView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getCollectList(currentPage++);

        mCollectionRv.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_tab_one));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(this::refresh, DELAY_TIME);
        });
    }

    @Override
    protected void doInOnCreate() {
        mHomePresenter = new HomePresenter();
        mHomePresenter.attachView(this);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("我的收藏");
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
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomePresenter != null) {
            mHomePresenter.detachView();
        }
        LOAD_TIME = 1;
    }

    /**
     * 获取收藏列表成功
     *
     * @param articleDataList
     */
    @Override
    public void getCollectListSuccess(List<ArticleData> articleDataList) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        //总的来说就是根据articleDataList和mAdapter是否为null分为4种情况
        if (articleDataList == null && mAdapter == null) {
            mNoArticleTv.setVisibility(View.VISIBLE);   //第一次载入没有数据时提示‘无收藏文章’
        } else {
            if (articleDataList != null && mAdapter == null) {
                mNoArticleTv.setVisibility(View.GONE);  //这时才隐藏‘无文章提示’
                mArticleDataList = articleDataList;     //在articleDataList !=  null时才设置mList
                //初始化adapter
                initAdapter();
                if (LOAD_TIME == 1) {
                    mCollectionRv.addOnScrollListener(new LoadMoreScrollListener(mAdapter));
                    LOAD_TIME++;
                }
                mCollectionRv.setAdapter(mAdapter);
            } else {
                if (articleDataList == null) {
                    mAdapter.setLastedStatus(); //显示没有更多
                } else {
                    mArticleDataList = articleDataList;  //同上，在返回的list不是null时才设置mList，
                    // 保证mList不会在有item的时候被置为空，不然取消收藏会失败
                    mAdapter.updateList();  //更新列表
                }
            }
        }

        //更新checklist
        mCheckList.clear(); //这里要使用clear而不是新new一个，不然会导致adapter和该活动持有的checklist不一样
        for (int i = 0; i < mArticleDataList.size(); i++) {
            mCheckList.add(false);
        }

        //更新全选CheckBox
        if (mIdList.size() != mArticleDataList.size() && mChooseAllCb.isChecked()) {
            mChooseAllCb.setChecked(false);     //取消全选
        }
        if (mIdList.size() == mArticleDataList.size() && !mChooseAllCb.isChecked()) {
            mChooseAllCb.setChecked(true);     //设置全选
        }

    }

    /**
     * 获取收藏列表失败
     *
     * @param errorMsg
     */
    @Override
    public void getCollectListError(String errorMsg) {
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mAdapter.setErrorStatus();
        }
    }

    /**
     * 多选取消收藏成功
     *
     * @param removeIndexList
     */
    @Override
    public void multiUnCollectSuccess(List<Integer> removeIndexList) {
        refresh();  //重新刷新界面
        //更新首页文章
        Event<HomeEvent> event = new Event<>(EventBusCode.Collection2Home, new HomeEvent(true));
        EventBusUtil.sendEvent(event);

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 多选取消收藏失败
     *
     * @param errorMsg
     */
    @Override
    public void multiUnCollectError(String errorMsg) {
        showShortToast(errorMsg);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 取消收藏成功
     *
     * @param position
     */
    @Override
    public void unCollectSuccess(int position) {
        if (mArticleDataList != null) {
            //更新收藏列表
            mArticleDataList.remove(position);
            mAdapter.notifyItemRemoved(position);
            if (position != mArticleDataList.size()) {
                mAdapter.notifyItemRangeChanged(position, mArticleDataList.size() - position);
            }
            //当没有收藏文章时提示文字
            if (mArticleDataList.size() == 0) {
                mNoArticleTv.setVisibility(View.VISIBLE);
            }
        }
        //更新首页文章
        Event<HomeEvent> event = new Event<>(EventBusCode.Collection2Home, new HomeEvent(true));
        EventBusUtil.sendEvent(event);
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
    public void getBannerInfoSuccess(List<String> imageUrlList, List<String> titleList, List<String> urlList) {

    }

    @Override
    public void getBannerInfoError(String errorMsg) {

    }

    @Override
    public void loadMore() {
        mPresenter.getCollectList(currentPage++);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusEvent(Event<CollectionEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.ShowArticle2Collection:
                if (event.getData().isRefresh()) {
                    refresh();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getHomeArticleSuccess(List<ArticleData> articleDataList) {

    }

    @Override
    public void getHomeArticleError(String errorMsg) {

    }

    @Override
    public void collectSuccess(int position) {
    }

    @Override
    public void collectError(String errorMsg) {

    }


    /**
     * 更新adapter
     */
    private void refresh() {
        //更新列表（重新获取数据）
        currentPage = 0;
        mAdapter = null;     //重置adapter，等于重头再来
        mPresenter.getCollectList(currentPage++);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mCheckList.size() != 0) {
            mCheckList.clear();
        }
        if (mIdList.size() != 0) {
            mIdList.clear();
        }
        mAdapter = new CollectedArticleAdapter(this, mArticleDataList, this, mCheckList, mIdList);

        mAdapter.setClickListener(new CollectedArticleAdapter.OnClickListener() {
            @Override
            public void clickCollect(boolean collect, int id, int position) {
                if (collect) {
                    TipDialog tipDialog = new TipDialog.Builder(CollectionActivity.this)
                            .setContent("确定要取消收藏？")
                            .setOnClickListener(new TipDialog.OnClickListener() {
                                @Override
                                public void clickEnsure() {
                                    mHomePresenter.unCollect(id, position);
                                }

                                @Override
                                public void clickCancel() {

                                }
                            })
                            .build();
                    tipDialog.show();
                }
            }

            @Override
            public void clickItem(String link, String title, boolean isCollect, int id, int position) {
                Event<ShowArticleEvent> event = new Event<>(EventBusCode.Collection2ShowArticle, new ShowArticleEvent(link,
                        title, isCollect, id, position, true));
                EventBusUtil.sendStickyEvent(event);
                jumpToNewActivity(ShowArticleActivity.class);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void selectItemChanged(int num) {
                if (num == 0) {
                    mMultiUnCollectTv.setSelected(false);
                    mMultiUnCollectTv.setText("取消收藏");
                } else {
                    mMultiUnCollectTv.setSelected(true);
                    mMultiUnCollectTv.setText("取消收藏(" + num + ")");
                    if (num != mArticleDataList.size() && mChooseAllCb.isChecked()) {
                        mChooseAllCb.setChecked(false);     //取消全选
                    }
                    if (num == mArticleDataList.size() && !mChooseAllCb.isChecked()) {
                        mChooseAllCb.setChecked(true);     //设置全选
                    }
                }
            }

            @Override
            public void longClickItem() {
                edit(); //长按时进入编辑状态
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_collection_edit:
                if (mAdapter != null) {
                    if (mMenu.getItem(0).getTitle().toString().equals(TITLE_EDIT)) {
                        edit(); //编辑操作
                    } else {
                        mMenu.getItem(0).setTitle(TITLE_EDIT);
                        //取消操作
                        mAdapter.setIsShowCheckBox(false);
                        mAdapter.reset();
                        mAdapter.notifyDataSetChanged();

                        mBottomBarRv.setVisibility(View.GONE);
                        mChooseAllCb.setChecked(false);

                        mSwipeRefreshLayout.setEnabled(true);

                        mMultiUnCollectTv.setSelected(false);
                        mMultiUnCollectTv.setText("取消收藏");
                    }
                } else {
                    showShortToast("请先添加收藏");
                }
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.cb_collection_choose_all, R.id.tv_collection_multi_uncollect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_collection_choose_all:
                if (mChooseAllCb.isChecked()) {
                    mAdapter.selectAll();
                } else {
                    mAdapter.selectNull();
                }
                break;
            case R.id.tv_collection_multi_uncollect:
                if (mMultiUnCollectTv.isSelected()) {
                    TipDialog tipDialog = new TipDialog.Builder(CollectionActivity.this)
                            .setContent("确定要取消这些收藏？")
                            .setOnClickListener(new TipDialog.OnClickListener() {
                                @Override
                                public void clickEnsure() {
                                    //进行删除操作
                                    List<Integer> removeList = new ArrayList<>();
                                    for(int i = 0; i < mCheckList.size(); i++) {
                                        if (mCheckList.get(i)) {
                                            removeList.add(i);
                                        }
                                    }
                                    mPresenter.multiUnCollect(removeList, mIdList);
                                    //点击删除后其他控件相应的操作
                                    mMultiUnCollectTv.setSelected(false);
                                    mMultiUnCollectTv.setText("取消收藏");
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    mBottomBarRv.setVisibility(View.GONE);
                                    mMenu.getItem(0).setTitle(TITLE_EDIT);
                                    mSwipeRefreshLayout.setEnabled(true);
                                }

                                @Override
                                public void clickCancel() {

                                }
                            })
                            .build();
                    tipDialog.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 编辑操作
     */
    private void edit() {
        mMenu.getItem(0).setTitle(TITLE_CAL);

        mAdapter.setIsShowCheckBox(true);
        mAdapter.notifyDataSetChanged();

        mBottomBarRv.setVisibility(View.VISIBLE);   //显示底部栏

        mSwipeRefreshLayout.setEnabled(false);  //设置为不能刷新
    }


}
