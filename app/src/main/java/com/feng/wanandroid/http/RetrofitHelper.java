package com.feng.wanandroid.http;

import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.http.cookies.ReadCookiesInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class RetrofitHelper {

    private Retrofit mRetrofit;
    private static  okhttp3.OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)   //连接超时时间（10秒）
        .writeTimeout(10, TimeUnit.SECONDS)     //写操作超时时间
        .readTimeout(10, TimeUnit.SECONDS)     //读操作超时时间
        .addInterceptor(new ReadCookiesInterceptor());  //读取cookies
    private static RetrofitHelper mInstance = null;

    public static RetrofitHelper getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitHelper();
        }
        return mInstance;
    }

    public RetrofitHelper() {
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }

}
