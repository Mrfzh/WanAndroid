package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public interface IShowArticleContract {
    interface View {
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
    }
    interface Presenter {
        void collectSuccess(int position);
        void collectError(String errorMsg);
        void unCollectSuccess(int position);
        void unCollectError(String errorMsg);
        void collect(int id, int position);
        void unCollect(int id, int position);
    }
    interface Model {
        void collect(int id, int position);
        void unCollect(int id, int position);
    }
}
