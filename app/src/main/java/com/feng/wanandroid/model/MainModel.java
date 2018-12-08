package com.feng.wanandroid.model;

import com.feng.wanandroid.contract.IMainContract;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class MainModel implements IMainContract.Model {

    private IMainContract.Presenter mPresenter;

    public MainModel(IMainContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void login() {

    }
}
