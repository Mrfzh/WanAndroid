package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.TreeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public interface TreeService {
    /**
     * 获取体系列表
     *
     * @return
     */
    @GET("tree/json")
    Observable<TreeBean> getTree();
}
