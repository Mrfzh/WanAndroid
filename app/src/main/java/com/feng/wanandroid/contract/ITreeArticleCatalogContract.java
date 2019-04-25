package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.ArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public interface ITreeArticleCatalogContract {
    interface View {
        void getArticleInfoSuccess(List<ArticleData> articleDataList);
        void getArticleInfoError(String errorMsg);
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
    }
    interface Presenter {
        void getArticleInfoSuccess(List<ArticleData> articleDataList);
        void getArticleInfoError(String errorMsg);
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
        void getArticleInfo(int page, int catalogId);
        void collect(int id, int position);
        void unCollect(int id, int position);
    }
    interface Model {
        void getArticleInfo(int page, int catalogId);
        void collect(int id, int position);
        void unCollect(int id, int position);
    }
}
