package com.uof.uof_mobile.item;

public class BasketItem {
    private int type;           // 상품 종류
    private String menu;        // 상품명
    private String subMenu;     // 하위메뉴
    private int price;          // 가격
    private int count;          // 동일한 상품에 대한 개수
    private MovieItem movieItem;    // 영화 타입일 경우 사용하는 객체

    public BasketItem(int type, String menu, String subMenu, int price, int count) {
        this.type = type;
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
    }

    public BasketItem(int type, String menu, String subMenu, int price, int count, MovieItem movieItem) {
        this.type = type;
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
        this.movieItem = movieItem;
    }

    public MovieItem getMovieItem() {
        return movieItem;
    }

    public void setMovieItem(MovieItem movieItem) {
        this.movieItem = movieItem;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
