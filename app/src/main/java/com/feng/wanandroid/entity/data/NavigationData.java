package com.feng.wanandroid.entity.data;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/4/20
 */
public class NavigationData {
    private List<String> chapterNames;
    private List<ChapterData> chapterData;

    public NavigationData(List<String> chapterNames, List<ChapterData> chapterData) {
        this.chapterNames = chapterNames;
        this.chapterData = chapterData;
    }

    public List<String> getChapterNames() {
        return chapterNames;
    }

    public void setChapterNames(List<String> chapterNames) {
        this.chapterNames = chapterNames;
    }

    public List<ChapterData> getChapterData() {
        return chapterData;
    }

    public void setChapterData(List<ChapterData> chapterData) {
        this.chapterData = chapterData;
    }

    public static class ChapterData {
        private List<String> titles;
        private List<String> links;

        public ChapterData(List<String> titles, List<String> links) {
            this.titles = titles;
            this.links = links;
        }

        public List<String> getTitles() {
            return titles;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public List<String> getLinks() {
            return links;
        }

        public void setLinks(List<String> links) {
            this.links = links;
        }
    }
}
