package com.feng.wanandroid.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class ShareUtil {

    /**
     * 分享文字内容
     *
     * @param content 分享内容（文字）
     * @param context
     */
    public static void shareText(String content, Context context) {
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);

        context.startActivity(intent);
    }

}
