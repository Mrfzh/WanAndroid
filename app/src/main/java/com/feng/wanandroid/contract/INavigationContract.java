package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.NavigationData;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public interface INavigationContract {
    interface View {
        void getNavigationDataSuccess(NavigationData navigationData);
        void getNavigationDataError(String errorMsg);
    }
    interface Presenter {
        void getNavigationDataSuccess(NavigationData navigationData);
        void getNavigationDataError(String errorMsg);
        void getNavigationData();
    }
    interface Model {
        void getNavigationData();
    }
}
