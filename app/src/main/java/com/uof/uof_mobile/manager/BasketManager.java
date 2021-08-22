package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.listitem.BasketItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class BasketManager {
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    public BasketManager() {

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
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
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
}
