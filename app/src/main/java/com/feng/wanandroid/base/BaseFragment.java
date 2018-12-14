package com.feng.wanandroid.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.feng.wanandroid.utils.EventBusUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/7
 */
public abstract class BaseFragment<V extends BasePresenter> extends Fragment {

    private Unbinder unBinder;
    protected V mPresenter;   //该Fragment对应的Presenter

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }

        doInOnCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);  //第三个参数一定要设为false
        unBinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 在onCreate方法中执行的操作
     */
    protected abstract void doInOnCreate();

    /**
     * 获取fragment布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 获取Presenter实例
     *
     * @return 相应的Presenter实例
     */
    protected abstract V getPresenter();

    protected void showShortToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

    protected void jump2Activity(Class activity) {
        startActivity(new Intent(getContext(), activity));
    }

    /**
     * 是否注册EventBus，注册后才可以订阅事件
     *
     * @return true表示绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

}
