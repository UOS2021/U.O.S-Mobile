package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.SelectProductDialog;
import com.uof.uof_mobile.manager.RestaurantBascketManager;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingAdapter;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class RestaurantOrderingActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnRestaurantOrderingBack;
    private AppCompatTextView tvRestaurantOrderingCompanyName;
    private ChipGroup cgRestaurantOrderingCategoryList;
    private RecyclerView rvRestaurantOrderingProductList;
    private LinearLayoutCompat llRestaurantOrderingSelected;
    private AppCompatTextView tvRestaurantOrderingPayAmount;
    private AppCompatTextView tvRestaurantOrderingSelectedCount;
    private LinearLayoutCompat llRestaurantOrderingPay;
    private JSONObject companyData;
    private JSONArray productData;

    private RestaurantOrderingAdapter restaurantOrderingAdapter;
    private RestaurantBascketManager restaurantBascketManager;

    private String tempJson = "{\"message\":{\"company\":{\"name\":\"맥도날드\"},\"category_list\":[{\"category\":\"테스트카테고리\",\"product_list\":[{\"name\":\"상품1\",\"price\":500,\"desc\":\"상품1입니다\",\"image\":\"imgdata\"},{\"name\":\"상품2\",\"price\":700,\"desc\":\"상품2입니다\",\"image\":\"imgdata\"},{\"name\":\"상품3\",\"price\":900,\"desc\":\"상품3입니다\",\"image\":\"imgdata\"},{\"name\":\"상품4\",\"price\":1000,\"desc\":\"상품4입니다\",\"image\":\"imgdata\"}],\"set_list\":[{\"name\":\"세트1\",\"price\":3000,\"desc\":\"세트1입니다\",\"image\":\"imgdata\",\"product_list\":[{\"name\":\"서브상품1\",\"price\":500,\"desc\":\"서브상품1입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품2\",\"price\":500,\"desc\":\"서브상품2입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품3\",\"price\":500,\"desc\":\"서브상품3입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품4\",\"price\":500,\"desc\":\"서브상품4입니다\",\"image\":\"imgdata\"}]}]}]}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantordering);

        init();
    }

    private void init() {
        ibtnRestaurantOrderingBack = findViewById(R.id.ibtn_restaurantordering_back);
        tvRestaurantOrderingCompanyName = findViewById(R.id.tv_restaurantordering_companyname);
        cgRestaurantOrderingCategoryList = findViewById(R.id.cg_restaurantordering_categorylist);
        rvRestaurantOrderingProductList = findViewById(R.id.rv_restaurantordering_productlist);
        llRestaurantOrderingSelected = findViewById(R.id.ll_restaurantordering_selected);
        tvRestaurantOrderingPayAmount = findViewById(R.id.tv_restaurantordering_payamount);
        tvRestaurantOrderingSelectedCount = findViewById(R.id.tv_restaurantordering_selectedcount);
        llRestaurantOrderingPay = findViewById(R.id.ll_restaurantordering_pay);

        tvRestaurantOrderingPayAmount.setText("0");
        tvRestaurantOrderingSelectedCount.setText("0");

        Intent loadData = getIntent();

        try {
            //companyData = new JSONObject(loadData.getStringExtra("companyData"));
            //productData = new JSONArray(loadData.getStringExtra("productData"));
            companyData = new JSONObject(tempJson).getJSONObject("message").getJSONObject("company");
            productData = new JSONObject(tempJson).getJSONObject("message").getJSONArray("category_list");
            tvRestaurantOrderingCompanyName.setText(companyData.getString("name"));
        } catch (Exception e) {
            Toast.makeText(RestaurantOrderingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 카테고리를 chipgroup에 추가
        for (int loop = 0; loop < productData.length(); loop++) {
            Chip chip = new Chip(RestaurantOrderingActivity.this);
            try {
                chip.setText(productData.getJSONObject(loop).getString("category"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            chip.setChipDrawable(ChipDrawable.createFromAttributes(RestaurantOrderingActivity.this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice));
            cgRestaurantOrderingCategoryList.addView(chip);
        }

        restaurantOrderingAdapter = new RestaurantOrderingAdapter();
        restaurantOrderingAdapter.setJson(productData);
        rvRestaurantOrderingProductList.setLayoutManager(new GridLayoutManager(RestaurantOrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvRestaurantOrderingProductList.setAdapter(restaurantOrderingAdapter);
        restaurantBascketManager = new RestaurantBascketManager();

        // 뒤로가기 버튼이 눌렸을 경우
        ibtnRestaurantOrderingBack.setOnClickListener(view -> {
            finish();
        });

        // 리스트 아이템이 눌렸을 경우
        restaurantOrderingAdapter.setOnItemClickListener((view, position) -> {
            RestaurantOrderingProductItem restaurantOrderingItem = restaurantOrderingAdapter.getItem(position);

            if (restaurantOrderingItem.getType() == Constants.ItemType.PRODUCT) {
                new SelectProductDialog(RestaurantOrderingActivity.this, restaurantOrderingItem, (count) -> {
                    int currentPayAmount = Integer.parseInt(tvRestaurantOrderingPayAmount.getText().toString().replace(",", ""));
                    int currentSelectedCount = Integer.parseInt(tvRestaurantOrderingSelectedCount.getText().toString());

                    restaurantBascketManager.addItem(restaurantOrderingItem, count);
                    tvRestaurantOrderingPayAmount.setText(new DecimalFormat("###,###").format(currentPayAmount + restaurantOrderingItem.getPrice() * count));
                    tvRestaurantOrderingSelectedCount.setText(String.valueOf(currentSelectedCount + count));
                }).show();
            }
        });

        // 선택정보창 버튼이 눌렸을 경우
        llRestaurantOrderingSelected.setOnClickListener(view -> {

        });

        // 결제 버튼이 눌렸을 경우
        llRestaurantOrderingPay.setOnClickListener(view -> {
            Intent intent = new Intent(RestaurantOrderingActivity.this, PayActivity.class);
            startActivity(intent);
        });
    }
}