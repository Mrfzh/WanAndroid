package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IProjectContract;
import com.feng.wanandroid.entity.data.ProjectTreeData;
import com.feng.wanandroid.model.ProjectModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/6/4
 */
public class ProjectPresenter extends BasePresenter<IProjectContract.View>
        implements IProjectContract.Presenter{

    private IProjectContract.Model mModel;

    public ProjectPresenter() {
        mModel = new ProjectModel(this);
    }

    @Override
    public void getProjectTreeDataSuccess(ProjectTreeData projectTreeData) {
        if (isAttachView()) {
            getMvpView().getProjectTreeDataSuccess(projectTreeData);
        }
    }

    @Override
    public void getProjectTreeDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getProjectTreeDataError(errorMsg);
        }
    }

    @Override
    public void getProjectTreeData() {
        mModel.getProjectTreeData();
    }
}
