package com.uof.uof_mobile.recyclerview;

import android.graphics.Bitmap;

public class OrderingItem {
    private String menu;        // 상품명
    private String subMenu;     // 하위메뉴
    private int price;          // 가격
    private int count;          // 동일한 상품에 대한 개수
    private Bitmap image;       // 이미지

    public OrderingItem(String menu, String subMenu, int price, int count, Bitmap image) {
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
        this.image = image;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    // OrderingItem(동일한 상품)의 개수와 개당 가격을 곱한 총 가격 반환
    public int getTotalPrice() {
        return count * price;
    }
}
