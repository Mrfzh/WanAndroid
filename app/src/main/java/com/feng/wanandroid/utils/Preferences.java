package com.feng.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class Preferences {

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static boolean clearSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }

}
