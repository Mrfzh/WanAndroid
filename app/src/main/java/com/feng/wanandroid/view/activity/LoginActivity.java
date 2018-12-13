package com.feng.wanandroid.view.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.presenter.LoginPresenter;
import com.feng.wanandroid.utils.BaseUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    @BindView(R.id.base_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.et_login_user)
    EditText mUserEt;
    @BindView(R.id.et_login_password)
    EditText mPasswordEt;
    @BindView(R.id.pb_login)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("登录");
    }

    @Override
    protected Toolbar getToolbar() {
        return mLoginToolbar;
    }

    @Override
    protected boolean setToolbarBackIcon() {
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void loginSuccess(String userName, String password) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast("登录成功");
        updateUserInfo(userName, password);     //更新存储在本地的用户信息
        setIsLogin(true);
        Intent intent = new Intent(MainActivity.UPDATE_ACTION);
        intent.putExtra(MainActivity.UPDATE_TAG, userName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    @Override
    public void loginError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(errorMsg);
    }

    @OnClick({R.id.tv_login_login, R.id.tv_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_login:
                login();
                break;
            case R.id.tv_login_register:
                jumpToNewActivity(RegisterActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        if (mUserEt.getText().toString().equals("")) {
            showShortToast("请输入用户名");
            return;
        }
        if (mPasswordEt.getText().toString().equals("")) {
            showShortToast("请输入密码");
            return;
        }
        //先隐藏软键盘
        BaseUtils.hideSoftKeyboard(this);

        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.login(mUserEt.getText().toString(), mPasswordEt.getText().toString());
    }
}
