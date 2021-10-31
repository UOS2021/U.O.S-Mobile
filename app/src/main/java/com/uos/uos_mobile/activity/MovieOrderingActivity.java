package com.uos.uos_mobile.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

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
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 영화관에서 주문가능한 상품목록을 보여주는 Activity.<br>
 * xml: activity_movieordering.xml<br><br>
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class MovieOrderingActivity extends UosActivity {

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

    private BasketManager basketManager;

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(MovieOrderingActivity.this, com.uos.uos_mobile.R.style.AlertDialogTheme)
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
        setContentView(com.uos.uos_mobile.R.layout.activity_movieordering);

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

        /* UI 초기 상태 설정 */
        tvMovieOrderingShowMovie.setEnabled(false);
        tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
        llMovieOrderingMovie.setVisibility(View.VISIBLE);
        tvMovieOrderingShowFood.setEnabled(true);
        tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        llMovieOrderingFood.setVisibility(View.GONE);

        uosPartnerId = getIntent().getStringExtra("uosPartnerId");

        try {
            SharedPreferencesManager.open(MovieOrderingActivity.this, Global.SharedPreference.APP_DATA);
            JSONObject message = new JSONObject((String) SharedPreferencesManager.load(Global.SharedPreference.TEMP_MESSAGE, ""));
            SharedPreferencesManager.save(Global.SharedPreference.TEMP_MESSAGE, "");
            SharedPreferencesManager.close();

            companyData = message.getJSONObject("company");
            categoryData = message.getJSONArray("category_list");
            movieData = message.getJSONArray("movie_list");
            tvMovieOrderingCompanyName.setText(companyData.getString("name"));

            /* 영화 목록 Adapter 설정 */
            movieOrderingAdapter = new MovieOrderingAdapter();
            movieOrderingAdapter.setJson(movieData);
            rvMovieOrderingMovieList.setLayoutManager(new LinearLayoutManager(MovieOrderingActivity.this, LinearLayoutManager.VERTICAL, false));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MovieOrderingActivity.this, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
            rvMovieOrderingMovieList.addItemDecoration(dividerItemDecoration);
            rvMovieOrderingMovieList.setAdapter(movieOrderingAdapter);

            /* 상품 목록 Adapter 설정 */
            orderingAdapter = new OrderingAdapter();
            orderingAdapter.setJson(categoryData);
            rvMovieOrderingProductList.setLayoutManager(new GridLayoutManager(MovieOrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
            rvMovieOrderingProductList.setAdapter(orderingAdapter);
            basketManager = new BasketManager();

            updatePriceInfo();

            /* 카테고리를 chipgroup에 추가 */
            for (int loop = 0; loop < categoryData.length(); loop++) {
                Chip chip = (Chip) MovieOrderingActivity.this.getLayoutInflater().inflate(com.uos.uos_mobile.R.layout.chip_category, cgMovieOrderingCategoryList, false);

                chip.setText(categoryData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvMovieOrderingProductList.setAdapter(orderingAdapter);
                });

                if (loop == 0) {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvMovieOrderingProductList.setAdapter(orderingAdapter);
                    chip.setChecked(true);
                }
                cgMovieOrderingCategoryList.addView(chip);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MovieOrderingActivity.this, "상품목록을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* 뒤로가기 버튼이 눌렸을 경우 */
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

        /* 영화 표시 버튼이 눌렸을 경우 */
        tvMovieOrderingShowMovie.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(false);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
            llMovieOrderingMovie.setVisibility(View.VISIBLE);

            tvMovieOrderingShowFood.setEnabled(true);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingFood.setVisibility(View.GONE);
        });

        /* 음식 표시 버튼이 눌렸을 경우 */
        tvMovieOrderingShowFood.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(true);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingMovie.setVisibility(View.GONE);

            tvMovieOrderingShowFood.setEnabled(false);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.recyclerview_background));
            llMovieOrderingFood.setVisibility(View.VISIBLE);
        });

        /* 리스트 아이템이 눌렸을 경우 */
        orderingAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = orderingAdapter.getItem(position);

            if (orderingProductItem.getType() == Global.ItemType.PRODUCT) {

                /* 선택된 아이템이 단일상품일 경우 */

                new SelectProductDialog(MovieOrderingActivity.this, orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            } else {

                /* 선택된 아이템이 세트상품일 경우 */

                new SelectSetDialog(MovieOrderingActivity.this, (OrderingSetItem) orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            }
        });

        /* 선택정보창 버튼이 눌렸을 경우 */
        llMovieOrderingSelected.setOnClickListener(view -> {
            BasketDialog basketDialog = new BasketDialog(MovieOrderingActivity.this, uosPartnerId, basketManager, tvMovieOrderingCompanyName.getText().toString());
            basketDialog.setOnDismissListener(dialogInterface -> {
                updatePriceInfo();
            });
            basketDialog.show();
        });

        /* 결제 버튼이 눌렸을 경우 */
        llMovieOrderingPay.setOnClickListener(view -> {
            Intent intent = new Intent(MovieOrderingActivity.this, PayActivity.class);
            intent.putExtra("uosPartnerId", uosPartnerId);
            intent.putExtra("basketManager", basketManager);
            intent.putExtra("companyName", tvMovieOrderingCompanyName.getText().toString());
            startActivity(intent);
        });

        /* 목록에 있는 영화를 선택했을 경우 */
        movieOrderingAdapter.setOnItemClickListener((view, position) -> new SelectSeatDialog(MovieOrderingActivity.this, movieOrderingAdapter.getItem(position), (updatedMovieItem, basketItem) -> {
            movieOrderingAdapter.setItem(position, updatedMovieItem.clone());
            basketItem.setMovieItem(movieOrderingAdapter.getItem(position));

            for (BasketItem basketItem1 : basketManager.getOrderingItemArrayList()) {
                if (basketItem1.getMenu().equals(basketItem.getMenu())) {
                    basketManager.getOrderingItemArrayList().remove(basketItem1);
                    break;
                }
            }
            basketManager.getOrderingItemArrayList().add(basketItem);
            updatePriceInfo();
        }).show());
    }

    /**
     * 상품 선택 또는 선택해제 시 선택 상품 정보(가격 및 개수)를 갱신합니다.
     */
    private void updatePriceInfo() {
        ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvMovieOrderingTotalPrice.getText().toString().replace(",", "")), basketManager.getOrderPrice());
        va.setDuration(1000);
        va.addUpdateListener(va1 -> tvMovieOrderingTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
        va.start();

        tvMovieOrderingProductCount.setText(String.valueOf(basketManager.getOrderCount()));

        if (basketManager.getOrderCount() == 0) {

            /* 장바구니에 담긴 상품이 없을 경우 */

            llMovieOrderingSelected.setEnabled(false);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            llMovieOrderingPay.setEnabled(false);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else {

            /* 장바구니에 담긴 상품이 있을 경우 */

            llMovieOrderingSelected.setEnabled(true);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            llMovieOrderingPay.setEnabled(true);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        }
    }
}