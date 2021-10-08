package com.uos.uos_mobile.item;

import java.util.ArrayList;

/**
 * 카테고리 정보를 담고 있는 클래스. <br><br>
 * OrderingCategoryItem은 특정 카테고리를 위한 클래스로 내부에는 카테고리에 포함되는 OrderingProductItem(상품
 * 정보)가 ArrayList 형태로 들어있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class OrderingCategoryItem {
    /**
     * 카테고리 명
     */
    private String category;

    /**
     * 카테고리에 포함되어있는 상품 목록.
     */
    private ArrayList<OrderingProductItem> orderingProductItemArrayList;

    /**
     * OrderingCategoryItem의 기본 생성자.
     */
    public OrderingCategoryItem(){

    }

    /**
     * OrderingCategoryItem의 명시적 생성자. <br><br>
     * @param category 카테고리 이름
     * @param orderingProductItemArrayList 카테고리가 포함하고 있는 상품에 대한 ArrayList
     */
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
