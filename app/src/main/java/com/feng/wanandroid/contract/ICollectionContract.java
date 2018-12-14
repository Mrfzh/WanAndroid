package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.ArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public interface ICollectionContract {
    interface View {
        void getCollectListSuccess(List<ArticleData> articleDataList);
        void getCollectListError(String errorMsg);
    }
    interface Presenter {
        void getCollectListSuccess(List<ArticleData> articleDataList);
        void getCollectListError(String errorMsg);
        void getCollectList(int currentPage);
    }
    interface Model {
        void getCollectList(int currentPage);
    }
}
