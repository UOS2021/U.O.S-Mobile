package com.uof.uof_mobile.recyclerview;

import com.uof.uof_mobile.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestaurantOrderingSetItem extends RestaurantOrderingProductItem {
    private JSONArray productList;

    public RestaurantOrderingSetItem(JSONObject jsonObject) {
        this.type = Constants.ItemType.SET;
        this.selected = false;
        this.count = 0;
        try {
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getInt("price");
            this.desc = jsonObject.getString("desc");
            this.image = jsonObject.getString("image");
            this.productList = jsonObject.getJSONArray("product_list");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getProductList() {
        return productList;
    }

    public void setProductList(JSONArray productList) {
        this.productList = productList;
    }
}
