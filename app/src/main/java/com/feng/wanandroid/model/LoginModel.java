package com.feng.wanandroid.model;

import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.entity.LoginBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.AccountService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class LoginModel implements ILoginContract.Model {

    private ILoginContract.Presenter mPresenter;
    private AccountService mAccountService;

    public LoginModel(ILoginContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        this.mAccountService = RetrofitHelper
                .getInstance(false).getRetrofit().create(AccountService.class); //同时保存cookies
    }

    @Override
    public void login(String user, String password) {
        mAccountService.login(user, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (!loginBean.getErrorMsg().equals("")) {
                            mPresenter.loginError(loginBean.getErrorMsg());
                        } else {
                            mPresenter.loginSuccess(loginBean.getData().getUsername());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mPresenter.loginError("服务器不给力o(╥﹏╥)o");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
