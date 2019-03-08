package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ITreeArticleCatalogContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.model.TreeArticleCatalogModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogPresenter extends BasePresenter<ITreeArticleCatalogContract.View>
        implements ITreeArticleCatalogContract.Presenter {

    private ITreeArticleCatalogContract.Model mModel;

    public TreeArticleCatalogPresenter() {
        mModel = new TreeArticleCatalogModel(this);
    }

    @Override
    public void getArticleInfoSuccess(List<ArticleData> articleDataList) {
        if (isAttachView()) {
            getMvpView().getArticleInfoSuccess(articleDataList);
        }
    }

    @Override
    public void getArticleInfoError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getArticleInfoError(errorMsg);
        }
    }

    @Override
    public void getArticleInfo(int page, int catalogId) {
        mModel.getArticleInfo(page, catalogId);
    }
}
