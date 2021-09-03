package com.uof.uof_mobile.item;

import java.util.ArrayList;

public class OrderingCategoryItem {
    private String category;                                                // 카테고리 명
    private ArrayList<OrderingProductItem> orderingProductItemArrayList;    // 카테고리에 포함되어있는 상품 목록

    public OrderingCategoryItem(String category, ArrayList<OrderingProductItem> orderingProductItemArrayList) {
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
