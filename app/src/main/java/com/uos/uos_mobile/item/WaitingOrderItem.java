package com.uos.uos_mobile.item;

import java.util.ArrayList;

public class WaitingOrderItem {
    private String companyName;
    private int orderCode;
    private String orderTime;
    private String state;
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    public WaitingOrderItem() {

    }

    public WaitingOrderItem(String companyName, int orderCode, String orderTime, String state, ArrayList<BasketItem> basketItemArrayList) {
        this.companyName = companyName;
        this.orderCode = orderCode;
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

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
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
