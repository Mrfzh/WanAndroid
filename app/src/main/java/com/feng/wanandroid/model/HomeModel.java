package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.bean.CollectBean;
import com.feng.wanandroid.entity.bean.HomeArticleBean;
import com.feng.wanandroid.entity.ArticleData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.AccountService;
import com.feng.wanandroid.http.api.HomeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomeModel extends BaseModel implements IHomeContract.Model {
    private IHomeContract.Presenter mPresenter;
    private HomeService mHomeService;
    private AccountService mAccountService;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

    public HomeModel(IHomeContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mHomeService = RetrofitHelper.getInstance().getRetrofit().create(HomeService.class);
        mAccountService = RetrofitHelper.getInstance().getRetrofit().create(AccountService.class);
    }

    @Override
    public void getHomeArticle(int pageIndex) {
        execute(mHomeService.getHomeArticle(pageIndex), new Observer<HomeArticleBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HomeArticleBean homeArticleBean) {
                if (!homeArticleBean.getErrorMsg().equals("")) {
                    mPresenter.getHomeArticleError(homeArticleBean.getErrorMsg());
                } else {
                    List<HomeArticleBean.DataBean.DatasBean> datas =  homeArticleBean.getData().getDatas();
                    if (datas.size() == 0) {
                        //如果没有数据了就返回null
                        mPresenter.getHomeArticleSuccess(null);
                    } else {
                        if (pageIndex == 0) {
                            mArticleDataList = new ArrayList<>();   //如果重新请求的话要重置集合
                        }
                        for (int i = 0; i < datas.size(); i++) {
                            mArticleDataList.add(new ArticleData(datas.get(i).getAuthor(), datas.get(i).getChapterName(),
                                    datas.get(i).isCollect(), datas.get(i).getLink(), datas.get(i).getNiceDate(),
                                    datas.get(i).getTitle(), datas.get(i).getId()));
                        }
                        mPresenter.getHomeArticleSuccess(mArticleDataList);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mPresenter.getHomeArticleError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
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
