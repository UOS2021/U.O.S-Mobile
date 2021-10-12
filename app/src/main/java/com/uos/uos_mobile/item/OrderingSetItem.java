package com.uos.uos_mobile.item;

import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 세트 상품 정보를 담고 있는 클래스.<br><br>
 * U.O.S-Mobile에서 사용하는 세트 상품에 대한 클래스로 OrderingProductItem 클래스를 상속받고 있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class OrderingSetItem extends OrderingProductItem {
    /**
     * 세트가 포함하고 있는 상품 목록.
     */
    private JSONArray productList;

    /**
     * 세트 구성 설명.
     */
    private String conf;

    /**
     * OrderingSetItem의 기본 생성자.
     */
    public OrderingSetItem() {

    }

    /**
     * OrderingSetItem 명시적 생성자.<br><br>
     * 매개변수로 들어온 JSONObject 객체에서 세트 상품 정보를 추출하여 멤버 변수에 저장.
     *
     * @param jsonObject 세트 상품 정보를 담고 있는 JSONObject 객체.
     */
    public OrderingSetItem(JSONObject jsonObject) {
        super();
        this.type = Global.ItemType.SET;
        try {
            this.conf = jsonObject.getString("conf");
            this.productList = jsonObject.getJSONArray("category_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getProductList() {
        return productList;
    }

    public void setProductList(JSONArray productList) {
        this.productList = productList;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}
