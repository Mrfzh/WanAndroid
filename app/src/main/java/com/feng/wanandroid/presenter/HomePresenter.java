package com.feng.wanandroid.presenter;

import android.util.Log;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.HomeArticleData;
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
    public void getHomeArticleSuccess(List<HomeArticleData> homeArticleDataList) {
        if (isAttachView()) {
            getMvpView().getHomeArticleSuccess(homeArticleDataList);
        }
    }

    @Override
    public void getHomeArticleError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getHomeArticleError(errorMsg);
        }
    }

    @Override
    public void getHomeArticle(int pageIndex) {
        Log.d("fzh", "getHomeArticle: run");
        mModel.getHomeArticle(pageIndex);
    }
}
