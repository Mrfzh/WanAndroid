package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/14
 */
public class HomeEvent {
    private int position;
    private boolean isCollect;
    private boolean isRefresh;

    private HomeEvent(int position, boolean isCollect, boolean isRefresh) {
        this.position = position;
        this.isCollect = isCollect;
        this.isRefresh = isRefresh;
    }

    /**
     * 用于更新单个item的收藏状态
     *
     * @param position item位置
     * @param isCollect 是否收藏
     */
    public HomeEvent(int position, boolean isCollect) {
        this(position, isCollect, false);
    }

    /**
     * 用于刷新adapter，不关心其他
     *
     * @param isRefresh 是否刷新
     */
    public HomeEvent(boolean isRefresh) {
        this(-1, false, isRefresh);
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
}
