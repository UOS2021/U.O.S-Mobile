package com.uof.uof_mobile.item;

import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderingSetItem extends OrderingProductItem {
    private JSONArray productList;  // 세트 내 포함 상품 목록
    private String conf;            // 세트 구성

    public OrderingSetItem(JSONObject jsonObject) {
        this.type = Global.ItemType.SET;
        this.selected = false;
        this.count = 0;
        try {
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getInt("price");
            this.conf = jsonObject.getString("conf");
            this.desc = jsonObject.getString("desc");
            this.image = UsefulFuncManager.convertStringToBitmap(jsonObject.getString("image"));
            this.productList = jsonObject.getJSONArray("category_list");
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

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public static class LobbyListViewItem {
        private int ordernum; // 주문번호
        private JSONArray menulist; // ex) [{name : "productname",count : 3}]

        public LobbyListViewItem(int ordernum, JSONArray menulist) {
            this.ordernum = ordernum;
            this.menulist = menulist;
        }

        public int getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(int ordernum) {
            this.ordernum = ordernum;
        }

        public JSONArray getMenulist() {
            return menulist;
        }

        public void setMenulist(JSONArray menulist) {
            this.menulist = menulist;
        }

    }
}
