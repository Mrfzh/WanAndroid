package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IMainContract;
import com.feng.wanandroid.model.MainModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class MainPresenter extends BasePresenter<IMainContract.View> implements IMainContract.Presenter {
    private IMainContract.Model mModel;

    public MainPresenter() {
        mModel = new MainModel(this);
    }

    @Override
    public void logoutSuccess() {
        if (isAttachView()) {
            getMvpView().logoutSuccess();
        }
    }

    @Override
    public void logoutError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().logoutError(errorMsg);
        }
    }

    @Override
    public void logout() {
        mModel.logout();
    }
}
