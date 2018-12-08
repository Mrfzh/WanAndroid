package com.feng.wanandroid.http.api;

import com.feng.wanandroid.entity.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
}
