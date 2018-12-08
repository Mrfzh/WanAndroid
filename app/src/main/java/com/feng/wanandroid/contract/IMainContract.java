package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public interface IMainContract {
    interface View {
        void loginSuccess();
        void loginError();
    }
    interface Presenter {
        void loginSuccess();
        void loginError();
        void login();
    }
    interface Model {
        void login();
    }
}
