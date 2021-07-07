package com.example.campuscanteen;

import java.io.Serializable;

public class ModelMenu implements Serializable {

    private String foodName ="", menuId ="",price="",menuUrl="";

    public ModelMenu(){

    }

    public ModelMenu(String foodName, String menuId, String price, String menuUrl) {
        this.foodName = foodName;
        this.menuId = menuId;
        this.price = price;
        this.menuUrl = menuUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
}
