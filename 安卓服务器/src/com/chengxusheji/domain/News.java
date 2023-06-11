package com.chengxusheji.domain;

import java.sql.Timestamp;
public class News {
    /*新闻id*/
    private int newsId;
    public int getNewsId() {
        return newsId;
    }
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    /*标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*新闻内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*浏览量*/
    private int viewNum;
    public int getViewNum() {
        return viewNum;
    }
    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    /*发布时间*/
    private String publishDate;
    public String getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

}