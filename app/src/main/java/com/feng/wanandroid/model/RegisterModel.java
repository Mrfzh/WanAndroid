package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IRegisterContract;
import com.feng.wanandroid.entity.RegisterBean;
import com.feng.wanandroid.http.api.AccountService;
import com.feng.wanandroid.http.cookies.SaveCookiesInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public class RegisterModel extends BaseModel implements IRegisterContract.Model {
    private IRegisterContract.Presenter mPresenter;
    private AccountService mAccountService;
    private okhttp3.OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //连接超时时间（10秒）
            .writeTimeout(10, TimeUnit.SECONDS)     //写操作超时时间
            .readTimeout(10, TimeUnit.SECONDS)     //读操作超时时间
            .addInterceptor(new SaveCookiesInterceptor());  //保存cookies

    public RegisterModel(IRegisterContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void register(String userName, String password, String rePassword) {
        mAccountService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AccountService.class);

        execute(mAccountService.register(userName, password, rePassword), new Observer<RegisterBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RegisterBean registerBean) {
                if (!registerBean.getErrorMsg().equals("")) {
                    mPresenter.registerError(registerBean.getErrorMsg());
                } else {
                    mPresenter.registerSuccess(userName, password);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mPresenter.registerError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
