package com.feng.wanandroid.view.fragment;

import android.os.Bundle;
import android.util.Log;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.contract.IProjectArticleCatalogContract;
import com.feng.wanandroid.entity.data.ProjectArticleData;
import com.feng.wanandroid.presenter.ProjectArticleCatalogPresenter;

import java.util.List;
import java.util.Objects;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public class ProjectArticleCatalogFragment extends BaseFragment<ProjectArticleCatalogPresenter>
        implements IProjectArticleCatalogContract.View {

    private static final String CID = "cid";
    private static final String TAG = "fzh";
    public static boolean IS_GET_INFO = false;  //标记位：各fragment是否都获取到信息

    private int currentPage = 0;   //当前加载的页码
    private int LOAD_TIME = 1;    //加载次数（第二次加载时不需要再addOnScrollListener，不然添加了多个监听器后会导致下拉加载出错）

    private int mCid;  //当前分类cid
    private List<ProjectArticleData> mProjectArticleDataList; //文章信息集合


    @Override
    protected void doInOnCreate() {
        mCid = Objects.requireNonNull(getArguments()).getInt(CID, -1);

        //请求文章数据
        mPresenter.getProjectArticleData(currentPage++, mCid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        IS_GET_INFO = false;    //重置标记位
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project_article_catalog;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected ProjectArticleCatalogPresenter getPresenter() {
        return new ProjectArticleCatalogPresenter();
    }

    public static ProjectArticleCatalogFragment newInstance(int cid) {
        ProjectArticleCatalogFragment fragment = new ProjectArticleCatalogFragment();
        //动态加载fragment
        Bundle bundle = new Bundle();
        bundle.putInt(CID, cid);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void getProjectArticleDataSuccess(List<ProjectArticleData> projectArticleDataList) {

    }

    @Override
    public void getProjectArticleDataError(String errorMsg) {

    }
}
