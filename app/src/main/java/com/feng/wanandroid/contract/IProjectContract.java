package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.ProjectTreeData;

/**
 * @author Feng Zhaohao
 * Created on 2019/6/4
 */
public interface IProjectContract {
    interface View {
        void getProjectTreeDataSuccess(ProjectTreeData projectTreeData);
        void getProjectTreeDataError(String errorMsg);
    }
    interface Presenter {
        void getProjectTreeDataSuccess(ProjectTreeData projectTreeData);
        void getProjectTreeDataError(String errorMsg);
        void getProjectTreeData();
    }
    interface Model {
        void getProjectTreeData();      //获取项目分类数据
    }
}
