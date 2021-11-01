package com.uos.uos_mobile.item;

import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderItem {
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();
    private String date;
    private String companyName;
    private int state;
    private int orderCode;
    private int totalPrice;

    public OrderItem(JSONObject orderData) {
        try {
            state = orderData.getInt("state");
            date = orderData.getString("date");
            companyName = orderData.getString("company_name");
            totalPrice = orderData.getInt("total_price");
            orderCode = orderData.getInt("order_code");

            JSONArray productList = orderData.getJSONArray("product_list");
            for (int loop = 0; loop < productList.length(); loop++) {
                basketItemArrayList.add(new BasketItem(productList.getJSONObject(loop)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public ArrayList<BasketItem> getBasketItemArrayList() {
        return basketItemArrayList;
    }

    public void setBasketItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderSimple() {
        String result = "";

        int maximumPrice = 0;
        int totalItemCount = 0;

        for (BasketItem basketItem : basketItemArrayList) {
            if (maximumPrice < basketItem.getPrice()) {
                maximumPrice = basketItem.getPrice();
                if (basketItem.getType() == Global.ItemType.MOVIE_TICKET) {
                    result = basketItem.getMenu().replace("&", " - ");
                } else {
                    result = basketItem.getMenu();
                }
            }
            totalItemCount += basketItem.getCount();
        }

        if (totalItemCount > 1) {
            if (basketItemArrayList.size() == 1) {
                result += " " + totalItemCount + "개";
            } else {
                result += " 외 " + (totalItemCount - 1) + "개";
            }
        }

        return result;
    }
}
