package com.feng.wanandroid.view.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.base.MyApplication;
import com.feng.wanandroid.contract.ILoginContract;
import com.feng.wanandroid.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    public static final String UPDATE_TAG = "username";

    @BindView(R.id.tb_login_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.et_login_user)
    EditText mUserEt;
    @BindView(R.id.et_login_password)
    EditText mPasswordEt;

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
        setToolbarTitle("登录");
        mLoginToolbar.setNavigationIcon(R.mipmap.back);
        mLoginToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected Toolbar getToolbar() {
        return mLoginToolbar;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void loginSuccess(String userName) {
        showShortToast("登录成功");
        Intent intent = new Intent(MainActivity.UPDATE_ACTION);
        intent.putExtra(UPDATE_TAG, userName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    @Override
    public void loginError(String errorMsg) {
        showShortToast(errorMsg);
    }

    @OnClick({R.id.tv_login_login, R.id.tv_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_login:
                login();
                break;
            case R.id.tv_login_register:
                break;
            default:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        checkContent();
        mPresenter.login(mUserEt.getText().toString(), mPasswordEt.getText().toString());
    }

    private void checkContent() {
        if (mUserEt.getText().toString().equals(""))
            showShortToast("请输入用户名");
        if (mPasswordEt.getText().toString().equals(""))
            showShortToast("请输入密码");
    }
}
