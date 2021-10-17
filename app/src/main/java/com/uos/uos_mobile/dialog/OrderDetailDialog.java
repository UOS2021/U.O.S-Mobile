package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.adapter.OrderProductAdapter;
import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.manager.UsefulFuncManager;

public class OrderDetailDialog extends UosDialog {
    private final Context context;
    private final OrderItem orderItem;
    private AppCompatImageButton ibtnDlgWaitingOrderInfoClose;
    private AppCompatTextView tvDlgWaitingOrderInfoCompanyName;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderDate;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderCode;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderTotalPrice;
    private RecyclerView rvDlgWaitingOrderInfo;
    private OrderProductAdapter orderProductAdapter;

    public OrderDetailDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, OrderItem orderItem) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.orderItem = orderItem;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_orderdetail);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        ibtnDlgWaitingOrderInfoClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgorderdetail_close);
        tvDlgWaitingOrderInfoCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_companyname);
        tvDlgWaitingOrderInfoOrderCode = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_ordercode);
        tvDlgWaitingOrderInfoOrderDate = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_date);
        tvDlgWaitingOrderInfoOrderTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_totalprice);
        rvDlgWaitingOrderInfo = findViewById(com.uos.uos_mobile.R.id.rv_dlgorderdetail);

        tvDlgWaitingOrderInfoCompanyName.setText(orderItem.getCompanyName());
        tvDlgWaitingOrderInfoOrderDate.setText(String.valueOf(orderItem.getDate()));
        tvDlgWaitingOrderInfoOrderCode.setText(String.valueOf(orderItem.getOrderCode()));
        int totalPrice = 0;
        for (BasketItem basketItem : orderItem.getBasketItemArrayList()) {
            totalPrice += basketItem.getTotalPrice();
        }

        tvDlgWaitingOrderInfoOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(totalPrice) + "원");

        orderProductAdapter = new OrderProductAdapter();
        orderProductAdapter.setBasketItemArrayList(orderItem.getBasketItemArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvDlgWaitingOrderInfo.addItemDecoration(dividerItemDecoration);
        rvDlgWaitingOrderInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgWaitingOrderInfo.setAdapter(orderProductAdapter);


        // 닫기 버튼이 눌렸을 경우
        ibtnDlgWaitingOrderInfoClose.setOnClickListener(view -> {
            dismiss();
        });
    }
}