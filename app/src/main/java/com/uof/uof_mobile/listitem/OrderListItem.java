package com.uof.uof_mobile.listitem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListItem {

    private String date;
    private String companyname;
    private int price;
    private ArrayList<OrderListProductItem> orderListProductItemArrayList = new ArrayList<>();

    public OrderListItem() {

    }

    public OrderListItem(JSONObject jsonObject) {
        try {
            date = jsonObject.getString("date");
            companyname = jsonObject.getString("companyname");
            price = jsonObject.getInt("price");

            JSONArray jsonArray = jsonObject.getJSONArray("orderlist");
            for (int loop2 = 0; loop2 < jsonArray.length(); loop2++) {
                orderListProductItemArrayList.add(new OrderListProductItem(jsonArray.getJSONObject(loop2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStringProductItemList(){
        String result = "";
        for(OrderListProductItem orderListProductItem : orderListProductItemArrayList) {
            result += orderListProductItem.getName() + " X" + orderListProductItem.getCount() + "\n";
        }
        return result.substring(0,result.length()-1);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
