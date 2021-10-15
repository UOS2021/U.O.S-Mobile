package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.adapter.WaitingOrderInfoAdapter;
import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.WaitingOrderItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class WaitingOrderDetailDialog extends UosDialog {
    private final Context context;
    private final WaitingOrderItem waitingOrderItem;
    private AppCompatImageButton ibtnDlgWaitingOrderInfoClose;
    private AppCompatTextView tvDlgWaitingOrderInfoCompanyName;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderTime;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderCode;
    private AppCompatTextView tvDlgWaitingOrderInfoOrderTotalPrice;
    private RecyclerView rvDlgWaitingOrderInfo;
    private ConstraintLayout clDlgWaitingOrderInfoTake;
    private WaitingOrderInfoAdapter waitingOrderInfoAdapter;

    public WaitingOrderDetailDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, WaitingOrderItem waitingOrderItem) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.waitingOrderItem = waitingOrderItem;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.uos.uos_mobile.R.layout.dialog_waitingorderdetail);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgWaitingOrderInfoClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgwaitingorderinfo_close);
        tvDlgWaitingOrderInfoCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderinfo_companyname);
        tvDlgWaitingOrderInfoOrderCode = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderinfo_ordercode);
        tvDlgWaitingOrderInfoOrderTime = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderinfo_ordertime);
        tvDlgWaitingOrderInfoOrderTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderinfo_totalprice);
        rvDlgWaitingOrderInfo = findViewById(com.uos.uos_mobile.R.id.rv_dlgwaitingorderinfo);
        clDlgWaitingOrderInfoTake = findViewById(com.uos.uos_mobile.R.id.ll_dlgwaitingorderinfo_takeproduct);

        tvDlgWaitingOrderInfoCompanyName.setText(waitingOrderItem.getCompanyName());
        tvDlgWaitingOrderInfoOrderTime.setText(String.valueOf(waitingOrderItem.getOrderTime()));
        tvDlgWaitingOrderInfoOrderCode.setText(String.valueOf(waitingOrderItem.getOrderCode()));
        int totalPrice = 0;
        for (BasketItem basketItem : waitingOrderItem.getBasketItemArrayList()) {
            totalPrice += basketItem.getTotalPrice();
        }

        tvDlgWaitingOrderInfoOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(totalPrice) + "원");

        waitingOrderInfoAdapter = new WaitingOrderInfoAdapter();
        waitingOrderInfoAdapter.setBasketItemArrayList(waitingOrderItem.getBasketItemArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvDlgWaitingOrderInfo.addItemDecoration(dividerItemDecoration);
        rvDlgWaitingOrderInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgWaitingOrderInfo.setAdapter(waitingOrderInfoAdapter);

        if (waitingOrderItem.getState().equals(Global.Order.PREPARING)) {
            clDlgWaitingOrderInfoTake.setEnabled(false);
            clDlgWaitingOrderInfoTake.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else {
            clDlgWaitingOrderInfoTake.setEnabled(true);
            clDlgWaitingOrderInfoTake.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        }

        // 닫기 버튼이 눌렸을 경우
        ibtnDlgWaitingOrderInfoClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 수령 완료 버튼이 눌렸을 경우
        clDlgWaitingOrderInfoTake.setOnClickListener(view -> {
            try {
                JSONObject message = new JSONObject();
                message.accumulate("order_code", waitingOrderItem.getOrderCode());

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CUSTOMER_TOOK_PRODUCT);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHANGE_ORDER_STATE_SUCCESS)) {

                    /* 상품수령완료 */

                    
                } else {

                    /* 상품수령완료 실패 */

                    
                }

            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
                Toast.makeText(context, "상품 수령 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }
}