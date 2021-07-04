package com.example.campuscanteen;

import java.io.Serializable;

public class ModelCanteen implements Serializable {

    private String CanteenId = "", CanteenName = "",CanteenImgUrl="";

    public ModelCanteen() {
    }

    public ModelCanteen(String canteenId, String canteenName, String canteenImgUrl) {
        CanteenId = canteenId;
        CanteenName = canteenName;
        CanteenImgUrl = canteenImgUrl;
    }

    public String getCanteenId() {
        return CanteenId;
    }

    public void setCanteenId(String canteenId) {
        CanteenId = canteenId;
    }

    public String getCanteenName() {
        return CanteenName;
    }

    public void setCanteenName(String canteenName) {
        CanteenName = canteenName;
    }

    public String getCanteenImgUrl() {
        return CanteenImgUrl;
    }

    public void setCanteenImgUrl(String canteenImgUrl) {
        CanteenImgUrl = canteenImgUrl;
    }
}
