package com.feng.wanandroid.model;

import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IHomeContract;
import com.feng.wanandroid.entity.HomeArticleBean;
import com.feng.wanandroid.entity.HomeArticleData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.HomeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomeModel implements IHomeContract.Model {
    private IHomeContract.Presenter mPresenter;
    private HomeService mHomeService;
    private List<HomeArticleData> mHomeArticleDataList = new ArrayList<>();

    public HomeModel(IHomeContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mHomeService = RetrofitHelper.getInstance().getRetrofit().create(HomeService.class);
    }

    @Override
    public void getHomeArticle(int pageIndex) {
        mHomeService.getHomeArticle(pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeArticleBean>() {
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
                                for (int i = 0; i < datas.size(); i++) {
                                    mHomeArticleDataList.add(new HomeArticleData(datas.get(i).getAuthor(), datas.get(i).getChapterName(),
                                            datas.get(i).isCollect(), datas.get(i).getLink(), datas.get(i).getNiceDate(), datas.get(i).getTitle()));
                                }
                                mPresenter.getHomeArticleSuccess(mHomeArticleDataList);
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
}
