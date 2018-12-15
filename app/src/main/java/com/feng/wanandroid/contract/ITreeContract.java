package com.feng.wanandroid.contract;

import com.feng.wanandroid.entity.data.TreeData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/15
 */
public interface ITreeContract {
    interface View {
        void getTreeSuccess(List<TreeData> treeDataList);
        void getTreeError(String errorMsg);
    }
    interface Presenter {
        void getTreeSuccess(List<TreeData> treeDataList);
        void getTreeError(String errorMsg);
        void getTree();
    }
    interface Model {
        void getTree();
    }
}
