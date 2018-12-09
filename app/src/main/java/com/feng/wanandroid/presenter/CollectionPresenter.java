package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.model.CollectionModel;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class CollectionPresenter extends BasePresenter<ICollectionContract.View> implements ICollectionContract.Presenter{

    private ICollectionContract.Model mModel;

    public CollectionPresenter() {
        mModel = new CollectionModel(this);
    }

    @Override
    public void getCollectListSuccess(CollectArticleBean collectArticleBean) {
        if (isAttachView()) {
            getMvpView().getCollectListSuccess(collectArticleBean);
        }
    }

    @Override
    public void getCollectListError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getCollectListError(errorMsg);
        }
    }

    @Override
    public void getCollectList(int currentPage) {
        mModel.getCollectList(currentPage);
    }
}
