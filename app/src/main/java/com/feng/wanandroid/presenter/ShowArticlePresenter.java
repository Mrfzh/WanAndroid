package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IShowArticleContract;
import com.feng.wanandroid.model.ShowArticleModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public class ShowArticlePresenter extends BasePresenter<IShowArticleContract.View>
    implements IShowArticleContract.Presenter{

    private IShowArticleContract.Model mModel;

    public ShowArticlePresenter() {
        mModel = new ShowArticleModel(this);
    }

    @Override
    public void collectSuccess(int position) {
        if (isAttachView()) {
            getMvpView().collectSuccess(position);
        }
    }

    @Override
    public void collectError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().collectError(errorMsg);
        }
    }

    @Override
    public void unCollectSuccess(int position) {
        if (isAttachView()) {
            getMvpView().unCollectSuccess(position);
        }
    }

    @Override
    public void unCollectError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().unCollectError(errorMsg);
        }
    }

    @Override
    public void collect(int id, int position) {
        mModel.collect(id, position);
    }

    @Override
    public void unCollect(int id, int position) {
        mModel.unCollect(id, position);
    }
}
