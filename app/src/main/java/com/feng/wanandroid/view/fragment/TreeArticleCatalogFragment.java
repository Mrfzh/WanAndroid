package com.feng.wanandroid.view.fragment;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.contract.ITreeArticleCatalogContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.presenter.TreeArticleCatalogPresenter;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogFragment extends BaseFragment<TreeArticleCatalogPresenter>
        implements ITreeArticleCatalogContract.View {

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tree_article_catalog;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected TreeArticleCatalogPresenter getPresenter() {
        return new TreeArticleCatalogPresenter();
    }

    @Override
    public void getArticleInfoSuccess(List<ArticleData> articleDataList) {

    }

    @Override
    public void getArticleInfoError(String errorMsg) {

    }
}
