package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.recyclerview.RestaurantOrderingProductItem;

import java.util.ArrayList;

public class RestaurantBascketManager {
    private ArrayList<RestaurantOrderingProductItem> restaurantOrderingItemArrayList = new ArrayList<>();

    public RestaurantBascketManager(){

    }

    public int getCount(){
        return restaurantOrderingItemArrayList.size();
    }

    public boolean addItem(RestaurantOrderingProductItem restaurantOrderingItem, int count){
        for(int loop = 0; loop < restaurantOrderingItemArrayList.size(); loop++){

        }

        for(int loop = 0; loop < count; loop++){
            restaurantOrderingItemArrayList.add(restaurantOrderingItem);
        }

        return true;
    }

    public boolean removeItem(RestaurantOrderingProductItem restaurantOrderingItem, int count){
        return false;
    }

    public String getJson(){
        return "";
    }
}
