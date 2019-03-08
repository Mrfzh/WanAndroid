package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.contract.ITreeArticleCatalogContract;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogModel extends BaseModel implements ITreeArticleCatalogContract.Model {

    private ITreeArticleCatalogContract.Presenter mPresenter;

    public TreeArticleCatalogModel(ITreeArticleCatalogContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 获取二级目录下的文章信息
     *
     * @param page 当前页数
     * @param catalogId 二级目录id
     */
    @Override
    public void getArticleInfo(int page, int catalogId) {

    }
}
