package com.feng.wanandroid.base;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/12
 */
public class BaseModel {

    /**
     * 执行一般的网络请求
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    protected <T> void execute(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
