package com.feng.wanandroid.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseDialog;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class LogoutDialog extends BaseDialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //将默认背景设置为透明，否则有白底，看不出圆角
    }

    public LogoutDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getCustomView() {
        return R.layout.dialog_logout;
    }

    @Override
    protected float getWidthScale() {
        return 0.7f;
    }

    @Override
    protected float getHeightScale() {
        return 0.7f;
    }
}
