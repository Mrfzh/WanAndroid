package com.feng.wanandroid.presenter;

import com.feng.wanandroid.base.BasePresenter;
import com.feng.wanandroid.contract.ITreeContract;
import com.feng.wanandroid.entity.data.TreeData;
import com.feng.wanandroid.model.TreeModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreePresenter extends BasePresenter<ITreeContract.View> implements ITreeContract.Presenter {
    private ITreeContract.Model mModel;

    public TreePresenter() {
        mModel = new TreeModel(this);
    }

    @Override
    public void getTreeSuccess(List<TreeData> treeDataList) {
        if (isAttachView()) {
            getMvpView().getTreeSuccess(treeDataList);
        }
    }

    @Override
    public void getTreeError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getTreeError(errorMsg);
        }
    }

    @Override
    public void getTree() {
        mModel.getTree();
    }
}
