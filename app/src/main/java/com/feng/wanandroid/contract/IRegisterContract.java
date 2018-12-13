package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public interface IRegisterContract {
    interface View {
        void registerSuccess(String userName, String password);
        void registerError(String errorMsg);
    }
    interface Presenter {
        void registerSuccess(String userName, String password);
        void registerError(String errorMsg);
        void register(String userName, String password, String rePassword);
    }
    interface Model {
        void register(String userName, String password, String rePassword);
    }
}
