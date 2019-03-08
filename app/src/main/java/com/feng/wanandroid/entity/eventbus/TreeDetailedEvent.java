package com.feng.wanandroid.entity.eventbus;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/3/8
 */
public class TreeDetailedEvent {
    private String firstLevelCatalogName;
    private List<String> secondLevelCatalogNames;
    private List<Integer> secondLevelCatalogIds;

    public TreeDetailedEvent(String firstLevelCatalogName, List<String> secondLevelCatalogNames, List<Integer> secondLevelCatalogIds) {
        this.firstLevelCatalogName = firstLevelCatalogName;
        this.secondLevelCatalogNames = secondLevelCatalogNames;
        this.secondLevelCatalogIds = secondLevelCatalogIds;
    }

    public String getFirstLevelCatalogName() {
        return firstLevelCatalogName;
    }

    public void setFirstLevelCatalogName(String firstLevelCatalogName) {
        this.firstLevelCatalogName = firstLevelCatalogName;
    }

    public List<String> getSecondLevelCatalogNames() {
        return secondLevelCatalogNames;
    }

    public void setSecondLevelCatalogNames(List<String> secondLevelCatalogNames) {
        this.secondLevelCatalogNames = secondLevelCatalogNames;
    }

    public List<Integer> getSecondLevelCatalogIds() {
        return secondLevelCatalogIds;
    }

    public void setSecondLevelCatalogIds(List<Integer> secondLevelCatalogIds) {
        this.secondLevelCatalogIds = secondLevelCatalogIds;
    }
}
