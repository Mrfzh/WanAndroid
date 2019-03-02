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
    private boolean isFromCollect;
    private boolean isHideCollect;

    private ShowArticleEvent(String link, String title, boolean isCollect, int id, int position, boolean isFromCollect, boolean isHideCollect) {
        this.link = link;
        this.title = title;
        this.isCollect = isCollect;
        this.id = id;
        this.position = position;
        this.isFromCollect = isFromCollect;
        this.isHideCollect = isHideCollect;
    }

    public ShowArticleEvent(String link, String title, boolean isCollect, int id, int position, boolean isFromCollect) {
        this(link, title, isCollect, id, position, isFromCollect, false);
    }

    public ShowArticleEvent(String link, String title) {
        this(link, title, false, -1, -1, false, true);
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

    public boolean isFromCollect() {
        return isFromCollect;
    }

    public void setFromCollect(boolean fromCollect) {
        isFromCollect = fromCollect;
    }

    public boolean isHideCollect() {
        return isHideCollect;
    }

    public void setHideCollect(boolean hideCollect) {
        isHideCollect = hideCollect;
    }
}
