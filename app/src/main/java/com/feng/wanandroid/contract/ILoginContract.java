package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public interface ILoginContract {
    interface View {
        void loginSuccess(String userName);
        void loginError(String errorMsg);
    }
    interface Presenter {
        void loginSuccess(String userName);
        void loginError(String errorMsg);
        void login(String user, String password);
    }
    interface Model {
        void login(String user, String password);
    }
}
