package com.mobileclient.domain;

import java.io.Serializable;

public class Paint implements Serializable {
    /*作品id*/
    private int paintId;
    public int getPaintId() {
        return paintId;
    }
    public void setPaintId(int paintId) {
        this.paintId = paintId;
    }

    /*作品名称*/
    private String paintName;
    public String getPaintName() {
        return paintName;
    }
    public void setPaintName(String paintName) {
        this.paintName = paintName;
    }

    /*绘画分类*/
    private int paintClassObj;
    public int getPaintClassObj() {
        return paintClassObj;
    }
    public void setPaintClassObj(int paintClassObj) {
        this.paintClassObj = paintClassObj;
    }

    /*作品图片*/
    private String paintPhoto;
    public String getPaintPhoto() {
        return paintPhoto;
    }
    public void setPaintPhoto(String paintPhoto) {
        this.paintPhoto = paintPhoto;
    }

    /*作品描述*/
    private String paintDesc;
    public String getPaintDesc() {
        return paintDesc;
    }
    public void setPaintDesc(String paintDesc) {
        this.paintDesc = paintDesc;
    }

    /*作品文件*/
    private String paintFile;
    public String getPaintFile() {
        return paintFile;
    }
    public void setPaintFile(String paintFile) {
        this.paintFile = paintFile;
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