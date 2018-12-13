package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.model.LoginModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class LoginPresenter extends BasePresenter<ILoginContract.View> implements ILoginContract.Presenter{

    private ILoginContract.Model mModel;

    public LoginPresenter() {
        mModel = new LoginModel(this);
    }


    @Override
    public void loginSuccess(String userName, String password) {
        if (isAttachView()) {
            getMvpView().loginSuccess(userName, password);
        }
    }

    @Override
    public void loginError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().loginError(errorMsg);
        }
    }

    @Override
    public void login(String user, String password) {
        mModel.login(user, password);
    }
}
