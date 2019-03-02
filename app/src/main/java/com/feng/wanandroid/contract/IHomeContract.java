package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.ArticleData;

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
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
        void getBannerInfoSuccess(List<String> imageUrlList, List<String> titleList, List<String> urlList);
        void getBannerInfoError(String errorMsg);
    }
    interface Presenter {
        void getHomeArticleSuccess(List<ArticleData> articleDataList);
        void getHomeArticleError(String errorMsg);
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
        void getBannerInfoSuccess(List<String> imageUrlList, List<String> titleList, List<String> urlList);
        void getBannerInfoError(String errorMsg);
        void getHomeArticle(int pageIndex);
        void collect(int id, int position);
        void unCollect(int id, int position);
        void getBannerInfo();
    }
    interface Model {
        void getHomeArticle(int pageIndex);
        void collect(int id, int position);
        void unCollect(int id, int position);
        void getBannerInfo();
    }
}
