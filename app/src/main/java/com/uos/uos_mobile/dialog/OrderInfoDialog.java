package com.uos.uos_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.uos.uos_mobile.adapter.WaitingOrderInfoAdapter;
import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.OrderListItem;
import com.uos.uos_mobile.manager.SQLiteManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

public class OrderInfoDialog extends AppCompatDialog {
    private final Context context;
    private final OrderListItem orderListItem;
    private AppCompatImageButton ibtnDlgOrderInfoClose;
    private AppCompatTextView tvDlgOrderInfoCompanyName;
    private AppCompatTextView tvDlgOrderInfoOrderTime;
    private AppCompatTextView tvDlgOrderInfoOrderTotalPrice;
    private RecyclerView rvDlgOrderInfo;
    private WaitingOrderInfoAdapter waitingOrderInfoAdapter;
    private SQLiteManager sqLiteManager;

    public OrderInfoDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, OrderListItem orderListItem) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.orderListItem = orderListItem;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.uos.uos_mobile.R.layout.dialog_orderinfo);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    @Override
    public void dismiss() {
        Global.removeDialog(this);
        super.dismiss();
    }

    private void init() {
        Global.addDialog(this, false);

        ibtnDlgOrderInfoClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgorderinfo_close);
        tvDlgOrderInfoCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderinfo_companyname);
        tvDlgOrderInfoOrderTime = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderinfo_ordertime);
        tvDlgOrderInfoOrderTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderinfo_totalprice);
        rvDlgOrderInfo = findViewById(com.uos.uos_mobile.R.id.rv_dlgorderinfo);

        sqLiteManager = new SQLiteManager(context);

        tvDlgOrderInfoCompanyName.setText(orderListItem.getCompanyName());
        tvDlgOrderInfoOrderTime.setText(orderListItem.getDate() + " " + orderListItem.getTime());

        int totalPrice = 0;
        for (BasketItem basketItem : orderListItem.getBasketItemArrayList()) {
            totalPrice += basketItem.getTotalPrice();
        }

        tvDlgOrderInfoOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(totalPrice) + "원");

        waitingOrderInfoAdapter = new WaitingOrderInfoAdapter();
        waitingOrderInfoAdapter.setBasketItemArrayList(orderListItem.getBasketItemArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvDlgOrderInfo.addItemDecoration(dividerItemDecoration);
        rvDlgOrderInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgOrderInfo.setAdapter(waitingOrderInfoAdapter);

        // 닫기 버튼이 눌렸을 경우
        ibtnDlgOrderInfoClose.setOnClickListener(view -> {
            dismiss();
        });
    }
}
