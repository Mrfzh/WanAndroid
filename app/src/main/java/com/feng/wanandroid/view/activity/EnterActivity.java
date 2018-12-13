package com.feng.wanandroid.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseActivity;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.IEnterContract;
import com.feng.wanandroid.presenter.EnterPresenter;
import com.feng.wanandroid.utils.Preferences;

public class EnterActivity extends BaseActivity<EnterPresenter> implements IEnterContract.View {

    public static final String INTENT_USER = "intent_user";
    private static final int DELAY_TIME = 2 * 1000;
    private String mUserName;
    private String mPassword;

    @Override
    protected void doBeforeSetContentView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_enter;
    }

    @Override
    protected EnterPresenter getPresenter() {
        return new EnterPresenter();
    }

    @Override
    protected void initData() {
        mUserName = Preferences.getSharedPreferences(this, Constant.AUTO_LOGIN_SHARE_PRE)
                .getString(Constant.USER_NAME_KEY, null);
        mPassword = Preferences.getSharedPreferences(this, Constant.AUTO_LOGIN_SHARE_PRE)
                .getString(Constant.PASSWORD_KEY, null);
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected boolean setToolbarBackIcon() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doInOnCreate() {
        new Handler().postDelayed(() -> {
            //如果之前未退出登录则自动登录
            if (getIsLogin() && mUserName != null && mPassword != null) {
                mPresenter.autoLogin(mUserName, mPassword);
            } else {
                loginError();
            }
        }, DELAY_TIME);
    }

    @Override
    public void autoLoginSuccess(String userName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(INTENT_USER, userName);
        startActivity(intent);
        finish();
    }

    @Override
    public void autoLoginError() {
        loginError();
    }

    private void loginError() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(INTENT_USER, "");
        startActivity(intent);
        finish();
    }
}
