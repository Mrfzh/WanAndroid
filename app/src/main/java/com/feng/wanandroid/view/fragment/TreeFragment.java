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
import com.feng.wanandroid.contract.ITreeContract;
import com.feng.wanandroid.entity.data.TreeData;
import com.feng.wanandroid.presenter.TreePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreeFragment extends BaseFragment<TreePresenter> implements ITreeContract.View {

    private static final int DELAY_TIME = 500;

    @BindView(R.id.rv_tree_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.srv_tree_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.pb_tree)
    ProgressBar mProgressBar;

    private TreeAdapter mTreeAdapter = null;
    private List<TreeData> mTreeDataList = new ArrayList<>();

    @Override
    protected void doInOnCreate() {
        mPresenter.getTree();
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
        mProgressBar.setVisibility(View.GONE);
        mRefresh.setRefreshing(false);

        mTreeDataList = treeDataList;
        mTreeAdapter = null;
        //初始化adapter
        mTreeAdapter = new TreeAdapter(getContext(), treeDataList);
        mTreeAdapter.setOnClickListener(new TreeAdapter.OnClickListener() {
            @Override
            public void clickItem(String name, List<String> childNames, List<Integer> ids) {
                
            }
        });
        //配置RV
        mRecyclerView.setAdapter(mTreeAdapter);
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
    }

    /**
     * 刷新操作
     */
    private void refresh() {
        mPresenter.getTree();
    }
}
