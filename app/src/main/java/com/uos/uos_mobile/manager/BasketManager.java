package com.uos.uos_mobile.manager;

import com.uos.uos_mobile.item.BasketItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 장바구니를 관리하는 Manager 클래스.<br><br>
 * <p>
 * BasketMangaer는 주문 Activity(ex. OrderingActivity, MovieOrderingActivity) 실행 시 생성되며 주문
 * Activity에서 선택한 상품들을 보관하는 장바구니 역할을 합니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class BasketManager implements Serializable {

    /**
     * 장바구니에 있는 모든 상품에 대한 목록.
     */
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    public ArrayList<BasketItem> getOrderingItemArrayList() {
        return basketItemArrayList;
    }

    public void setOrderingItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
    }

    /**
     * 매개변수로 전달된 BasketItem을 basketItemArrayList에 추가합니다. 만약 동일한 상품이 이미 장바구니에
     * 존재할 경우 해당 상품의 개수를 증가시깁니다.
     *
     * @param basketItem
     */
    public void addItem(BasketItem basketItem) {
        boolean isExist = false;

        for (BasketItem item : basketItemArrayList) {
            if (basketItem.getMenu().equals(item.getMenu()) && basketItem.getSubMenu().equals(item.getSubMenu())) {

                /* 만약 동일한 상품이 이미 장바구니에 존재할 경우 - 해당 상품의 개수 증가 */

                isExist = true;
                item.setCount(item.getCount() + basketItem.getCount());
                break;
            }
        }

        if (!isExist) {

            /* 동일한 상품이 장바구니에 존재하지 않을 경우 - 장바구니에 상품 추가 */

            basketItemArrayList.add(basketItem);
        }
    }

    /**
     * 장바구니에 들어있는 상품목록을 JSON 형식으로 반환합니다.
     *
     * @return JSONArray 장바구니 상품목록.
     */
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    /**
     * 장바구니 내 모든 상품의 개수를 반환합니다.
     *
     * @return int 모든 상품의 개수.
     */
    public int getOrderCount() {
        int count = 0;
        for (BasketItem basketItem : basketItemArrayList) {
            count += basketItem.getCount();
        }

        return count;
    }

    /**
     * 장바구니 내 모든 상품의 가격 총합을 반환합니다.
     *
     * @return int 모든 상품의 가격 총합.
     */
    public int getOrderPrice() {
        int price = 0;
        for (BasketItem basketItem : basketItemArrayList) {
            price += basketItem.getPrice() * basketItem.getCount();
        }

        return price;
    }
}
