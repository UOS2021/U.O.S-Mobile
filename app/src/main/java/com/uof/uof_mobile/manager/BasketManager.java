package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.listitem.OrderingItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class BasketManager {
    private ArrayList<OrderingItem> orderingItemArrayList = new ArrayList<>();

    public BasketManager() {

    }

    public ArrayList<OrderingItem> getOrderingItemArrayList() {
        return orderingItemArrayList;
    }

    public void setOrderingItemArrayList(ArrayList<OrderingItem> orderingItemArrayList) {
        this.orderingItemArrayList = orderingItemArrayList;
    }

    public void addItem(OrderingItem orderingItem) {
        boolean isExist = false;

        for (OrderingItem item : orderingItemArrayList) {
            if (orderingItem.getMenu().equals(item.getMenu()) && orderingItem.getSubMenu().equals(item.getSubMenu())) {
                isExist = true;
                item.setCount(item.getCount() + orderingItem.getCount());
                break;
            }
        }

        if (!isExist) {
            orderingItemArrayList.add(orderingItem);
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
        for (OrderingItem orderingItem : orderingItemArrayList) {
            count += orderingItem.getCount();
        }

        return count;
    }

    // 장바구니 내 모든 주문의 가격 총합
    public int getOrderPrice() {
        int price = 0;
        for (OrderingItem orderingItem : orderingItemArrayList) {
            price += orderingItem.getPrice() * orderingItem.getCount();
        }

        return price;
    }
}
