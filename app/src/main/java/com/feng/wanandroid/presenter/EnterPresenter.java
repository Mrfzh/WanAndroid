package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IEnterContract;
import com.feng.wanandroid.model.EnterModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public class EnterPresenter extends BasePresenter<IEnterContract.View> implements IEnterContract.Presenter{
    private IEnterContract.Model mModel;

    public EnterPresenter() {
        mModel = new EnterModel(this);
    }

    @Override
    public void autoLoginSuccess(String userName) {
        if (isAttachView()) {
            getMvpView().autoLoginSuccess(userName);
        }
    }

    @Override
    public void autoLoginError() {
        if (isAttachView()) {
            getMvpView().autoLoginError();
        }
    }

    @Override
    public void autoLogin(String userName, String password) {
        mModel.autoLogin(userName, password);
    }
}
