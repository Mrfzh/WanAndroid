package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IProjectContract;
import com.feng.wanandroid.entity.bean.ProjectTreeBean;
import com.feng.wanandroid.entity.data.ProjectTreeData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.ProjectService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2019/6/4
 */
public class ProjectModel extends BaseModel implements IProjectContract.Model {

    private IProjectContract.Presenter mPresenter;
    private ProjectService mProjectService;

    public ProjectModel(IProjectContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mProjectService = RetrofitHelper.getInstance().create(ProjectService.class);
    }

    @Override
    public void getProjectTreeData() {
        execute(mProjectService.getProjectTree(), new Observer<ProjectTreeBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ProjectTreeBean projectTreeBean) {
                ProjectTreeData projectTreeData = null;
                if (projectTreeBean != null) {
                    //返回错误结果
                    if (!projectTreeBean.getErrorMsg().equals("")) {
                        mPresenter.getProjectTreeDataError(projectTreeBean.getErrorMsg());
                        return;
                    }
                    //提取数据
                    List<Integer> idList = new ArrayList<>();
                    List<String> nameList = new ArrayList<>();
                    for (int i = 0; i < projectTreeBean.getData().size(); i++) {
                        idList.add(projectTreeBean.getData().get(i).getId());
                        String oleName = projectTreeBean.getData().get(i).getName();
                        String newName = oleName.replaceAll("amp;", "");
                        nameList.add(newName);
                    }
                    projectTreeData = new ProjectTreeData(idList, nameList);
                }
                mPresenter.getProjectTreeDataSuccess(projectTreeData);
            }

            @Override
            public void onError(Throwable e) {
                mPresenter.getProjectTreeDataError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
