package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.INavigationContract;
import com.feng.wanandroid.entity.data.NavigationData;
import com.feng.wanandroid.model.NavigationModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public class NavigationPresenter extends BasePresenter<INavigationContract.View>
        implements INavigationContract.Presenter{

    private INavigationContract.Model mModel;

    public NavigationPresenter() {
        mModel = new NavigationModel(this);
    }

    @Override
    public void getNavigationDataSuccess(NavigationData navigationData) {
        if (isAttachView()) {
            getMvpView().getNavigationDataSuccess(navigationData);
        }
    }

    @Override
    public void getNavigationDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getNavigationDataError(errorMsg);
        }
    }

    @Override
    public void getNavigationData() {
        mModel.getNavigationData();
    }
}
