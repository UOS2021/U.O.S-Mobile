package com.uos.uos_mobile.item;

import android.graphics.Bitmap;

import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 상품 정보를 담고 있는 클래스 <br><br>
 * U.O.S-Mobile에서 사용하는 상품에 대한 최상위 클래스로 추가적인 데이터를 포함한 새로운 상품 클래스를 만들고 싶을
 * 경우에는 본 클래스를 상속받아 구현해야 합니다. 클래스를 상속받을 경우 생성자에서 type을 지정해줘야 하며 해당
 * type은 Global.ItemType 클래스에 선언 후 사용합니다. type은 추후 상품 표시 목록에서 상품 형식을 구분할 떄 사용
 * 됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class OrderingProductItem {
    /**
     * 상품 종류(단일상품, 세트상품).
     */
    protected int type;

    /**
     * 상품 선택 상태.
     */
    protected boolean selected;

    /**
     * 선택된 상품 개수.
     */
    protected int count;

    /**
     * 상품명.
     */
    protected String name;

    /**
     * 상품 가격.
     */
    protected int price;

    /**
     * 상품 설명.
     */
    protected String desc;

    /**
     * 상품 이미지.
     */
    protected Bitmap image;

    /**
     * OrderingProductItem의 기본 생성자.
     */
    public OrderingProductItem() {

    }

    /**
     * OrderingProductItem의 명시적 생성자 <br><br>
     * 매개변수로 들어온 JSONObject 객체에서 상품 정보를 추출하여 멤버 변수에 저장.
     *
     * @param jsonObject 상품정보를 담고 있는 JSONObject 객체
     */
    public OrderingProductItem(JSONObject jsonObject) {
        this.type = Global.ItemType.PRODUCT;
        this.selected = false;
        this.count = 0;
        try {
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getInt("price");
            this.desc = jsonObject.getString("desc");
            this.image = UsefulFuncManager.convertStringToBitmap(jsonObject.getString("image"));
        } catch (JSONException e) {
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
