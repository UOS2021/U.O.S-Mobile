package com.uof.uof_mobile.listitem;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderListProductItem {
    private String name;
    private int count;

    public OrderListProductItem(JSONObject jsonObject){
        try {
            name = jsonObject.getString("name");
            count = jsonObject.getInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
