package com.feng.wanandroid.app;

import android.app.Application;
import android.content.Context;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
