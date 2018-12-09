package com.feng.wanandroid.model;

import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IMainContract;
import com.feng.wanandroid.entity.LogoutBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.AccountService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class MainModel implements IMainContract.Model {
    private IMainContract.Presenter mPresenter;
    private AccountService mAccountService;

    public MainModel(IMainContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mAccountService = RetrofitHelper.getInstance().getRetrofit().create(AccountService.class);
    }

    @Override
    public void logout() {
        mAccountService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LogoutBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LogoutBean logoutBean) {
                        if (!logoutBean.getErrorMsg().equals("")) {
                            mPresenter.logoutError(logoutBean.getErrorMsg());
                        } else {
                            mPresenter.logoutSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mPresenter.logoutError(Constant.ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
