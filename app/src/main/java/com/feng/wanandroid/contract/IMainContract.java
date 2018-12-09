package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public interface IMainContract {
    interface View {
        void logoutSuccess();
        void logoutError(String errorMsg);
    }
    interface Presenter {
        void logoutSuccess();
        void logoutError(String errorMsg);
        void logout();
    }
    interface Model {
        void logout();
    }
}
