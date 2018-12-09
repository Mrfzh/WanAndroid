package com.feng.wanandroid.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.feng.wanandroid.R;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.utils.BaseUtils;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/7
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    private Unbinder unBinder;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unBinder = ButterKnife.bind(this);  //绑定ButterKnife

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        initToolbar();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     *  获取当前活动的布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 获取当前活动的Presenter
     *
     * @return Presenter实例
     */
    protected abstract T getPresenter();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    protected abstract Toolbar getToolbar();

    protected void initToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextAppearance(this, R.style.ToolbarTitle);    //设置标题字体样式
        }
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 弹出Toast
     *
     * @param content 弹出的内容
     */
    protected void showShortToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置toolbar的标题
     *
     * @param title 标题
     */
    protected void setToolbarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    /**
     * 设置状态栏的颜色
     *
     * @param color 颜色(R.color.xxx)
     */
    protected void setStateBarColor(int color) {
        BaseUtils.setStatusBarColor(this, color);
    }

    /**
     * 设置toolbar的颜色
     *
     * @param toolbar 要设置的toolbar
     * @param color 颜色(R.color.xxx)
     */
    protected void setToolbarColor(Toolbar toolbar, int color) {
        toolbar.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * 跳转到另一活动
     *
     * @param activity 新活动.class
     */
    protected void jumpToNewActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
