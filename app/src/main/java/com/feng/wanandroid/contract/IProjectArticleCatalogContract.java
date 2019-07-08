package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.ProjectArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public interface IProjectArticleCatalogContract {
    interface View {
        void getProjectArticleDataSuccess(List<ProjectArticleData> projectArticleDataList);
        void getProjectArticleDataError(String errorMsg);
    }
    interface Presenter {
        void getProjectArticleDataSuccess(List<ProjectArticleData> projectArticleDataList);
        void getProjectArticleDataError(String errorMsg);
        void getProjectArticleData(int page, int cid);  //获取项目文章信息
    }
    interface Model {
        void getProjectArticleData(int page, int cid);  //获取项目文章信息
    }
}
