package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IProjectArticleCatalogContract;
import com.feng.wanandroid.entity.bean.ProjectArticleCatalogBean;
import com.feng.wanandroid.entity.data.ProjectArticleData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.ProjectService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public class ProjectArticleCatalogModel extends BaseModel
        implements IProjectArticleCatalogContract.Model {

    private IProjectArticleCatalogContract.Presenter mPresenter;
    private ProjectService mProjectService;

    public ProjectArticleCatalogModel(IProjectArticleCatalogContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mProjectService = RetrofitHelper.getInstance().create(ProjectService.class);
    }

    @Override
    public void getProjectArticleData(int page, int cid) {
        execute(mProjectService.getProjectArticleCatalog(page, cid),
                new Observer<ProjectArticleCatalogBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(ProjectArticleCatalogBean projectArticleCatalogBean) {
                List<ProjectArticleData> projectArticleDataList = new ArrayList<>();
                List<ProjectArticleCatalogBean.DataBean.DatasBean> datasBeans =
                        projectArticleCatalogBean.getData().getDatas();
                for (int i = 0; i < datasBeans.size(); i++) {
                    ProjectArticleCatalogBean.DataBean.DatasBean datasBean = datasBeans.get(i);
                    projectArticleDataList.add(new ProjectArticleData(datasBean.getTitle(),
                            datasBean.getDesc(), datasBean.getAuthor(), datasBean.getNiceDate(),
                            datasBean.getLink(), datasBean.getEnvelopePic()));
                }
                mPresenter.getProjectArticleDataSuccess(projectArticleDataList);
            }

            @Override
            public void onError(Throwable throwable) {
                mPresenter.getProjectArticleDataError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
