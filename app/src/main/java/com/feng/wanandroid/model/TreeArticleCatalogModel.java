package com.feng.wanandroid.model;

import com.feng.wanandroid.base.BaseModel;
import com.feng.wanandroid.config.Constant;
import com.feng.wanandroid.contract.ITreeArticleCatalogContract;
import com.feng.wanandroid.entity.bean.HomeArticleBean;
import com.feng.wanandroid.entity.bean.TreeArticleBean;
import com.feng.wanandroid.entity.data.ArticleData;
import com.feng.wanandroid.http.RetrofitHelper;
import com.feng.wanandroid.http.api.TreeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/5
 */
public class TreeArticleCatalogModel extends BaseModel implements ITreeArticleCatalogContract.Model {

    private ITreeArticleCatalogContract.Presenter mPresenter;
    private TreeService mTreeService;
    private List<ArticleData> mArticleDataList = new ArrayList<>();

    public TreeArticleCatalogModel(ITreeArticleCatalogContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mTreeService = RetrofitHelper.getInstance().create(TreeService.class);
    }

    /**
     * 获取二级目录下的文章信息
     *
     * @param page 当前页数
     * @param catalogId 二级目录id
     */
    @Override
    public void getArticleInfo(int page, int catalogId) {
        execute(mTreeService.getTreeArticle(page, catalogId), new Observer<TreeArticleBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(TreeArticleBean treeArticleBean) {
                if (!treeArticleBean.getErrorMsg().equals("")) {
                    mPresenter.getArticleInfoError(treeArticleBean.getErrorMsg());
                } else {
                    List<TreeArticleBean.DataBean.DatasBean> datas = treeArticleBean.getData().getDatas();
                    if (datas.size() == 0) {
                        //如果没有数据了就返回null
                        mPresenter.getArticleInfoSuccess(null);
                    } else {
                        if (page == 0) {
                            mArticleDataList = new ArrayList<>();   //如果重新请求的话要重置集合
                        }
                        for (int i = 0; i < datas.size(); i++) {
                            mArticleDataList.add(new ArticleData(datas.get(i).getAuthor(), datas.get(i).getChapterName(),
                                    datas.get(i).isCollect(), datas.get(i).getLink(), datas.get(i).getNiceDate(),
                                    datas.get(i).getTitle(), datas.get(i).getId()));
                        }
                        mPresenter.getArticleInfoSuccess(mArticleDataList);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mPresenter.getArticleInfoError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
