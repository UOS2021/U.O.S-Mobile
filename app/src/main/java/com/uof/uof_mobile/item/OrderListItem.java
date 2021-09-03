package com.uof.uof_mobile.item;

import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

import java.util.ArrayList;

public class OrderListItem {
    private ArrayList<BasketItem> basketItemArrayList;
    private String date;
    private String companyName;
    private int totalPrice = 0;

    public OrderListItem(String companyName, String date, ArrayList<BasketItem> basketItemArrayList) {
        this.companyName = companyName;
        this.date = date + " (" + UsefulFuncManager.getWeekDayFromDate(date) + ")";
        this.basketItemArrayList = basketItemArrayList;
        for (BasketItem basketItem : this.basketItemArrayList) {
            totalPrice += basketItem.getTotalPrice();
        }
    }

    public ArrayList<BasketItem> getBasketItemArrayList() {
        return basketItemArrayList;
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

    public void setBasketItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
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

        result += " 외 " + (totalItemCount - 1) + "개";

        return result;
    }
}
