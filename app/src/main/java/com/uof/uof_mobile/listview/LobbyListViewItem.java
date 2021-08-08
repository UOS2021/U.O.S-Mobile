package com.uof.uof_mobile.listview;

import org.json.JSONArray;

public class LobbyListViewItem {
    private int ordernum; // 주문번호
    private JSONArray menulist; // ex) [{name : "productname",count : 3}]

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
    public LobbyListViewItem(int ordernum, JSONArray menulist){
        this.ordernum = ordernum;
        this.menulist = menulist;
    }

}
