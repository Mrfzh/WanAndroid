package com.feng.wanandroid.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.base.BasePresenter;

import java.util.Objects;

import butterknife.BindView;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/7
 */
public class TestFragment extends BaseFragment {

    @BindView(R.id.tv_fg_test_content)
    TextView mContentTV;

    private static final String INIT_CONTENT = "content";

    private String mContent;

    public static TestFragment newInstance(String content) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INIT_CONTENT, content);
        testFragment.setArguments(bundle);

        return testFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        mContentTV.setText(mContent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void doInOnCreate() {
        mContent = Objects.requireNonNull(getArguments()).getString(INIT_CONTENT);
    }
}
