package com.mobileclient.domain;

import java.io.Serializable;

public class Article implements Serializable {
    /*文章id*/
    private int articleId;
    public int getArticleId() {
        return articleId;
    }
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    /*标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*文章分类*/
    private int articleClassObj;
    public int getArticleClassObj() {
        return articleClassObj;
    }
    public void setArticleClassObj(int articleClassObj) {
        this.articleClassObj = articleClassObj;
    }

    /*内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*点击率*/
    private int hitNum;
    public int getHitNum() {
        return hitNum;
    }
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /*发布用户*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

    /*发布时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}