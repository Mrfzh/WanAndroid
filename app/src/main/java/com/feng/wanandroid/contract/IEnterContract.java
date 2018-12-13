package com.feng.wanandroid.contract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public interface IEnterContract {
    interface View {
        void autoLoginSuccess(String userName);
        void autoLoginError();
    }
    interface Presenter {
        void autoLoginSuccess(String userName);
        void autoLoginError();
        void autoLogin(String userName, String password);
    }
    interface Model {
        void autoLogin(String userName, String password);
    }
}
