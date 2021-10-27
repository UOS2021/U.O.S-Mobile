package com.uos.uos_mobile.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.activity.PayActivity;
import com.uos.uos_mobile.adapter.BasketAdapter;
import com.uos.uos_mobile.manager.BasketManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;

public class BasketDialog extends UosDialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgBasketClose;
    private RecyclerView rvDlgBasket;
    private AppCompatTextView tvDlgBasketTotalPrice;
    private ConstraintLayout clDlgBasketOrder;
    private BasketAdapter basketAdapter;

    private final String uosPartnerId;

    private final BasketManager basketManager;

    private final String companyName;

    public BasketDialog(@NonNull Context context, String uosPartnerId, BasketManager basketManager, String companyName) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.uosPartnerId = uosPartnerId;
        this.basketManager = basketManager;
        this.companyName = companyName;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_basket);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        ibtnDlgBasketClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgbasket_close);
        rvDlgBasket = findViewById(com.uos.uos_mobile.R.id.rv_dlgbasket);
        tvDlgBasketTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_dlgbasket_totalprice);
        clDlgBasketOrder = findViewById(com.uos.uos_mobile.R.id.cl_dlgbasket_order);

        tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderPrice()));

        basketAdapter = new BasketAdapter(this, basketManager);
        rvDlgBasket.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvDlgBasket.addItemDecoration(dividerItemDecoration);
        rvDlgBasket.setAdapter(basketAdapter);

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgBasketClose.setOnClickListener(view -> {
            dismiss();
        });

        // 장바구니 아이템 수량 변경 시
        basketAdapter.setOnUpdateListener(() -> {
            ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvDlgBasketTotalPrice.getText().toString().replace(",", "")), basketManager.getOrderPrice());
            va.setDuration(1000);
            va.addUpdateListener(va1 -> tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
            va.start();
        });

        // 주문하기 버튼 클릭 시
        clDlgBasketOrder.setOnClickListener(view -> {
            Intent intent = new Intent(context, PayActivity.class);
            intent.putExtra("uosPartnerId", uosPartnerId);
            intent.putExtra("basketManager", basketManager);
            intent.putExtra("companyName", companyName);
            context.startActivity(intent);
        });
    }
}
