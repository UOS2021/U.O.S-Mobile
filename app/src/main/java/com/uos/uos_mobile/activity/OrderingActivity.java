package com.uos.uos_mobile.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

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
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 매장에서 주문가능한 상품목록을 보여주는 Activity.<br>
 * xml: activity_ordering.xml<br><br>
 * <p>
 * OrderingActivity는 가장 기본적인 형태의 주문가능 목록을 보여주는 Activity입니다. 본 Activity는 음식점,
 * PC방과 같이 단일 상품, 세트 상품을 판매하는 매장에서 사용하기에 적절합니다. UOS에서 제공하는 OrderingActivity의
 * 확장으로는 MovieOrderingActivity가 있으며 해당 Activity는 특정 자리를 예매할 수 있는 기능이 추가되어있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class OrderingActivity extends UosActivity {

    /**
     * OrderingActivity를 종료하는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnOrderingBack;

    /**
     * 매장명(회사명)을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvOrderingCompanyName;

    /**
     * 카테고리를 목록이 표시되어있는 ChipGroup.
     */
    private ChipGroup cgOrderingCategoryList;

    /**
     * 상품목록이 표시되는 RecyclerView.
     */
    private RecyclerView rvOrderingProductList;

    /**
     * 터치 시 장바구니 화면으로 이동하는 LinearLayoutCompat.
     */
    private LinearLayoutCompat llOrderingBasket;

    /**
     * 선택한 상품의 총 금액을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvOrderingTotalPrice;

    /**
     * 선택한 상품의 총 개수를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvOrderingProductCount;

    /**
     * 터치 시 PayActivity로 넘어가는 LinearLayoutCompat.
     */
    private LinearLayoutCompat llOrderingPay;

    /**
     * 주문가능한 상품목록에 대한 정보를 관리하는 OrderingAdapter.
     */
    private OrderingAdapter orderingAdapter;

    /**
     * 현재 선택된 카테고리명을 저장하는 변수.
     */
    private String selectedCategory;

    /**
     * 선택한 상품들을 저장하고 관리하는 BasketManager.
     */
    private BasketManager basketManager;

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(OrderingActivity.this, com.uos.uos_mobile.R.style.AlertDialogTheme)
                .setTitle("주문 취소")
                .setMessage("주문창에서 나가시겠습니까?")
                .setPositiveButton("확인", (dialogInterface, i) -> {
                    super.onBackPressed();
                })
                .setNegativeButton("취소", (dialogInterface, i) -> {
                }).create();

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        });
        alertDialog.show();
    }

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_ordering);

        ibtnOrderingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_ordering_back);
        tvOrderingCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_ordering_companyname);
        cgOrderingCategoryList = findViewById(com.uos.uos_mobile.R.id.cg_ordering_categorylist);
        rvOrderingProductList = findViewById(com.uos.uos_mobile.R.id.rv_ordering_productlist);
        llOrderingBasket = findViewById(com.uos.uos_mobile.R.id.ll_ordering_basket);
        tvOrderingTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_ordering_totalprice);
        tvOrderingProductCount = findViewById(com.uos.uos_mobile.R.id.tv_ordering_productcount);
        llOrderingPay = findViewById(com.uos.uos_mobile.R.id.ll_ordering_order);

        try {
            SharedPreferencesManager.open(OrderingActivity.this, Global.SharedPreference.APP_DATA);
            JSONObject message = new JSONObject((String) SharedPreferencesManager.load(Global.SharedPreference.TEMP_MESSAGE, ""));
            SharedPreferencesManager.save(Global.SharedPreference.TEMP_MESSAGE, "");
            SharedPreferencesManager.close();
            JSONArray categoryData = message.getJSONArray("category_list");
            tvOrderingCompanyName.setText(message.getJSONObject("company").getString("name"));

            /* 상품 목록 Adapter 설정 */
            orderingAdapter = new OrderingAdapter();
            orderingAdapter.setJson(categoryData);
            rvOrderingProductList.setLayoutManager(new GridLayoutManager(OrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
            rvOrderingProductList.setAdapter(orderingAdapter);
            basketManager = new BasketManager();

            updatePriceInfo();

            /* 카테고리를 chipgroup에 추가 */
            for (int loop = 0; loop < categoryData.length(); loop++) {
                Chip chip = (Chip) OrderingActivity.this.getLayoutInflater().inflate(com.uos.uos_mobile.R.layout.chip_category, cgOrderingCategoryList, false);

                chip.setText(categoryData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvOrderingProductList.setAdapter(orderingAdapter);
                });

                if (loop == 0) {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvOrderingProductList.setAdapter(orderingAdapter);
                    chip.setChecked(true);
                }
                cgOrderingCategoryList.addView(chip);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(OrderingActivity.this, "상품목록을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* 뒤로가기 버튼이 눌렸을 경우 */
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

        /* 리스트 아이템이 눌렸을 경우 */
        orderingAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = orderingAdapter.getItem(position);

            if (orderingProductItem.getType() == Global.ItemType.PRODUCT) {

                /* 선택된 아이템이 단일상품일 경우 */

                new SelectProductDialog(OrderingActivity.this, orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            } else {

                /* 선택된 아이템이 세트상품일 경우 */

                new SelectSetDialog(OrderingActivity.this, (OrderingSetItem) orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            }
        });

        /* 선택정보창 버튼이 눌렸을 경우 */
        llOrderingBasket.setOnClickListener(view -> {
            BasketDialog basketDialog = new BasketDialog(OrderingActivity.this, getIntent().getStringExtra("uosPartnerId"), basketManager, tvOrderingCompanyName.getText().toString());
            basketDialog.setOnDismissListener(dialogInterface -> {
                updatePriceInfo();
            });
            basketDialog.show();
        });

        /* 결제 버튼이 눌렸을 경우 */
        llOrderingPay.setOnClickListener(view -> {
            Intent intent = new Intent(OrderingActivity.this, PayActivity.class);
            intent.putExtra("uosPartnerId", getIntent().getStringExtra("uosPartnerId"));
            intent.putExtra("basketManager", basketManager);
            intent.putExtra("companyName", tvOrderingCompanyName.getText().toString());
            startActivity(intent);
        });
    }

    /**
     * 상품 선택 또는 선택해제 시 선택 상품 정보(가격 및 개수)를 갱신합니다.
     */
    private void updatePriceInfo() {
        ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvOrderingTotalPrice.getText().toString().replace(",", "")), basketManager.getOrderPrice());
        va.setDuration(1000);
        va.addUpdateListener(va1 -> tvOrderingTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
        va.start();

        tvOrderingProductCount.setText(String.valueOf(basketManager.getOrderCount()));

        if (basketManager.getOrderCount() == 0) {

            /* 장바구니에 담긴 상품이 없을 경우 */

            llOrderingBasket.setEnabled(false);
            llOrderingBasket.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llOrderingPay.setEnabled(false);
            llOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else {

            /* 장바구니에 담긴 상품이 있을 경우 */

            llOrderingBasket.setEnabled(true);
            llOrderingBasket.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            llOrderingPay.setEnabled(true);
            llOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        }
    }
}