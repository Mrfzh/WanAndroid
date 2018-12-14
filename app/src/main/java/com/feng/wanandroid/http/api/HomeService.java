package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.HomeArticleBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public interface HomeService {

    /**
     * 获取首页文章
     *
     * @param pageIndex 页码
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<HomeArticleBean> getHomeArticle(@Path("page") int pageIndex);
}
