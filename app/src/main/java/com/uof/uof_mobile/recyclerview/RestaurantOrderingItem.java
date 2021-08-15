package com.uof.uof_mobile.recyclerview;

public class RestaurantOrderingItem {
    protected int type;
    protected boolean selected;

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
