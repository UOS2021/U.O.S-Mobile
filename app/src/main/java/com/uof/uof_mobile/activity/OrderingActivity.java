package com.uof.uof_mobile.activity;

import android.animation.ValueAnimator;
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
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.OrderingAdapter;
import com.uof.uof_mobile.dialog.BasketDialog;
import com.uof.uof_mobile.dialog.SelectProductDialog;
import com.uof.uof_mobile.dialog.SelectSetDialog;
import com.uof.uof_mobile.item.OrderingProductItem;
import com.uof.uof_mobile.item.OrderingSetItem;
import com.uof.uof_mobile.manager.BasketManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        Global.basketManager.getOrderingItemArrayList().clear();
        if (Global.socketManager != null && Global.socketManager.isSocketConnected()) {
            Global.socketManager.disconnect();
        }
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);
        ibtnOrderingBack = findViewById(R.id.ibtn_ordering_back);
        tvOrderingCompanyName = findViewById(R.id.tv_ordering_companyname);
        cgOrderingCategoryList = findViewById(R.id.cg_ordering_categorylist);
        rvOrderingProductList = findViewById(R.id.rv_ordering_productlist);
        llOrderingSelected = findViewById(R.id.ll_ordering_selected);
        tvOrderingTotalPrice = findViewById(R.id.tv_ordering_totalprice);
        tvOrderingProductCount = findViewById(R.id.tv_ordering_productcount);
        llOrderingPay = findViewById(R.id.ll_ordering_order);
        Intent loadData = getIntent();

        try {
            companyData = new JSONObject(loadData.getStringExtra("companyData"));
            categoryData = new JSONArray(loadData.getStringExtra("categoryData"));
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
            Chip chip = (Chip) OrderingActivity.this.getLayoutInflater().inflate(R.layout.chip_category, cgOrderingCategoryList, false);
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
            AlertDialog alertDialog = new AlertDialog.Builder(OrderingActivity.this, R.style.AlertDialogTheme)
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
                BasketDialog basketDialog = new BasketDialog(OrderingActivity.this);
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
                startActivity(new Intent(OrderingActivity.this, PayActivity.class));
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
            llOrderingSelected.setBackgroundColor(getResources().getColor(R.color.gray));
            llOrderingPay.setEnabled(false);
            llOrderingPay.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            llOrderingSelected.setEnabled(true);
            llOrderingSelected.setBackgroundColor(getResources().getColor(R.color.color_primary));
            llOrderingPay.setEnabled(true);
            llOrderingPay.setBackgroundColor(getResources().getColor(R.color.color_primary));
        }
    }
}