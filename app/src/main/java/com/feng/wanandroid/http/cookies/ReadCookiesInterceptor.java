package com.feng.wanandroid.http.cookies;

import android.util.Log;

import com.feng.wanandroid.app.MyApplication;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.utils.Preferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class ReadCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = (HashSet<String>) Preferences
                .getSharedPreferences(MyApplication.getContext(), Constant.COOKIES_SHARE_PRE)
                .getStringSet(Constant.COOKIES_KEY, new HashSet<>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}
