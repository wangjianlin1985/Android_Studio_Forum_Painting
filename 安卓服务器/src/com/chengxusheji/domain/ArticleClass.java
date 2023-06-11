package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ArticleClass {
    /*文章分类id*/
    private int articleClassId;
    public int getArticleClassId() {
        return articleClassId;
    }
    public void setArticleClassId(int articleClassId) {
        this.articleClassId = articleClassId;
    }

    /*文章分类名称*/
    private String articleClassName;
    public String getArticleClassName() {
        return articleClassName;
    }
    public void setArticleClassName(String articleClassName) {
        this.articleClassName = articleClassName;
    }

}