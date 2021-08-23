package com.uof.uof_mobile.listitem;

public class BasketItem {
    private String menu;        // 상품명
    private String subMenu;     // 하위메뉴
    private int price;          // 가격
    private int count;          // 동일한 상품에 대한 개수

    public BasketItem(String menu, String subMenu, int price, int count) {
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
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

    // BasketItem(동일한 상품)의 개수와 개당 가격을 곱한 총 가격 반환
    public int getTotalPrice() {
        return count * price;
    }
}
