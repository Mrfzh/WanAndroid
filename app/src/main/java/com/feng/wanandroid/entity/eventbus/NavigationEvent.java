package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2019/5/9
 */
public class NavigationEvent {
    private boolean isBackToTop;    //返回顶部

    public NavigationEvent(boolean isBackToTop) {
        this.isBackToTop = isBackToTop;
    }

    public boolean isBackToTop() {
        return isBackToTop;
    }

    public void setBackToTop(boolean backToTop) {
        isBackToTop = backToTop;
    }
}
