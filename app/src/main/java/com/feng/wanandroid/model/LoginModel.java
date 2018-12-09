package com.feng.wanandroid.model;

import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.entity.LoginBean;
import com.feng.wanandroid.http.api.AccountService;
import com.feng.wanandroid.http.cookies.SaveCookiesInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class LoginModel implements ILoginContract.Model {

    private ILoginContract.Presenter mPresenter;
    private AccountService mAccountService;
    private okhttp3.OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //连接超时时间（10秒）
            .writeTimeout(10, TimeUnit.SECONDS)     //写操作超时时间
            .readTimeout(10, TimeUnit.SECONDS)     //读操作超时时间
            .addInterceptor(new SaveCookiesInterceptor());  //保存cookies

    public LoginModel(ILoginContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void login(String user, String password) {
        mAccountService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AccountService.class);

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
                        mPresenter.loginError(Constant.ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
