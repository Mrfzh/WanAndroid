package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.INavigationContract;
import com.feng.wanandroid.entity.bean.NavigationBean;
import com.feng.wanandroid.entity.data.NavigationData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.NavigationService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public class NavigationModel extends BaseModel implements INavigationContract.Model {

    private INavigationContract.Presenter mPresenter;
    private NavigationService mNavigationService;

    public NavigationModel(INavigationContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mNavigationService = RetrofitHelper.getInstance().create(NavigationService.class);
    }

    @Override
    public void getNavigationData() {
        execute(mNavigationService.getNavigationData(), new Observer<NavigationBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(NavigationBean navigationBean) {
                if (navigationBean.getErrorMsg().equals("")) {
                    //处理接收到的导航数据
                    List<String> chapterNames = new ArrayList<>();
                    List<NavigationData.ChapterData> chapterDataList = new ArrayList<>();

                    List<NavigationBean.DataBean> dataBeans = navigationBean.getData();
                    for (int i = 0; i < dataBeans.size(); i++) {
                        chapterNames.add(dataBeans.get(i).getName());
                        List<String> titles = new ArrayList<>();
                        List<String> links = new ArrayList<>();
                        List<NavigationBean.DataBean.ArticlesBean> articlesBeans = dataBeans.get(i).getArticles();
                        for (int j = 0; j < articlesBeans.size(); j++) {
                            titles.add(articlesBeans.get(j).getTitle());
                            links.add(articlesBeans.get(j).getLink());
                        }
                        chapterDataList.add(new NavigationData.ChapterData(titles, links));
                    }

                    NavigationData navigationData = new NavigationData(chapterNames, chapterDataList);
                    mPresenter.getNavigationDataSuccess(navigationData);
                } else {
                    mPresenter.getNavigationDataError(navigationBean.getErrorMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mPresenter.getNavigationDataError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
