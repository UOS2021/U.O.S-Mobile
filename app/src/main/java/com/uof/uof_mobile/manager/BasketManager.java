package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.listitem.BasketItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BasketManager {
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();
    private String companyName;

    public BasketManager(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<BasketItem> getOrderingItemArrayList() {
        return basketItemArrayList;
    }

    public void setOrderingItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
    }

    public void addItem(BasketItem basketItem) {
        boolean isExist = false;

        for (BasketItem item : basketItemArrayList) {
            if (basketItem.getMenu().equals(item.getMenu()) && basketItem.getSubMenu().equals(item.getSubMenu())) {
                isExist = true;
                item.setCount(item.getCount() + basketItem.getCount());
                break;
            }
        }

        if (!isExist) {
            basketItemArrayList.add(basketItem);
        }
    }

    // 장바구니에 들어있는 상품목록을 JSON 형식으로 반환
    public JSONArray getJson() {
        JSONArray jsonArray = new JSONArray();

        try {
            for (BasketItem basketItem : basketItemArrayList) {
                JSONObject item = new JSONObject();
                item.accumulate("type", basketItem.getType());
                item.accumulate("menu", basketItem.getMenu());
                item.accumulate("submenu", basketItem.getSubMenu());
                item.accumulate("count", basketItem.getCount());
                item.accumulate("price", basketItem.getPrice());
                jsonArray.put(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    // 장바구니 내 모든 주문의 개수 총합
    public int getOrderCount() {
        int count = 0;
        for (BasketItem basketItem : basketItemArrayList) {
            count += basketItem.getCount();
        }

        return count;
    }

    // 장바구니 내 모든 주문의 가격 총합
    public int getOrderPrice() {
        int price = 0;
        for (BasketItem basketItem : basketItemArrayList) {
            price += basketItem.getPrice() * basketItem.getCount();
        }

        return price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
