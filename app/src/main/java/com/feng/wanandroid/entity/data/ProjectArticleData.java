package com.feng.wanandroid.entity.data;

/**
 * @author Feng Zhaohao
 * Created on 2019/7/8
 */
public class ProjectArticleData {
    private String title;       //文章标题
    private String desc;        //文章描述
    private String author;      //文章作者
    private String date;        //发表时间
    private String link;        //文章链接
    private String picture;     //文章图片

    public ProjectArticleData(String title, String desc, String author,
                              String date, String link, String picture) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.date = date;
        this.link = link;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "title = " + title + ", desc = " + desc;
    }
}
