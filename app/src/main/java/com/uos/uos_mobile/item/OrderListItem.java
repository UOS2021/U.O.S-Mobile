package com.uos.uos_mobile.item;

import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import java.util.ArrayList;

public class OrderListItem {
    private ArrayList<BasketItem> basketItemArrayList;
    private String date;
    private String time;
    private String companyName;
    private int totalPrice = 0;

    public OrderListItem(String companyName, String date, ArrayList<BasketItem> basketItemArrayList) {
        this.companyName = companyName;
        this.date = date.substring(0, 10) + " (" + UsefulFuncManager.getWeekDayFromDate(date) + ")";
        this.time = date.substring(11);
        this.basketItemArrayList = basketItemArrayList;
        for (BasketItem basketItem : this.basketItemArrayList) {
            totalPrice += basketItem.getTotalPrice();
        }
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
            result += " 외 " + (totalItemCount - 1) + "개";
        }

        return result;
    }
}
