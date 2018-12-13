package com.feng.wanandroid.model;

import android.annotation.SuppressLint;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ICollectionContract;
import com.feng.wanandroid.entity.ArticleData;
import com.feng.wanandroid.entity.CollectArticleBean;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.CollectionService;

import java.util.ArrayList;
import java.util.List;

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
    List<ArticleData> mArticleDataList = new ArrayList<>();

    public CollectionModel(ICollectionContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mCollectionService = RetrofitHelper.getInstance().getRetrofit().create(CollectionService.class);
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
}
