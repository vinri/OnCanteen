package com.example.campuscanteen;

public class FoodMenuModel {
    private static String foodMenuModel = "";

    public static String getFoodMenuModel() {
        return foodMenuModel;
    }

    public static void setFoodMenuModel(String foodMenuModel) {
        FoodMenuModel.foodMenuModel = foodMenuModel;
    }
}
