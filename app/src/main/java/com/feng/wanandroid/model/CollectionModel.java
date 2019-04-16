package com.feng.wanandroid.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.bean.CollectBean;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.entity.bean.CollectArticleBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.AccountService;
import com.feng.wanandroid.http.api.CollectionService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class CollectionModel extends BaseModel implements ICollectionContract.Model {

    private ICollectionContract.Presenter mPresenter;
    private CollectionService mCollectionService;
    private AccountService mAccountService;
    private List<ArticleData> mArticleDataList = new ArrayList<>();
    private static final String TAG = "fzh";

    public CollectionModel(ICollectionContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mCollectionService = RetrofitHelper.getInstance().create(CollectionService.class);
        mAccountService = RetrofitHelper.getInstance().create(AccountService.class);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCollectList(int currentPage) {
        execute(mCollectionService.getCollectArticleList(currentPage), new Observer<CollectArticleBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CollectArticleBean collectArticleBean) {
                if (!collectArticleBean.getErrorMsg().equals("")) {
                    mPresenter.getCollectListError(collectArticleBean.getErrorMsg());
                } else {

                    List<CollectArticleBean.DataBean.DatasBean> datas = collectArticleBean.getData().getDatas();
                    if (datas.size() == 0) {
                        //如果没有数据了就返回null
                        mPresenter.getCollectListSuccess(null);
                    } else {
                        if (currentPage == 0) {
                            mArticleDataList = new ArrayList<>();   //如果重新请求的话要重置集合
                        }
                        for (int i = 0; i < datas.size(); i++) {
                            mArticleDataList.add(new ArticleData(datas.get(i).getAuthor(), datas.get(i).getChapterName(),
                                    true, datas.get(i).getLink(), datas.get(i).getNiceDate(), datas.get(i).getTitle()
                            , datas.get(i).getOriginId()));
                        }
                        mPresenter.getCollectListSuccess(mArticleDataList);
                    }
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

    @Override
    public void multiUnCollect(List<Integer> removeIndexList, List<Integer> idList) {
        List<Observable<CollectBean>> observableList = new ArrayList<>();   //observable集合
        //添加要删除收藏所对应的observable
        for (int i = 0; i < idList.size(); i++) {
            observableList.add(mAccountService.uncollect(idList.get(i)));
        }

        Observable.concat(observableList)   //使用concat合并多个相同的操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CollectBean collectBean) {
                        if (!collectBean.getErrorMsg().equals("")) {
                            mPresenter.multiUnCollectError(collectBean.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.multiUnCollectError(Constant.ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {
                        mPresenter.multiUnCollectSuccess(removeIndexList);
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
