package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.HomeArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public interface IHomeContract {
    interface View {
        void getHomeArticleSuccess(List<HomeArticleData> homeArticleDataList);
        void getHomeArticleError(String errorMsg);
    }
    interface Presenter {
        void getHomeArticleSuccess(List<HomeArticleData> homeArticleDataList);
        void getHomeArticleError(String errorMsg);
        void getHomeArticle(int pageIndex);
    }
    interface Model {
        void getHomeArticle(int pageIndex);
    }
}
