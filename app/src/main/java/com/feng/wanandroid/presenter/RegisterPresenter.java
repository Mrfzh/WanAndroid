package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IRegisterContract;
import com.feng.wanandroid.model.RegisterModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public class RegisterPresenter extends BasePresenter<IRegisterContract.View> implements IRegisterContract.Presenter {
    private IRegisterContract.Model mModel;

    public RegisterPresenter() {
        mModel = new RegisterModel(this);
    }

    @Override
    public void registerSuccess(String userName, String password) {
        if(isAttachView()) {
            getMvpView().registerSuccess(userName, password);
        }
    }

    @Override
    public void registerError(String errorMsg) {
        if(isAttachView()) {
            getMvpView().registerError(errorMsg);
        }
    }

    @Override
    public void register(String userName, String password, String rePassword) {
        mModel.register(userName, password, rePassword);
    }
}
