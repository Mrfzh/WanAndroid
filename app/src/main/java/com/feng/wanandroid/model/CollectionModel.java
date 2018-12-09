package com.feng.wanandroid.model;

import android.annotation.SuppressLint;

import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.CollectionService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class CollectionModel implements ICollectionContract.Model {

    private ICollectionContract.Presenter mPresenter;
    private CollectionService mCollectionService;

    public CollectionModel(ICollectionContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mCollectionService = RetrofitHelper.getInstance().getRetrofit().create(CollectionService.class);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCollectList(int currentPage) {
        mCollectionService.getCollectArticleList(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectArticleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CollectArticleBean collectArticleBean) {
                        if (!collectArticleBean.getErrorMsg().equals("")) {
                            mPresenter.getCollectListError(collectArticleBean.getErrorMsg());
                        } else {
                            mPresenter.getCollectListSuccess(collectArticleBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mPresenter.getCollectListError(Constant.ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
