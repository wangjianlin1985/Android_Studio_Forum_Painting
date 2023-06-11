package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Article {
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
    private ArticleClass articleClassObj;
    public ArticleClass getArticleClassObj() {
        return articleClassObj;
    }
    public void setArticleClassObj(ArticleClass articleClassObj) {
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
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
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