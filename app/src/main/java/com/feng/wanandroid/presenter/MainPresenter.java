package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IMainContract;
import com.feng.wanandroid.model.MainModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class MainPresenter extends BasePresenter<IMainContract.View> implements IMainContract.Presenter{

    private IMainContract.Model mModel;

    public MainPresenter() {
        mModel = new MainModel(this);
    }

    @Override
    public void loginSuccess() {
        if(isAttachView()) {
            getMvpView().loginSuccess();
        }
    }

    @Override
    public void loginError() {
        if (isAttachView()) {
            getMvpView().loginError();
        }
    }

    @Override
    public void login() {
        mModel.login();
    }
}
