package com.uos.uos_mobile.item;

import android.graphics.Bitmap;

import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class OrderingProductItem {
    protected int type;         // 상품 종류(단일상품, 세트상품)
    protected boolean selected; // 선택 여부
    protected int count;        // 개수
    protected String name;      // 상품명
    protected int price;        // 가격
    protected String desc;      // 설명
    protected Bitmap image;     // 이미지

    public OrderingProductItem() {

    }

    public OrderingProductItem(JSONObject jsonObject) {
        this.type = Global.ItemType.PRODUCT;
        this.selected = false;
        this.count = 0;
        try {
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getInt("price");
            this.desc = jsonObject.getString("desc");
            this.image = UsefulFuncManager.convertStringToBitmap(jsonObject.getString("image"));
        } catch (Exception e) {
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
