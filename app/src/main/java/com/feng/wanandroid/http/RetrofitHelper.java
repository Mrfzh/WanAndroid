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

    private static class SingletonHolder {
        private static final RetrofitHelper mInstance = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance(){
        return SingletonHolder.mInstance;
    }

    private RetrofitHelper() {
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)   //连接超时时间（10秒）
                .writeTimeout(10, TimeUnit.SECONDS)     //写操作超时时间
                .readTimeout(10, TimeUnit.SECONDS)     //读操作超时时间
                .addInterceptor(new ReadCookiesInterceptor());  //读取cookies

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

}
