package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ITreeContract;
import com.feng.wanandroid.entity.bean.TreeBean;
import com.feng.wanandroid.entity.data.TreeData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.TreeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public class TreeModel extends BaseModel implements ITreeContract.Model {
    private ITreeContract.Presenter mPresenter;
    private TreeService mTreeService;

    public TreeModel(ITreeContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mTreeService = RetrofitHelper.getInstance().create(TreeService.class);
    }

    @Override
    public void getTree() {
        execute(mTreeService.getTree(), new Observer<TreeBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(TreeBean treeBean) {
                if (!treeBean.getErrorMsg().equals("")) {
                    mPresenter.getTreeError(treeBean.getErrorMsg());
                } else {
                    //添加数据
                    List<TreeData> treeDataList = new ArrayList<>();
                    List<TreeData.Children> childrenList = new ArrayList<>();
                    for (int i = 0; i < treeBean.getData().size(); i++) {
                        for (int j = 0; j < treeBean.getData().get(i).getChildren().size(); j++) {
                            childrenList.add(new TreeData.Children(treeBean.getData().get(i).getChildren().get(j).getName(),
                                    treeBean.getData().get(i).getChildren().get(j).getId()));
                        }
                        treeDataList.add(new TreeData(treeBean.getData().get(i).getName(),
                                childrenList));
                        childrenList = new ArrayList<>();
                    }
                    //发送
                    mPresenter.getTreeSuccess(treeDataList);
                }
            }

            @Override
            public void onError(Throwable e) {
                mPresenter.getTreeError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
