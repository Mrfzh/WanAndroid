package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.NavigationBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public interface NavigationService {

    /**
     * 获取导航页面的数据
     *
     * @return
     */
    @GET("navi/json")
    Observable<NavigationBean> getNavigationData();
}
