package com.feng.wanandroid.entity;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/11
 */
public class HomeArticleData {

    private String author;
    private String chapterName;
    private boolean collect;
    private String link;
    private String niceDate;
    private String title;

    public HomeArticleData(String author, String chapterName, boolean collect, String link, String niceDate, String title) {
        this.author = author;
        this.chapterName = chapterName;
        this.collect = collect;
        this.link = link;
        this.niceDate = niceDate;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
