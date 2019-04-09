package com.feng.wanandroid.view.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.TreeAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.cache.ACache;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.ITreeContract;
import com.feng.wanandroid.entity.data.TreeData;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.TreeDetailedEvent;
import com.feng.wanandroid.presenter.TreePresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.view.activity.TreeDetailedActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreeFragment extends BaseFragment<TreePresenter> implements ITreeContract.View {

    private static final int DELAY_TIME = 500;
    private static final String KEY_SAVE_TREE_DATA = "saveTreeDataList";

    @BindView(R.id.rv_tree_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.srv_tree_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.pb_tree)
    ProgressBar mProgressBar;

    private TreeAdapter mTreeAdapter = null;
    private List<TreeData> mTreeDataList = new ArrayList<>();

    private ACache mCache;

    @Override
    protected void doInOnCreate() {
        mPresenter.getTree();

        mCache = ACache.get(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tree;
    }

    @Override
    protected void initView() {
        mRefresh.setColorSchemeColors(getResources().getColor(R.color.color_tab_one));
        mRefresh.setOnRefreshListener(() -> new Handler().postDelayed(this::refresh, DELAY_TIME));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected TreePresenter getPresenter() {
        return new TreePresenter();
    }

    /**
     * 获取体系数据成功
     *
     * @param treeDataList
     */
    @Override
    public void getTreeSuccess(List<TreeData> treeDataList) {
        mTreeDataList = treeDataList;
        mProgressBar.setVisibility(View.GONE);
        mRefresh.setRefreshing(false);

        mTreeAdapter = null;
        //初始化adapter
        initAdapter();
        //配置RV
        mRecyclerView.setAdapter(mTreeAdapter);

        //缓存消息
        mCache.put(KEY_SAVE_TREE_DATA, (Serializable) treeDataList);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mTreeAdapter = new TreeAdapter(getContext(), mTreeDataList);
        mTreeAdapter.setOnClickListener(new TreeAdapter.OnClickListener() {
            @Override
            public void clickItem(String name, List<String> childNames, List<Integer> ids) {
                //跳转到具体的体系目录
                Event<TreeDetailedEvent> event = new Event<>(EventBusCode.Tree2TreeDetailed,
                        new TreeDetailedEvent(name , childNames, ids));
                EventBusUtil.sendStickyEvent(event);
                jump2Activity(TreeDetailedActivity.class);
            }
        });
    }

    /**
     * 获取体系数据失败
     *
     * @param errorMsg
     */
    @Override
    public void getTreeError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        mRefresh.setRefreshing(false);
        showShortToast(errorMsg);

        if (mTreeAdapter == null) {
            //读取缓存
            mTreeDataList = (List<TreeData>) mCache.getAsObject(KEY_SAVE_TREE_DATA);
            if (mTreeDataList != null) {
                //初始化adapter
                initAdapter();
            }
        }
        //配置RV
        mRecyclerView.setAdapter(mTreeAdapter);
    }

    /**
     * 注册EventBus
     */
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<HomeEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.Main2Tree:
                if (event.getData().isBackToTop()) {
                    mRecyclerView.smoothScrollToPosition(0);  //Rv返回顶部
                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新操作
     */
    private void refresh() {
        mPresenter.getTree();
    }
}
