package com.uos.uos_mobile.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import com.uos.uos_mobile.adapter.MovieOrderingAdapter;
import com.uos.uos_mobile.adapter.OrderingAdapter;
import com.uos.uos_mobile.dialog.BasketDialog;
import com.uos.uos_mobile.dialog.SelectProductDialog;
import com.uos.uos_mobile.dialog.SelectSeatDialog;
import com.uos.uos_mobile.dialog.SelectSetDialog;
import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.MovieItem;
import com.uos.uos_mobile.item.OrderingProductItem;
import com.uos.uos_mobile.item.OrderingSetItem;
import com.uos.uos_mobile.manager.BasketManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieOrderingActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnMovieOrderingBack;
    private AppCompatTextView tvMovieOrderingCompanyName;
    private AppCompatTextView tvMovieOrderingShowMovie;
    private AppCompatTextView tvMovieOrderingShowFood;
    private LinearLayoutCompat llMovieOrderingMovie;
    private RecyclerView rvMovieOrderingMovieList;
    private LinearLayoutCompat llMovieOrderingFood;
    private ChipGroup cgMovieOrderingCategoryList;
    private RecyclerView rvMovieOrderingProductList;
    private LinearLayoutCompat llMovieOrderingSelected;
    private AppCompatTextView tvMovieOrderingTotalPrice;
    private AppCompatTextView tvMovieOrderingProductCount;
    private LinearLayoutCompat llMovieOrderingPay;
    private JSONObject companyData;
    private JSONArray movieData;
    private JSONArray categoryData;
    private MovieOrderingAdapter movieOrderingAdapter;
    private OrderingAdapter orderingAdapter;
    private String selectedCategory;

    private String uosPartnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_movieordering);

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
            if(activity instanceof MovieOrderingActivity){
                activity.finish();
            }
        }
        Global.activities.add(this);

        ibtnMovieOrderingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_movieordering_back);
        tvMovieOrderingCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_movieordering_companyname);
        tvMovieOrderingShowMovie = findViewById(com.uos.uos_mobile.R.id.tv_movieordering_showmovie);
        tvMovieOrderingShowFood = findViewById(com.uos.uos_mobile.R.id.tv_movieordering_showfood);
        llMovieOrderingMovie = findViewById(com.uos.uos_mobile.R.id.ll_movieordering_movie);
        rvMovieOrderingMovieList = findViewById(com.uos.uos_mobile.R.id.rv_movieordering_movielist);
        llMovieOrderingFood = findViewById(com.uos.uos_mobile.R.id.ll_movieordering_food);
        cgMovieOrderingCategoryList = findViewById(com.uos.uos_mobile.R.id.cg_movieordering_categorylist);
        rvMovieOrderingProductList = findViewById(com.uos.uos_mobile.R.id.rv_movieordering_productlist);
        llMovieOrderingSelected = findViewById(com.uos.uos_mobile.R.id.ll_movieordering_selected);
        llMovieOrderingSelected = findViewById(com.uos.uos_mobile.R.id.ll_movieordering_selected);
        tvMovieOrderingTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_movieordering_totalprice);
        tvMovieOrderingProductCount = findViewById(com.uos.uos_mobile.R.id.tv_movieordering_productcount);
        llMovieOrderingPay = findViewById(com.uos.uos_mobile.R.id.ll_movieordering_order);

        // UI 초기 상태 설정
        tvMovieOrderingShowMovie.setEnabled(false);
        tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
        llMovieOrderingMovie.setVisibility(View.VISIBLE);
        tvMovieOrderingShowFood.setEnabled(true);
        tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        llMovieOrderingFood.setVisibility(View.GONE);

        uosPartnerId = getIntent().getStringExtra("uosPartnerId");

        try {
            SharedPreferenceManager.open(MovieOrderingActivity.this, Global.SharedPreference.APP_DATA);
            JSONObject message = new JSONObject(SharedPreferenceManager.load(Global.SharedPreference.TEMP_MESSAGE, ""));
            SharedPreferenceManager.save(Global.SharedPreference.TEMP_MESSAGE, "");
            SharedPreferenceManager.close();

            companyData = message.getJSONObject("company");
            categoryData = message.getJSONArray("category_list");
            movieData = message.getJSONArray("movie_list");
            tvMovieOrderingCompanyName.setText(companyData.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MovieOrderingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 영화 목록 Adapter 설정
        movieOrderingAdapter = new MovieOrderingAdapter();
        movieOrderingAdapter.setJson(movieData);
        rvMovieOrderingMovieList.setLayoutManager(new LinearLayoutManager(MovieOrderingActivity.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MovieOrderingActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvMovieOrderingMovieList.addItemDecoration(dividerItemDecoration);
        rvMovieOrderingMovieList.setAdapter(movieOrderingAdapter);

        // 상품 목록 Adapter 설정
        orderingAdapter = new OrderingAdapter();
        orderingAdapter.setJson(categoryData);
        rvMovieOrderingProductList.setLayoutManager(new GridLayoutManager(MovieOrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvMovieOrderingProductList.setAdapter(orderingAdapter);
        Global.basketManager = new BasketManager(tvMovieOrderingCompanyName.getText().toString());

        updatePriceInfo();

        // 카테고리를 chipgroup에 추가
        for (int loop = 0; loop < categoryData.length(); loop++) {
            Chip chip = (Chip) MovieOrderingActivity.this.getLayoutInflater().inflate(com.uos.uos_mobile.R.layout.chip_category, cgMovieOrderingCategoryList, false);
            try {
                chip.setText(categoryData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvMovieOrderingProductList.setAdapter(orderingAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (loop == 0) {
                selectedCategory = chip.getText().toString();
                orderingAdapter.setSelectedCategory(selectedCategory);
                rvMovieOrderingProductList.setAdapter(orderingAdapter);
                chip.setChecked(true);
            }
            cgMovieOrderingCategoryList.addView(chip);
        }

        // 뒤로가기 버튼이 눌렸을 경우
        ibtnMovieOrderingBack.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(MovieOrderingActivity.this, com.uos.uos_mobile.R.style.AlertDialogTheme)
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

        // 영화 표시 버튼이 눌렸을 때
        tvMovieOrderingShowMovie.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(false);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
            llMovieOrderingMovie.setVisibility(View.VISIBLE);

            tvMovieOrderingShowFood.setEnabled(true);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingFood.setVisibility(View.GONE);
        });

        // 음식 표시 버튼이 눌렸을 때
        tvMovieOrderingShowFood.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(true);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingMovie.setVisibility(View.GONE);

            tvMovieOrderingShowFood.setEnabled(false);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
            llMovieOrderingFood.setVisibility(View.VISIBLE);
        });

        // 리스트 아이템이 눌렸을 경우
        orderingAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = orderingAdapter.getItem(position);

            if (orderingProductItem.getType() == Global.ItemType.PRODUCT) {
                // 선택된 아이템이 단일상품일 경우
                new SelectProductDialog(MovieOrderingActivity.this, orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            } else {
                // 선택된 아이템이 세트상품일 경우
                new SelectSetDialog(MovieOrderingActivity.this, (OrderingSetItem) orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            }
        });

        // 선택정보창 버튼이 눌렸을 경우
        llMovieOrderingSelected.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(MovieOrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                BasketDialog basketDialog = new BasketDialog(MovieOrderingActivity.this, uosPartnerId);
                basketDialog.setOnDismissListener(dialogInterface -> {
                    updatePriceInfo();
                });
                basketDialog.show();
            }
        });

        // 결제 버튼이 눌렸을 경우
        llMovieOrderingPay.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(MovieOrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MovieOrderingActivity.this, PayActivity.class);
                intent.putExtra("uosPartnerId", uosPartnerId);
                startActivity(intent);
            }
        });

        // 영화 선택 시
        movieOrderingAdapter.setOnItemClickListener((view, position) -> new SelectSeatDialog(MovieOrderingActivity.this, movieOrderingAdapter.getItem(position), new SelectSeatDialog.SelectSeatDialogListener() {
            @Override
            public void onAddProductClicked(MovieItem updatedMovieItem, BasketItem basketItem) {
                movieOrderingAdapter.setItem(position, updatedMovieItem.clone());
                basketItem.setMovieItem(movieOrderingAdapter.getItem(position));

                for (BasketItem basketItem1 : Global.basketManager.getOrderingItemArrayList()) {
                    if (basketItem1.getMenu().equals(basketItem.getMenu())) {
                        Global.basketManager.getOrderingItemArrayList().remove(basketItem1);
                    }
                }
                Global.basketManager.getOrderingItemArrayList().add(basketItem);
                updatePriceInfo();
            }
        }).show());
    }

    private void updatePriceInfo() {
        ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvMovieOrderingTotalPrice.getText().toString().replace(",", "")), Global.basketManager.getOrderPrice());
        va.setDuration(1000);
        va.addUpdateListener(va1 -> tvMovieOrderingTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
        va.start();

        tvMovieOrderingProductCount.setText(String.valueOf(Global.basketManager.getOrderCount()));

        if (Global.basketManager.getOrderCount() == 0) {
            llMovieOrderingSelected.setEnabled(false);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingPay.setEnabled(false);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else {
            llMovieOrderingSelected.setEnabled(true);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            llMovieOrderingPay.setEnabled(true);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        }
    }
}