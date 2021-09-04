package com.uof.uof_mobile.dialog;

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

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.WaitingOrderInfoAdapter;
import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.OrderListItem;
import com.uof.uof_mobile.manager.SQLiteManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;

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
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.orderListItem = orderListItem;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_orderinfo);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgOrderInfoClose = findViewById(R.id.ibtn_dlgorderinfo_close);
        tvDlgOrderInfoCompanyName = findViewById(R.id.tv_dlgorderinfo_companyname);
        tvDlgOrderInfoOrderTime = findViewById(R.id.tv_dlgorderinfo_ordertime);
        tvDlgOrderInfoOrderTotalPrice = findViewById(R.id.tv_dlgorderinfo_totalprice);
        rvDlgOrderInfo = findViewById(R.id.rv_dlgorderinfo);

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
        dividerItemDecoration.setDrawable(context.getDrawable(R.drawable.recyclerview_divider));
        rvDlgOrderInfo.addItemDecoration(dividerItemDecoration);
        rvDlgOrderInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgOrderInfo.setAdapter(waitingOrderInfoAdapter);

        // 닫기 버튼이 눌렸을 경우
        ibtnDlgOrderInfoClose.setOnClickListener(view -> {
            dismiss();
        });
    }
}
