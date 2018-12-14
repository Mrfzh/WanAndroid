package com.feng.wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static <T> void removeListItem(List<T> list, int position){
        Iterator<T> iterator = list.iterator();
        T target = list.get(position);
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item.equals(target)) {
                iterator.remove();
            }
        }
    }
}
