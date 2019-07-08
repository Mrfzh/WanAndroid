package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.ProjectArticleCatalogBean;
import com.feng.wanandroid.entity.bean.ProjectTreeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * @param page 页码
     * @param cid 分类的id
     * @return
     */
    @GET("project/list/{page}/json")
    Observable<ProjectArticleCatalogBean> getProjectArticleCatalog(
            @Path ("page") int page, @Query("cid") int cid);
}
