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
    }
    interface Presenter {
        void getArticleInfoSuccess(List<ArticleData> articleDataList);
        void getArticleInfoError(String errorMsg);
        void getArticleInfo(int page, int catalogId);
    }
    interface Model {
        void getArticleInfo(int page, int catalogId);
    }
}
