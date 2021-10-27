package com.uos.uos_mobile.item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 선택된 상품을 장바구니에서 보관할 형식으로 변환한 클래스.<br><br>
 * OrderingProductItem을 상속받는 상품 객체 및 직접 구현한 상품 객체는 선택 후 장바구니에 추가하기 전
 * BasketItem의 형태에 맞게 변환해주어야 합니다. BasketItem은 장바구니에서 선택된 상품의 데이터를 표시할 때 일관된
 * 형식으로 보여주기 위해서 사용됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class BasketItem implements Serializable {
    /**
     * 상품 종류.
     */
    private int type;

    /**
     * 상품 대표명.<br>
     * 일반적으로 단일상품일 경우 상품명과 상품 대표명이 동일하며 ,세트 상품일 경우에는 세트 상품명이 상품 대표명과
     * 동일합니다. 상품 대표명은 상품을 대표하는 이름만 넣는 것이 추후 POS기에서 상품 확인 시 직관저긍로 볼 수
     * 있습니다. 추가적인 하위상품이나 설명이 필요하다면 submenu에 해당 내용을 넣는 것을 추천합니다.
     */
    private String menu;

    /**
     * 하위상품.<br>
     * 단일상품일 경우에는 추가할 하위메뉴가 없습니다. 하지만 세트와 같이 하위에 또 다른 상품이 포함되어있는 형태라면
     * 하위상품에 해당 데이터를 추가합니다. 예를 들어, 동일한 상품명을 가진 세트에서 하위상품이 다른 상품들을
     * 장바구니에서 구분하고자 할 때 사용할 수 있습니다.
     */
    private String subMenu;

    /**
     * 상품 한 개당 가격.
     */
    private int price;

    /**
     * 선택한 상품개수.
     */
    private int count;

    /**
     * 만약 현재 보유 정보를 저장해놓고 추후 주문선택화면에서 다시 보여줘야할 경우에는 아래와 같이 저장할 객체를 직접
     * 선언해둘 수 있습니다. 영화 아이템의 경우에는 해당 좌석을 선택 후 추후 다시 좌석을 불러올 시 선택된 좌석들을
     * 다시 보여줘야하기 때문에 아래와 같이 선언하였습니다.
     */
    private MovieItem movieItem;    // 영화 타입일 경우 사용하는 객체

    /**
     * BasketItem의 기본 생성자입니다.
     */
    public BasketItem() {

    }

    /**
     * BasketItem의 명시적 생성자입니다.
     *
     * @param type    상품 종류
     * @param menu    상품 대표명
     * @param subMenu 하위상품
     * @param price   상품의 개당 가격
     * @param count   선택한 상품 개수
     */
    public BasketItem(int type, String menu, String subMenu, int price, int count) {
        this.type = type;
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
    }

    /**
     * BasketItem의 명시적 생성자입니다.
     *
     * @param type      상품 종류
     * @param menu      상품 대표명
     * @param subMenu   하위상품
     * @param price     상품의 개당 가격
     * @param count     선택한 상품 개수
     * @param movieItem 선택한 영화객체(추후 선택한 좌석을 좌석선택화면에서 다시 보여주기 위함)
     */
    public BasketItem(int type, String menu, String subMenu, int price, int count, MovieItem movieItem) {
        this.type = type;
        this.menu = menu;
        this.subMenu = subMenu;
        this.price = price;
        this.count = count;
        this.movieItem = movieItem;
    }

    /**
     * BasketItem의 명시적 생성자입니다.
     *
     * @param productData 한 상품에 대한 주문 데이터.
     */
    public BasketItem(JSONObject productData) {
        try {
            this.type = productData.getInt("type");
            this.menu = productData.getString("menu");
            this.subMenu = productData.getString("submenu");
            this.count = productData.getInt("count");
            this.price = productData.getInt("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 선택한 상품의 개당 가격과 개수를 곱한 총 금액 반환합니다.
     *
     * @return 상품의 총 금액
     */
    public int getTotalPrice() {
        return count * price;
    }
}
