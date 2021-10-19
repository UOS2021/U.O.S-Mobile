package com.uos.uos_mobile.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.R;
import com.uos.uos_mobile.activity.OrderListActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.adapter.OrderProductAdapter;
import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class OrderDetailDialog extends UosDialog {
    private final Context context;
    private final OrderItem orderItem;
    private AppCompatImageButton ibtnDlgOrderDetailClose;
    private AppCompatTextView tvDlgOrderDetailCompanyName;
    private AppCompatTextView tvDlgOrderDetailOrderDate;
    private AppCompatTextView tvDlgOrderDetailOrderCode;
    private ConstraintLayout clDlgOrderDetailCancelOrder;
    private AppCompatTextView tvDlgOrderDetailOrderTotalPrice;
    private RecyclerView rvDlgOrderDetail;
    private OrderProductAdapter orderProductAdapter;

    private boolean isCancelProcessRunning = false;

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
        ibtnDlgOrderDetailClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgorderdetail_close);
        tvDlgOrderDetailCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_companyname);
        tvDlgOrderDetailOrderCode = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_ordercode);
        tvDlgOrderDetailOrderDate = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_date);
        clDlgOrderDetailCancelOrder = findViewById(com.uos.uos_mobile.R.id.cl_dlgorderdetail_cancelorder);
        tvDlgOrderDetailOrderTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_dlgorderdetail_totalprice);
        rvDlgOrderDetail = findViewById(com.uos.uos_mobile.R.id.rv_dlgorderdetail);

        tvDlgOrderDetailCompanyName.setText(orderItem.getCompanyName());
        tvDlgOrderDetailOrderDate.setText(String.valueOf(orderItem.getDate()));
        tvDlgOrderDetailOrderCode.setText(String.valueOf(orderItem.getOrderCode()));
        int totalPrice = 0;
        for (BasketItem basketItem : orderItem.getBasketItemArrayList()) {
            totalPrice += basketItem.getTotalPrice();
        }

        tvDlgOrderDetailOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(totalPrice) + "원");

        orderProductAdapter = new OrderProductAdapter();
        orderProductAdapter.setBasketItemArrayList(orderItem.getBasketItemArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(context.getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvDlgOrderDetail.addItemDecoration(dividerItemDecoration);
        rvDlgOrderDetail.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDlgOrderDetail.setAdapter(orderProductAdapter);

        if (orderItem.getState() == Global.Order.WAITING_ACCEPT) {
            clDlgOrderDetailCancelOrder.setVisibility(View.VISIBLE);
        } else {
            clDlgOrderDetailCancelOrder.setVisibility(View.INVISIBLE);
        }

        /* 닫기 버튼이 눌렸을 경우 */
        ibtnDlgOrderDetailClose.setOnClickListener(view -> {
            dismiss();
        });

        clDlgOrderDetailCancelOrder.setOnClickListener(view -> {
            clDlgOrderDetailCancelOrder.setEnabled(false);
            AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setTitle(tvDlgOrderDetailCompanyName.getText().toString() + " 주문취소")
                    .setMessage("주문(" + tvDlgOrderDetailOrderCode.getText().toString() + ")을 취소하시겠습니까?")
                    .setPositiveButton("예", (dialogInterface, i) -> {
                        try {
                            isCancelProcessRunning = true;
                            JSONObject message = new JSONObject();
                            message.accumulate("order_code", Integer.valueOf(tvDlgOrderDetailOrderCode.getText().toString()));

                            JSONObject sendData = new JSONObject();
                            sendData.accumulate("request_code", Global.Network.Request.CANCEL_ORDER);
                            sendData.accumulate("message", message);

                            JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                            String responseCode = recvData.getString("response_code");

                            if (responseCode.equals(Global.Network.Response.CANCEL_ORDER_SUCCESS)) {

                                /* 주문취소 성공 */

                                Toast.makeText(context, "주문이 취소되었습니다", Toast.LENGTH_SHORT).show();

                                ((OrderListActivity)UosActivity.get(OrderListActivity.class)).updateList();

                                dismiss();
                            } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                                /* 서버 연결 실패 */

                                Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                            } else {

                                /* 주문취소 실패 - 기타 오류 */

                                Toast.makeText(context, "주문취소 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "주문취소 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                        }
                        isCancelProcessRunning = false;
                        clDlgOrderDetailCancelOrder.setEnabled(true);
                    })
                    .setNegativeButton("아니오", null).create();

            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            });

            alertDialog.setOnDismissListener(dialog -> {
                if (!isCancelProcessRunning) {
                    clDlgOrderDetailCancelOrder.setEnabled(true);
                }
            });

            alertDialog.show();
        });
    }

    /**
     * FCM 데이터 수신 시 OrderState에 따른 OrderDetailDialog의 UI 업데이트를 위한 함수.
     *
     * @param orderCode 주문코드.
     * @param orderState 주문상태.
     */
    public void updateOrderState(int orderCode, int orderState) {
        ((OrderListActivity)context).runOnUiThread(() -> {
            if (orderItem.getOrderCode() == orderCode) {
                
                /* OrderDetailDialog에서 표시되고 있는 주문과 FCM으로 수신된 주문의 주문코드가 동일할 경우 */
                
                if (orderItem.getState() == Global.Order.WAITING_ACCEPT && orderState == Global.Order.PREPARING) {
                    Toast.makeText(context, "주문이 접수되었습니다", Toast.LENGTH_SHORT).show();
                    clDlgOrderDetailCancelOrder.setVisibility(View.INVISIBLE);
                } else if (orderItem.getOrderCode() == orderCode && orderState == -1) {
                    Toast.makeText(context, "주문이 거절되었습니다", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }
}