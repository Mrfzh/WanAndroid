package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.model.HomeModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomePresenter extends BasePresenter<IHomeContract.View> implements IHomeContract.Presenter {
    private IHomeContract.Model mModel;

    public HomePresenter() {
        mModel = new HomeModel(this);
    }

    @Override
    public void getHomeArticleSuccess(List<ArticleData> articleDataList) {
        if (isAttachView()) {
            getMvpView().getHomeArticleSuccess(articleDataList);
        }
    }

    @Override
    public void getHomeArticleError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getHomeArticleError(errorMsg);
        }
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
    public void getHomeArticle(int pageIndex) {
        mModel.getHomeArticle(pageIndex);
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
