package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.TreeArticleBean;
import com.feng.wanandroid.entity.bean.TreeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * 获取体系文章
     *
     * @param page 页码
     * @param id 二级目录id
     * @return
     */
    @GET("article/list/{page}/json?")
    Observable<TreeArticleBean> getTreeArticle(@Path("page") int page, @Query("cid") int id);
}
