package com.uof.uof_mobile.listitem;

import java.util.ArrayList;

public class WaitingOrderItem {
    private String companyName;
    private int orderNumber;
    private String orderTime;
    private String state;
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    public WaitingOrderItem() {

    }

    public WaitingOrderItem(String companyName, int orderNumber, String orderTime, String state, ArrayList<BasketItem> basketItemArrayList) {
        this.companyName = companyName;
        this.orderNumber = orderNumber;
        this.orderTime = orderTime;
        this.state = state;
        this.basketItemArrayList = basketItemArrayList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public ArrayList<BasketItem> getBasketItemArrayList() {
        return basketItemArrayList;
    }

    public void setBasketItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
