package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.BasketAdapter;
import com.uof.uof_mobile.manager.BasketManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;

public class BasketDialog extends Dialog {
    private final Context context;
    private final BasketManager basketManager;
    private AppCompatImageButton ibtnDlgBasketClose;
    private RecyclerView rvDlgBasket;
    private AppCompatTextView tvDlgBasketTotalPrice;
    private LinearLayoutCompat llDlgBasketPay;
    private BasketAdapter basketAdapter;

    public BasketDialog(@NonNull Context context, BasketManager basketManager) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.basketManager = basketManager;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_basket);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgBasketClose = findViewById(R.id.ibtn_dlgbasket_close);
        rvDlgBasket = findViewById(R.id.rv_dlgbasket);
        tvDlgBasketTotalPrice = findViewById(R.id.tv_dlgbasket_totalprice);
        llDlgBasketPay = findViewById(R.id.ll_dlgbasket_order);

        tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderPrice()));

        basketAdapter = new BasketAdapter(basketManager);
        rvDlgBasket.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.recyclerview_divider));
        rvDlgBasket.addItemDecoration(dividerItemDecoration);
        rvDlgBasket.setAdapter(basketAdapter);

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgBasketClose.setOnClickListener(view -> {
            dismiss();
        });

        // 장바구니 아이템 수량 변경 시
        basketAdapter.setOnUpdateListener(() -> {
            tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderPrice()));
        });
    }
}
