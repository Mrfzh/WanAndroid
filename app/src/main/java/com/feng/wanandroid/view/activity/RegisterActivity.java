package com.feng.wanandroid.view.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.config.EventBusCode;
import com.feng.wanandroid.contract.IRegisterContract;
import com.feng.wanandroid.entity.eventbus.Event;
import com.feng.wanandroid.entity.eventbus.HomeEvent;
import com.feng.wanandroid.entity.eventbus.MainEvent;
import com.feng.wanandroid.presenter.RegisterPresenter;
import com.feng.wanandroid.utils.BaseUtils;
import com.feng.wanandroid.utils.EventBusUtil;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterContract.View {

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_register_user)
    EditText mUserEt;
    @BindView(R.id.et_register_password)
    EditText mPasswordEt;
    @BindView(R.id.et_register_ensure_password)
    EditText mEnsurePasswordEt;
    @BindView(R.id.tv_register_commit)
    TextView mCommitTv;
    @BindView(R.id.pb_register)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterPresenter getPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected boolean setToolbarBackIcon() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("注册");
    }

    @Override
    protected void initView() {
        mCommitTv.setOnClickListener(v -> {
            if (checkEt()) {
                BaseUtils.hideSoftKeyboard(this);
                mPresenter.register(mUserEt.getText().toString(), mPasswordEt.getText().toString(),
                        mEnsurePasswordEt.getText().toString());
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void doInOnCreate() {

    }

    @Override
    public void registerSuccess(String userName, String password) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast("注册成功");
        updateUserInfo(userName, password);     //更新用户信息

        //更新用户信息
        Event<MainEvent> mainEvent = new Event<>(EventBusCode.Register2Main, new MainEvent(userName));
        EventBusUtil.sendEvent(mainEvent);
        //更新首页文章
        Event<HomeEvent> homeEvent = new Event<>(EventBusCode.Register2Home, new HomeEvent(true, false));
        EventBusUtil.sendEvent(homeEvent);

        finish();
    }

    @Override
    public void registerError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(errorMsg);
    }

    /**
     * 检查EditText的内容是否输入完整
     *
     * @return
     */
    private boolean checkEt() {
        if (mUserEt.getText().toString().equals("")) {
            showShortToast("请输入用户名");
            return false;
        }
        if (mPasswordEt.getText().toString().equals("")) {
            showShortToast("请输入密码");
            return false;
        }
        if (mEnsurePasswordEt.getText().toString().equals("")) {
            showShortToast("请再次输入密码");
            return false;
        }
        return true;
    }
}
