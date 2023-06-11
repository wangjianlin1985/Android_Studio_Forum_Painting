package com.chengxusheji.domain;

import java.sql.Timestamp;
public class PaintClass {
    /*绘画分类id*/
    private int paintClassId;
    public int getPaintClassId() {
        return paintClassId;
    }
    public void setPaintClassId(int paintClassId) {
        this.paintClassId = paintClassId;
    }

    /*绘画分类名称*/
    private String paintClassName;
    public String getPaintClassName() {
        return paintClassName;
    }
    public void setPaintClassName(String paintClassName) {
        this.paintClassName = paintClassName;
    }

}