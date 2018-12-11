package com.feng.wanandroid.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseDialog;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/9
 */
public class LogoutDialog extends BaseDialog implements View.OnClickListener{

    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void clickEnsure();
        void clickCancel();
    }

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

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
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logout, null);
        TextView ensure = view.findViewById(R.id.tv_dialog_logout_ensure);
        ensure.setOnClickListener(this);
        TextView cancel = view.findViewById(R.id.tv_dialog_logout_cancel);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    protected float getWidthScale() {
        return 0.7f;
    }

    @Override
    protected float getHeightScale() {
        return 0.7f;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_logout_ensure:
                mOnClickListener.clickEnsure();
                break;
            case R.id.tv_dialog_logout_cancel:
                mOnClickListener.clickCancel();
                break;
            default:
                break;
        }
    }
}
