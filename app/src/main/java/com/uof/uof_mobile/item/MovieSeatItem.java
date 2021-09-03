package com.uof.uof_mobile.item;

import org.json.JSONObject;

public class MovieSeatItem implements Cloneable {
    private String code;
    private int price;
    private int state;
    private boolean selected;

    public MovieSeatItem clone() throws CloneNotSupportedException {
        return (MovieSeatItem) super.clone();
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void changeSelected() {
        this.selected = !this.selected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCol() {
        return Integer.valueOf(code.substring(1)) - 1;
    }

    public int getRow() {
        char alphabet = code.charAt(0);

        return (alphabet - 65);
    }

    public void setMovieSeatItemFromJson(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("code");
            price = jsonObject.getInt("price");
            state = jsonObject.getInt("state");
            selected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
