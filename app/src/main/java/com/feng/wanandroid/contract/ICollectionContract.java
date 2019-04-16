package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.ArticleData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public interface ICollectionContract {
    interface View {
        void getCollectListSuccess(List<ArticleData> articleDataList);
        void getCollectListError(String errorMsg);
        void multiUnCollectSuccess(List<Integer> removeIndexList);
        void multiUnCollectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
    }
    interface Presenter {
        void getCollectListSuccess(List<ArticleData> articleDataList);
        void getCollectListError(String errorMsg);
        void multiUnCollectSuccess(List<Integer> removeIndexList);
        void multiUnCollectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
        void getCollectList(int currentPage);
        void multiUnCollect(List<Integer> removeIndexList, List<Integer> idList);
        void unCollect(int id, int position);
    }
    interface Model {
        void getCollectList(int currentPage);
        void multiUnCollect(List<Integer> removeIndexList, List<Integer> idList);
        void unCollect(int id, int position);
    }
}
