package com.feng.wanandroid.base;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class BasePresenter<V> {
    private V view;

    public void attachView(V view){
        this.view = view;
    }

    //不需要传参数
    public void detachView(){
        this.view = null;
    }

    protected  boolean isAttachView(){
        return view != null;
    }

    protected V getMvpView(){
        return view;
    }
}
