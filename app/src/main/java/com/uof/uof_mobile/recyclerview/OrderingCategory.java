package com.uof.uof_mobile.recyclerview;

import java.util.ArrayList;

public class OrderingCategory {
    private String category;
    private ArrayList<OrderingProductItem> orderingProductItemArrayList;

    public OrderingCategory(String category, ArrayList<OrderingProductItem> orderingProductItemArrayList){
        this.category = category;
        this.orderingProductItemArrayList = orderingProductItemArrayList;
    }

    public ArrayList<OrderingProductItem> getOrderingProductItemArrayList() {
        return orderingProductItemArrayList;
    }

    public void setOrderingProductItemArrayList(ArrayList<OrderingProductItem> orderingProductItemArrayList) {
        this.orderingProductItemArrayList = orderingProductItemArrayList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
