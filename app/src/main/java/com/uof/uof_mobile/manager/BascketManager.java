package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.recyclerview.OrderingProductItem;

import java.util.ArrayList;

public class BascketManager {
    private ArrayList<OrderingProductItem> orderingItemArrayList = new ArrayList<>();

    public BascketManager(){

    }

    public int getCount(){
        return orderingItemArrayList.size();
    }

    public boolean addItem(OrderingProductItem restaurantOrderingItem, int count){
        for(int loop = 0; loop < orderingItemArrayList.size(); loop++){

        }

        for(int loop = 0; loop < count; loop++){
            orderingItemArrayList.add(restaurantOrderingItem);
        }

        return true;
    }

    public boolean removeItem(OrderingProductItem orderingItemArrayList, int count){
        return false;
    }

    public String getJson(){
        return "";
    }
}
