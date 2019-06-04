package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.ProjectTreeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Feng Zhaohao
 * Created on 2019/6/4
 */
public interface ProjectService {

    /**
     * 获取项目分类
     *
     * @return
     */
    @GET("project/tree/json")
    Observable<ProjectTreeBean> getProjectTree();
}
