package com.uos.uos_mobile.dialog;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.R;
import com.uos.uos_mobile.activity.PayActivity;
import com.uos.uos_mobile.adapter.BasketAdapter;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

public class BasketDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgBasketClose;
    private RecyclerView rvDlgBasket;
    private AppCompatTextView tvDlgBasketTotalPrice;
    private LinearLayoutCompat llDlgBasketOrder;
    private BasketAdapter basketAdapter;

    public BasketDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;

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

    @Override
    public void dismiss() {
        Global.dialogs.remove(this);
        super.dismiss();
    }

    private void init() {
        for (Dialog dialog : Global.dialogs) {
            if (dialog instanceof BasketDialog) {
                dialog.dismiss();
            }
        }
        Global.dialogs.add(this);

        ibtnDlgBasketClose = findViewById(R.id.ibtn_dlgbasket_close);
        rvDlgBasket = findViewById(R.id.rv_dlgbasket);
        tvDlgBasketTotalPrice = findViewById(R.id.tv_dlgbasket_totalprice);
        llDlgBasketOrder = findViewById(R.id.ll_dlgbasket_order);

        tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderPrice()));

        basketAdapter = new BasketAdapter(this);
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
            ValueAnimator va = ValueAnimator.ofInt(Integer.valueOf(tvDlgBasketTotalPrice.getText().toString().replace(",", "")), Global.basketManager.getOrderPrice());
            va.setDuration(1000);
            va.addUpdateListener(va1 -> tvDlgBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) va1.getAnimatedValue())));
            va.start();
        });

        // 주문하기 버튼 클릭 시
        llDlgBasketOrder.setOnClickListener(view -> {
            context.startActivity(new Intent(context, PayActivity.class));
        });
    }
}
