package com.uof.uof_mobile.recyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.manager.UsefulFuncManager;

import org.json.JSONObject;

public class RestaurantOrderingProductItem {
    protected int type;
    protected boolean selected;
    protected int count;
    protected String name;
    protected int price;
    protected String desc;
    protected Bitmap image;

    public RestaurantOrderingProductItem(){

    }

    public RestaurantOrderingProductItem(JSONObject jsonObject) {
        this.type = Constants.ItemType.PRODUCT;
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

    public boolean isSelected() {
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
