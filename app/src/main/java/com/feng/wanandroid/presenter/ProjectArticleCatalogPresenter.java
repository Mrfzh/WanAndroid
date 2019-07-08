package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IProjectArticleCatalogContract;
import com.feng.wanandroid.entity.data.ProjectArticleData;
import com.feng.wanandroid.model.ProjectArticleCatalogModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public class ProjectArticleCatalogPresenter extends BasePresenter<IProjectArticleCatalogContract.View>
        implements IProjectArticleCatalogContract.Presenter {

    private IProjectArticleCatalogContract.Model mModel;

    public ProjectArticleCatalogPresenter() {
        mModel = new ProjectArticleCatalogModel(this);
    }

    @Override
    public void getProjectArticleDataSuccess(List<ProjectArticleData> projectArticleDataList) {
        if (isAttachView()) {
            getMvpView().getProjectArticleDataSuccess(projectArticleDataList);
        }
    }

    @Override
    public void getProjectArticleDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getProjectArticleDataError(errorMsg);
        }
    }

    @Override
    public void getProjectArticleData(int page, int cid) {
        mModel.getProjectArticleData(page, cid);
    }
}
