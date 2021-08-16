package com.uof.uof_mobile.recyclerview;

import java.util.ArrayList;

public class RestaurantOrderingCategory {
    private String category;
    private ArrayList<RestaurantOrderingProductItem> restaurantOrderingProductItemArrayList;

    public RestaurantOrderingCategory(String category, ArrayList<RestaurantOrderingProductItem> restaurantOrderingProductItemArrayList){
        this.category = category;
        this.restaurantOrderingProductItemArrayList = restaurantOrderingProductItemArrayList;
    }

    public ArrayList<RestaurantOrderingProductItem> getRestaurantOrderingProductItemArrayList() {
        return restaurantOrderingProductItemArrayList;
    }

    public void setRestaurantOrderingProductItemArrayList(ArrayList<RestaurantOrderingProductItem> restaurantOrderingProductItemArrayList) {
        this.restaurantOrderingProductItemArrayList = restaurantOrderingProductItemArrayList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
