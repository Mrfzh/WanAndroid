package com.feng.wanandroid.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.wanandroid.R;
import com.feng.wanandroid.base.BaseFragment;
import com.feng.wanandroid.contract.IProjectContract;
import com.feng.wanandroid.entity.data.ProjectTreeData;
import com.feng.wanandroid.presenter.ProjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class ProjectFragment extends BaseFragment<ProjectPresenter>
        implements IProjectContract.View {

    @BindView(R.id.tv_project_content)
    TextView mContentTv;

    private List<Integer> mIdList;      //项目id列表
    private List<String> mNameList;     //项目名列表

    @Override
    protected void doInOnCreate() {
        mPresenter.getProjectTreeData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected ProjectPresenter getPresenter() {
        return new ProjectPresenter();
    }

    /**
     * 获取项目列表数据成功
     *
     * @param projectTreeData
     */
    @Override
    public void getProjectTreeDataSuccess(ProjectTreeData projectTreeData) {
        if (projectTreeData != null) {
            mIdList = projectTreeData.getIdList();
            mNameList = projectTreeData.getNameList();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mNameList.size(); i++) {
                builder.append(mNameList.get(i));
                builder.append("----");
                builder.append(mIdList.get(i));
                builder.append("----");
            }

//            mContentTv.setText(builder.toString());
        } else {
            showShortToast("获取数据失败");
        }
    }

    /**
     * 获取项目列表数据失败
     *
     * @param errorMsg
     */
    @Override
    public void getProjectTreeDataError(String errorMsg) {
        showShortToast(errorMsg);
    }

}
