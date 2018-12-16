package com.feng.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class IntentUtil {
    /**
     * 调用系统浏览器浏览网址
     *
     * @param context
     * @param link 网址
     */
    public static void callLocalBrowser(Context context, String link) {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(link);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
