package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/14
 */
public class ShowArticleEvent {
    private String link;
    private String title;
    private boolean isCollect;
    private int id;
    private int position;
    private int from;
    private boolean isHideCollect;

    public static final int FROM_COLLECT = 0;
    public static final int FROM_HOME = 1;
    public static final int FROM_TREE = 2;

    /**
     * @param link 文章链接
     * @param title 文章标题
     * @param isCollect 文章是否被收藏
     * @param id 文章id
     * @param position 位置
     * @param from 从哪个活动跳转而来
     * @param isHideCollect 是否隐藏收藏菜单
     */
    public ShowArticleEvent(String link, String title, boolean isCollect, int id, int position, int from, boolean isHideCollect) {
        this.link = link;
        this.title = title;
        this.isCollect = isCollect;
        this.id = id;
        this.position = position;
        this.from = from;
        this.isHideCollect = isHideCollect;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public boolean isHideCollect() {
        return isHideCollect;
    }

    public void setHideCollect(boolean hideCollect) {
        isHideCollect = hideCollect;
    }
}
