package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.CollectArticleBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public interface CollectionService {

    /**
     * 获取收藏文章列表
     *
     * @param index 页码
     * @return
     */
    @GET("lg/collect/list/{index}/json")
    Observable<CollectArticleBean> getCollectArticleList(@Path("index") int index);
}
