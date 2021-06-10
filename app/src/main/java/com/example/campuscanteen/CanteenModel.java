package com.example.campuscanteen;

import java.io.Serializable;

public class CanteenModel implements Serializable {

    private String CanteenId = "", CanteenName = "";

    public CanteenModel() {
    }

    public CanteenModel(String canteenId, String canteenName) {
        CanteenId = canteenId;
        CanteenName = canteenName;
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
}
