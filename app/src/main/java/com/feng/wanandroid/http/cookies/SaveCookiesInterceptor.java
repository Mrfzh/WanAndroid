package com.feng.wanandroid.http.cookies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.feng.wanandroid.app.MyApplication;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.utils.Preferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/8
 */
public class SaveCookiesInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            Preferences.getSharedPreferencesEditor(MyApplication.getContext(), Constant.COOKIES_SHARE_PRE)
                    .putStringSet(Constant.COOKIES_KEY, cookies)
                    .apply();
        }
        return originalResponse;
    }
}
