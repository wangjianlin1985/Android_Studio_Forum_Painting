package com.mobileclient.domain;

import java.io.Serializable;

public class PaintClass implements Serializable {
    /*�滭����id*/
    private int paintClassId;
    public int getPaintClassId() {
        return paintClassId;
    }
    public void setPaintClassId(int paintClassId) {
        this.paintClassId = paintClassId;
    }

    /*�滭��������*/
    private String paintClassName;
    public String getPaintClassName() {
        return paintClassName;
    }
    public void setPaintClassName(String paintClassName) {
        this.paintClassName = paintClassName;
    }

}