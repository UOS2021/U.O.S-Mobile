package com.uof.uof_mobile.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.WaitingOrderInfoAdapter;
import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.WaitingOrderItem;
import com.uof.uof_mobile.manager.SQLiteManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class WaitingOrderInfoDialog extends AppCompatDialog {
    private final Context context;
    private final WaitingOrderItem waitingOrderItem;
    private AppCompatImageButton ibtnDlgWaitingOrderInfoClose;
    private AppCompatTextView tvDlgWaitingOrderInfoCompanyName;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderTime;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderNumber;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderTotalPrice;
    private RecyclerView rvDlgWaitingOrderInfo;
    private ConstraintLayout clDlgWaitingOrderInfoTake;
    private WaitingOrderInfoAdapter waitingOrderInfoAdapter;
    private SQLiteManager sqLiteManager;

    public WaitingOrderInfoDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, WaitingOrderItem waitingOrderItem) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.waitingOrderItem = waitingOrderItem;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_waitingorderinfo);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgWaitingOrderInfoClose = findViewById(R.id.ibtn_dlgwaitingorderinfo_close);
        tvDlgWaitingOrderInfoCompanyName = findViewById(R.id.tv_dlgwaitingorderinfo_companyname);
        tvDlgWaitingOrderInfoOrderNumber = findViewById(R.id.tv_dlgwaitingorderinfo_ordernumber);
        tvDlgWaitingOrderInfoOrderTime = findViewById(R.id.tv_dlgwaitingorderinfo_ordertime);
        tvDlgWaitingOrderInfoOrderTotalPrice = findViewById(R.id.tv_dlgwaitingorderinfo_totalprice);
        rvDlgWaitingOrderInfo = findViewById(R.id.rv_dlgwaitingorderinfo);
        clDlgWaitingOrderInfoTake = findViewById(R.id.ll_dlgwaitingorderinfo_takeproduct);

        sqLiteManager = new SQLiteManager(context);

        tvDlgWaitingOrderInfoCompanyName.setText(waitingOrderItem.getCompanyName());
        tvDlgWaitingOrderInfoOrderTime.setText(String.valueOf(waitingOrderItem.getOrderTime()));
        tvDlgWaitingOrderInfoOrderNumber.setText(String.valueOf(waitingOrderItem.getOrderNumber()));
        int totalPrice = 0;
        for (BasketItem basketItem : waitingOrderItem.getBasketItemArrayList()) {
            totalPrice += basketItem.getTotalPrice();
        }

        ValueAnimator va = ValueAnimator.ofInt(0, totalPrice);
        va.setDuration(2000);
        va.addUpdateListener(va1 -> tvDlgWaitingOrderInfoOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer)va1.getAnimatedValue()) + "원"));
        va.start();

        waitingOrderInfoAdapter = new WaitingOrderInfoAdapter();
        waitingOrderInfoAdapter.setBasketItemArrayList(waitingOrderItem.getBasketItemArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getDrawable(R.drawable.recyclerview_divider));
        rvDlgWaitingOrderInfo.addItemDecoration(dividerItemDecoration);
        rvDlgWaitingOrderInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgWaitingOrderInfo.setAdapter(waitingOrderInfoAdapter);

        if (waitingOrderItem.getState().equals("wait")) {
            clDlgWaitingOrderInfoTake.setEnabled(false);
            clDlgWaitingOrderInfoTake.setBackgroundColor(context.getResources().getColor(R.color.gray));
        } else {
            clDlgWaitingOrderInfoTake.setEnabled(true);
            clDlgWaitingOrderInfoTake.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
        }

        // 닫기 버튼이 눌렸을 경우
        ibtnDlgWaitingOrderInfoClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 수령 완료 버튼이 눌렸을 경우
        clDlgWaitingOrderInfoTake.setOnClickListener(view -> {
            sqLiteManager.openDatabase();
            sqLiteManager.removeOrder(waitingOrderItem.getOrderNumber());
            sqLiteManager.closeDatabase();
            dismiss();
        });
    }
}