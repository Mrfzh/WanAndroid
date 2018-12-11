package com.feng.wanandroid.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.feng.wanandroid.R;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        setContentView(getCustomView());
    }

    /**
     * 获取当前布局视图
     *
     * @return 布局视图
     */
    protected abstract View getCustomView();

    /**
     * 获取相对于屏幕宽度的宽度比例
     *
     * @return 比例
     */
    protected abstract float getWidthScale();

    /**
     * 获取相对于宽度的高度比例
     *
     * @return 比例
     */
    protected abstract float getHeightScale();

    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        //设置高宽
        lp.width = (int) (screenWidth * getWidthScale()); // 宽度
        lp.height = (int) (lp.width * getHeightScale());     // 高度
        dialogWindow.setAttributes(lp);
    }
}
