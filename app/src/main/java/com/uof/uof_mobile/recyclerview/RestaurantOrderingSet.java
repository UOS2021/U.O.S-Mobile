package com.uof.uof_mobile.recyclerview;

import com.uof.uof_mobile.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestaurantOrderingSet extends RestaurantOrderingItem {
    private String name;
    private int price;
    private String desc;
    private String image;
    private JSONArray productList;

    public RestaurantOrderingSet(JSONObject jsonObject){
        this.type = Constants.ItemType.SET;
        this.selected = false;
        try {
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getInt("price");
            this.desc = jsonObject.getString("desc");
            this.image = jsonObject.getString("image");
            this.productList = jsonObject.getJSONArray("product_list");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public JSONArray getProductList() {
        return productList;
    }

    public void setProductList(JSONArray productList) {
        this.productList = productList;
    }
}
