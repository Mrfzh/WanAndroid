package com.feng.wanandroid.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/7
 */
public class BaseUtils {

    /**
     * 设置状态栏的颜色
     *
     * @param activity 对应的活动
     * @param colorResId 颜色id
     */
    public static void setStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
