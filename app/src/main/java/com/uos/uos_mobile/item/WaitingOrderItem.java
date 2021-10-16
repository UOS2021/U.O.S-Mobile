package com.uos.uos_mobile.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaitingOrderItem {
    private String companyName;
    private String orderCode;
    private String orderTime;
    private String state;
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    public WaitingOrderItem() {

    }

    public WaitingOrderItem(JSONObject waitingOrder) {
        try {
            companyName = waitingOrder.getString("company_name");
            orderCode = waitingOrder.getString("order_code");
            orderTime = waitingOrder.getString("order_time");
            state = waitingOrder.getString("state");

            basketItemArrayList.clear();
            JSONArray orderData = waitingOrder.getJSONArray("order_data");
            for (int loop = 0; loop < orderData.length(); loop++) {
                JSONObject orderItem = orderData.getJSONObject(loop);
                basketItemArrayList.add(new BasketItem(orderItem.getInt("type"), orderItem.getString("menu"), orderItem.getString("submenu"), orderItem.getInt("price"), orderItem.getInt("count")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
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
