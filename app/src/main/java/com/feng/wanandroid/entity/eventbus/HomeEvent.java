package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/14
 */
public class HomeEvent {
    private int position;
    private boolean isCollect;
    private boolean isRefresh;
    private boolean isBackToTop;

    private HomeEvent(int position, boolean isCollect, boolean isRefresh, boolean isBackToTop) {
        this.position = position;
        this.isCollect = isCollect;
        this.isRefresh = isRefresh;
        this.isBackToTop = isBackToTop;
    }

    /**
     * 用于更新单个item的收藏状态
     *
     * @param position item位置
     * @param isCollect 是否收藏
     */
    public HomeEvent(int position, boolean isCollect) {
        this(position, isCollect, false, false);
    }

    /**
     * 用于刷新adapter
     *
     * @param isRefresh 是否刷新
     */
    public HomeEvent(boolean isRefresh) {
        this(-1, false, isRefresh, false);
    }

    /**
     * RV返回顶部
     */
    public HomeEvent() {
        this(-1, false, false, true);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public boolean isBackToTop() {
        return isBackToTop;
    }

    public void setBackToTop(boolean backToTop) {
        isBackToTop = backToTop;
    }
}
