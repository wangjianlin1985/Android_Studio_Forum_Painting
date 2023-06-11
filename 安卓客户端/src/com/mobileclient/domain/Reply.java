package com.mobileclient.domain;

import java.io.Serializable;

public class Reply implements Serializable {
    /*回复id*/
    private int replyId;
    public int getReplyId() {
        return replyId;
    }
    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    /*被回帖子*/
    private int postInfoObj;
    public int getPostInfoObj() {
        return postInfoObj;
    }
    public void setPostInfoObj(int postInfoObj) {
        this.postInfoObj = postInfoObj;
    }

    /*回复内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*回复人*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

    /*回复时间*/
    private String replyTime;
    public String getReplyTime() {
        return replyTime;
    }
    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

}