package com.uos.uos_mobile.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import com.uos.uos_mobile.adapter.OrderingAdapter;
import com.uos.uos_mobile.dialog.BasketDialog;
import com.uos.uos_mobile.dialog.SelectProductDialog;
import com.uos.uos_mobile.dialog.SelectSetDialog;
import com.uos.uos_mobile.item.OrderingProductItem;
import com.uos.uos_mobile.item.OrderingSetItem;
import com.uos.uos_mobile.manager.BasketManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderingActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnOrderingBack;
    private AppCompatTextView tvOrderingCompanyName;
    private ChipGroup cgOrderingCategoryList;
    private RecyclerView rvOrderingProductList;
    private LinearLayoutCompat llOrderingSelected;
    private AppCompatTextView tvOrderingTotalPrice;
    private AppCompatTextView tvOrderingProductCount;
    private LinearLayoutCompat llOrderingPay;
    private JSONObject companyData;
    private JSONArray categoryData;
    private OrderingAdapter orderingAdapter;
    private String selectedCategory;

    private String posAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_ordering);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        Global.basketManager.getOrderingItemArrayList().clear();
        super.onDestroy();
    }

    private void init() {
        for(Activity activity : Global.activities){
            if(activity instanceof OrderingActivity){
                activity.finish();
            }
        }
        Global.activities.add(this);

        ibtnOrderingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_ordering_back);
        tvOrderingCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_ordering_companyname);
        cgOrderingCategoryList = findViewById(com.uos.uos_mobile.R.id.cg_ordering_categorylist);
        rvOrderingProductList = findViewById(com.uos.uos_mobile.R.id.rv_ordering_productlist);
        llOrderingSelected = findViewById(com.uos.uos_mobile.R.id.ll_ordering_selected);
        tvOrderingTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_ordering_totalprice);
        tvOrderingProductCount = findViewById(com.uos.uos_mobile.R.id.tv_ordering_productcount);
        llOrderingPay = findViewById(com.uos.uos_mobile.R.id.ll_ordering_order);

        posAddress = getIntent().getStringExtra("posAddress");

        try {
            SharedPreferenceManager.open(OrderingActivity.this, Global.SharedPreference.APP_DATA);
            JSONObject message = new JSONObject(SharedPreferenceManager.load(Global.SharedPreference.TEMP_MESSAGE, ""));
            SharedPreferenceManager.save(Global.SharedPreference.TEMP_MESSAGE, "");
            SharedPreferenceManager.close();
            companyData = message.getJSONObject("company");
            categoryData = message.getJSONArray("category_list");
            tvOrderingCompanyName.setText(companyData.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OrderingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 상품 목록 Adapter 설정
        orderingAdapter = new OrderingAdapter();
        orderingAdapter.setJson(categoryData);
        rvOrderingProductList.setLayoutManager(new GridLayoutManager(OrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvOrderingProductList.setAdapter(orderingAdapter);
        Global.basketManager = new BasketManager(tvOrderingCompanyName.getText().toString());

        updatePriceInfo();

        // 카테고리를 chipgroup에 추가
        for (int loop = 0; loop < categoryData.length(); loop++) {
            Chip chip = (Chip) OrderingActivity.this.getLayoutInflater().inflate(com.uos.uos_mobile.R.layout.chip_category, cgOrderingCategoryList, false);
            try {
                chip.setText(categoryData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvOrderingProductList.setAdapter(orderingAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (loop == 0) {
                selectedCategory = chip.getText().toString();
                orderingAdapter.setSelectedCategory(selectedCategory);
                rvOrderingProductList.setAdapter(orderingAdapter);
                chip.setChecked(true);
            }
            cgOrderingCategoryList.addView(chip);
        }

        // 뒤로가기 버튼이 눌렸을 경우
        ibtnOrderingBack.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(OrderingActivity.this, com.uos.uos_mobile.R.style.AlertDialogTheme)
                    .setTitle("주문 취소")
                    .setMessage("주문창에서 나가시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                        finish();
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {
                    }).create();

            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            });
            alertDialog.show();
        });

        // 리스트 아이템이 눌렸을 경우
        orderingAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = orderingAdapter.getItem(position);

            if (orderingProductItem.getType() == Global.ItemType.PRODUCT) {
                // 선택된 아이템이 단일상품일 경우
                new SelectProductDialog(OrderingActivity.this, orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            } else {
                // 선택된 아이템이 세트상품일 경우
                new SelectSetDialog(OrderingActivity.this, (OrderingSetItem) orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            }
        });

        // 선택정보창 버튼이 눌렸을 경우
        llOrderingSelected.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(OrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                BasketDialog basketDialog = new BasketDialog(OrderingActivity.this, posAddress);
                basketDialog.setOnDismissListener(dialogInterface -> {
                    updatePriceInfo();
                });
                basketDialog.show();
            }
        });

        // 결제 버튼이 눌렸을 경우
        llOrderingPay.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(OrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(OrderingActivity.this, PayActivity.class);
                intent.putExtra("payAddress", posAddress);
                startActivity(intent);
            }
        });
    }

    private void updatePriceInfo() {
        ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvOrderingTotalPrice.getText().toString().replace(",", "")), Global.basketManager.getOrderPrice());
        va.setDuration(1000);
        va.addUpdateListener(va1 -> tvOrderingTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
        va.start();

        tvOrderingProductCount.setText(String.valueOf(Global.basketManager.getOrderCount()));

        if (Global.basketManager.getOrderCount() == 0) {
            llOrderingSelected.setEnabled(false);
            llOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llOrderingPay.setEnabled(false);
            llOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else {
            llOrderingSelected.setEnabled(true);
            llOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            llOrderingPay.setEnabled(true);
            llOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        }
    }
}