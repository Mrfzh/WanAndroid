package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.bean.CollectBean;
import com.feng.wanandroid.entity.bean.LoginBean;
import com.feng.wanandroid.entity.bean.LogoutBean;
import com.feng.wanandroid.entity.bean.RegisterBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public interface AccountService {

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<LoginBean> login(@Field("username") String userName, @Field("password") String password);

    /**
     * 退出登录
     *
     * @return
     */
    @GET("user/logout/json")
    Observable<LogoutBean> logout();

    /**
     * 注册
     *
     * @param userName 用户名
     * @param password 密码
     * @param rePassword 确认密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<RegisterBean> register(@Field("username") String userName, @Field("password") String password,
                                      @Field("repassword") String rePassword);

    /**
     * 收藏站内文章
     *
     * @param id 文章id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<CollectBean> collect(@Path("id") int id);

    /**
     * 取消收藏站内文章
     *
     * @param id 文章id（同上）
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<CollectBean> uncollect(@Path("id") int id);
}
