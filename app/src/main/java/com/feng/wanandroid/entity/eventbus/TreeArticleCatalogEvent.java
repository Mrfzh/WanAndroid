package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/25
 */
public class TreeArticleCatalogEvent {
    private int position;   //文章位置
    private boolean isCollected;  //文章是否被收藏

    public TreeArticleCatalogEvent(int position, boolean isCollected) {
        this.position = position;
        this.isCollected = isCollected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
