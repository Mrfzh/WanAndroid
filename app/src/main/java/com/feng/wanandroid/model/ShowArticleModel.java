package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IShowArticleContract;
import com.feng.wanandroid.entity.bean.CollectBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.AccountService;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public class ShowArticleModel extends BaseModel implements IShowArticleContract.Model {

    private IShowArticleContract.Presenter mPresenter;
    private AccountService mAccountService;

    public ShowArticleModel(IShowArticleContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mAccountService = RetrofitHelper.getInstance().create(AccountService.class);
    }

    @Override
    public void collect(int id, int position) {
        execute(mAccountService.collect(id), new Observer<CollectBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CollectBean collectBean) {
                if (!collectBean.getErrorMsg().equals("")) {
                    mPresenter.collectError(collectBean.getErrorMsg());
                } else {
                    mPresenter.collectSuccess(position);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mPresenter.collectError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unCollect(int id, int position) {
        execute(mAccountService.uncollect(id), new Observer<CollectBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CollectBean collectBean) {
                if (!collectBean.getErrorMsg().equals("")) {
                    mPresenter.unCollectError(collectBean.getErrorMsg());
                } else {
                    mPresenter.unCollectSuccess(position);
                }
            }

            @Override
            public void onError(Throwable e) {
                mPresenter.unCollectError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
