package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.model.CollectionModel;

import java.util.List;

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
    public void getCollectListSuccess(List<ArticleData> articleDataList) {
        if (isAttachView()) {
            getMvpView().getCollectListSuccess(articleDataList);
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
