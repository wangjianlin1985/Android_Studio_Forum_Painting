package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Paint {
    /*��Ʒid*/
    private int paintId;
    public int getPaintId() {
        return paintId;
    }
    public void setPaintId(int paintId) {
        this.paintId = paintId;
    }

    /*��Ʒ����*/
    private String paintName;
    public String getPaintName() {
        return paintName;
    }
    public void setPaintName(String paintName) {
        this.paintName = paintName;
    }

    /*�滭����*/
    private PaintClass paintClassObj;
    public PaintClass getPaintClassObj() {
        return paintClassObj;
    }
    public void setPaintClassObj(PaintClass paintClassObj) {
        this.paintClassObj = paintClassObj;
    }

    /*��ƷͼƬ*/
    private String paintPhoto;
    public String getPaintPhoto() {
        return paintPhoto;
    }
    public void setPaintPhoto(String paintPhoto) {
        this.paintPhoto = paintPhoto;
    }

    /*��Ʒ����*/
    private String paintDesc;
    public String getPaintDesc() {
        return paintDesc;
    }
    public void setPaintDesc(String paintDesc) {
        this.paintDesc = paintDesc;
    }

    /*��Ʒ�ļ�*/
    private String paintFile;
    public String getPaintFile() {
        return paintFile;
    }
    public void setPaintFile(String paintFile) {
        this.paintFile = paintFile;
    }

    /*�����*/
    private int hitNum;
    public int getHitNum() {
        return hitNum;
    }
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /*�����û�*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*����ʱ��*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}