package com.feng.wanandroid.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.feng.wanandroid.R;
import com.feng.wanandroid.adapter.TreeArticleAdapter;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.base.BasePagingLoadAdapter;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.ITreeArticleCatalogContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.ShowArticleEvent;
import com.feng.wanandroid.presenter.TreeArticleCatalogPresenter;
import com.feng.wanandroid.utils.EventBusUtil;
import com.feng.wanandroid.view.activity.ShowArticleActivity;
import com.feng.wanandroid.widget.custom.LoadMoreScrollListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogFragment extends BaseFragment<TreeArticleCatalogPresenter>
        implements ITreeArticleCatalogContract.View, BasePagingLoadAdapter.LoadMoreListener {

    public static boolean IS_GET_INFO = false;  //标记位：各fragment是否都获取到信息
    private static final String CID = "secondLevelCatalogId";
//    private static final String TAG = "fzh";
    private int currentPage = 0;   //当前加载的页码
    private int LOAD_TIME = 1;    //加载次数（第二次加载时不需要再addOnScrollListener，不然添加了多个监听器后会导致下拉加载出错）

    @BindView(R.id.rv_tree_article_catalog_list)
    RecyclerView mArticleRv;

    private int mSecondLevelCatalogId;  //二级目录id，用于获取二级目录文章
    private List<ArticleData> mArticleDataList; //文章信息集合
    private TreeArticleAdapter mTreeArticleAdapter;

    /**
     * 返回碎片实例
     *
     * @param secondLevelCatalogId 二级目录id
     * @return
     */
    public static TreeArticleCatalogFragment newInstance(int secondLevelCatalogId) {
        TreeArticleCatalogFragment fragment = new TreeArticleCatalogFragment();
        //动态加载fragment，接受activity传入的值
        Bundle bundle = new Bundle();
        bundle.putInt(CID, secondLevelCatalogId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void doInOnCreate() {
        mSecondLevelCatalogId = Objects.requireNonNull(getArguments()).getInt(CID, -1);

        mPresenter.getArticleInfo(currentPage++, mSecondLevelCatalogId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        IS_GET_INFO = false;    //重置标记位
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tree_article_catalog;
    }

    @Override
    protected void initView() {
        mArticleRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected TreeArticleCatalogPresenter getPresenter() {
        return new TreeArticleCatalogPresenter();
    }

    @Override
    public void getArticleInfoSuccess(List<ArticleData> articleDataList) {
        IS_GET_INFO = true;

        if (mTreeArticleAdapter == null) {
            if (articleDataList == null) {
                showShortToast("列表没有内容");
            } else {    //初始化adapter并进行RV的配置
                mArticleDataList = articleDataList;
                initAdapter();
                mArticleRv.setAdapter(mTreeArticleAdapter);
                if (LOAD_TIME == 1) {
                    mArticleRv.addOnScrollListener(new LoadMoreScrollListener(mTreeArticleAdapter));
                    //刷新后再加载不需要继续添加该监听器，不然会导致下拉加载出错
                    LOAD_TIME++;
                }
            }
        } else {
            if (articleDataList == null) {
                mTreeArticleAdapter.setLastedStatus();
            } else {
                mArticleDataList = articleDataList;
                mTreeArticleAdapter.updateList();
            }
        }

    }

    @Override
    public void getArticleInfoError(String errorMsg) {
        IS_GET_INFO = false;

        if (mTreeArticleAdapter == null) {
            showShortToast(errorMsg);
        } else {
            mTreeArticleAdapter.setErrorStatus();
        }
    }

    /**
     * 加载更多操作
     */
    @Override
    public void loadMore() {
        mPresenter.getArticleInfo(currentPage++, mSecondLevelCatalogId);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mTreeArticleAdapter = new TreeArticleAdapter(getContext(), mArticleDataList, this);
        mTreeArticleAdapter.setClickListener(new TreeArticleAdapter.OnClickListener() {
            @Override
            public void clickCollect(boolean collect, int id, int position) {

            }

            @Override
            public void clickItem(String link, String title, boolean isCollect, int id, int position) {
                Event<ShowArticleEvent> event = new Event<>(EventBusCode.TreeArticle2ShowArticle, new ShowArticleEvent(link,
                        title, isCollect, id, position, false));
                EventBusUtil.sendStickyEvent(event);
                jump2Activity(ShowArticleActivity.class);
            }
        });
    }
}
