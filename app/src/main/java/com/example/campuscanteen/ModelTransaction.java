package com.example.campuscanteen;

import java.io.Serializable;

public class ModelTransaction implements Serializable {

    private String canteenId="", userId="", totalBayar="", transactionId="", status="";

    public ModelTransaction() {
    }

    public ModelTransaction(String canteenId, String userId, String totalBayar, String transactionId, String status) {
        this.canteenId = canteenId;
        this.userId = userId;
        this.totalBayar = totalBayar;
        this.transactionId = transactionId;
        this.status = status;
    }

    public String getCanteenId() {
        return canteenId;
    }

    public void setCanteenId(String canteenId) {
        this.canteenId = canteenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(String totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
