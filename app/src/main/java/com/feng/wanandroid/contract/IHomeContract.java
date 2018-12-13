package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.ArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public interface IHomeContract {
    interface View {
        void getHomeArticleSuccess(List<ArticleData> articleDataList);
        void getHomeArticleError(String errorMsg);
        void collectSuccess(int position);
        void collectError(String errorMsg);
    }
    interface Presenter {
        void getHomeArticleSuccess(List<ArticleData> articleDataList);
        void getHomeArticleError(String errorMsg);
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void getHomeArticle(int pageIndex);
        void collect(int id, int position);
    }
    interface Model {
        void getHomeArticle(int pageIndex);
        void collect(int id, int position);
    }
}
