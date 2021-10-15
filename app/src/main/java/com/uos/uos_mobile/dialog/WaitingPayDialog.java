package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uos.uos_mobile.activity.PayActivity;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class WaitingPayDialog extends UosDialog {
    private final Context context;
    private final String companyName;
    private final JSONObject orderData;
    private ProgressBar pbDlgWaitingOrder;
    private AppCompatTextView tvDlgWaitingOrderMessage;
    private ConstraintLayout clDlgWaitingOrderCancel;
    private AppCompatTextView tvDlgWaitingOrder2;
    private AppCompatTextView tvDlgWaitingOrder3;
    private boolean orderCancel;

    private String uosPartnerId;

    public WaitingPayDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, String companyName, JSONObject orderData, String uosPartnerId) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.companyName = companyName;
        this.orderData = orderData;
        this.uosPartnerId = uosPartnerId;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.uos.uos_mobile.R.layout.dialog_waitingpay);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    /**
     * Activity 실행 시 데이터 및 UI를 초기화하는 함수.
     */
    private void init() {
        pbDlgWaitingOrder = findViewById(com.uos.uos_mobile.R.id.pb_dlgwaitingorder);
        tvDlgWaitingOrderMessage = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorder_message);
        clDlgWaitingOrderCancel = findViewById(com.uos.uos_mobile.R.id.cl_dlgwaitingorder_cancel);
        tvDlgWaitingOrder2 = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorder_2);
        tvDlgWaitingOrder3 = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorder_3);

        tvDlgWaitingOrderMessage.setText("매장에서 주문을 확인하고 있습니다\n잠시만 기다려주세요...");

        // 주문접수 상태 불러오기
        new Thread(() -> {
            try {
                JSONObject sendData = new JSONObject();
                JSONObject message = new JSONObject();

                sendData.accumulate("request_code", Global.Network.Request.ORDER_ACCEPTED_STATE);
                message.accumulate("uospartner_id", uosPartnerId);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(60000), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.ORDER_ACCEPT)) {
                    
                    /* 주문이 수락되었을 경우 */
                    
                    if (!orderCancel) {
                        
                        /* 주문을 취소하지 않았을 경우 */
                        
                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrderMessage.setText("결제 중입니다\n잠시만 기다려주세요...");
                            tvDlgWaitingOrder2.setText("확인");
                            tvDlgWaitingOrder3.setVisibility(View.GONE);
                            clDlgWaitingOrderCancel.setEnabled(false);
                            clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));
                        });
                    }

                    sendData = new JSONObject();
                    message = new JSONObject();

                    sendData.accumulate("request_code", Global.Network.Request.ORDER_CANCEL);
                    message.accumulate("uospartner_id", uosPartnerId);
                    message.accumulate("cancel", orderCancel);
                    sendData.accumulate("message", message);

                    recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                    responseCode = recvData.getString("response_code");

                    ((PayActivity) context).runOnUiThread(() -> {
                        pbDlgWaitingOrder.setVisibility(View.GONE);
                        clDlgWaitingOrderCancel.setEnabled(true);
                        clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    });

                    if (responseCode.equals(Global.Network.Response.PAY_SUCCESS)) {

                        /* 결제 성공 */

                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrderMessage.setText("결제가 완료되었습니다\n주문하신 상품이 준비되면 알려드리겠습니다");
                        });
                    } else if (responseCode.equals(Global.Network.Response.PAY_FAIL_WRONG_PASSWORD)) {

                        /* 결제 실패 - 카드 비밀번호 틀림 */

                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrderMessage.setText("카드 비밀번호가 틀렸습니다\n확인 후 다시 시도해주세요");
                        });
                    } else if (responseCode.equals(Global.Network.Response.ORDER_CANCEL_SUCCESS)) {

                        /* 주문취소 성공 */

                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrderMessage.setText("주문이 취소되었습니다");
                        });
                    } else {
                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                        });
                    }
                } else if (responseCode.equals(Global.Network.Response.ORDER_REFUSE)) {

                    /* 주문이 거부되었을 경우 */

                    ((PayActivity) context).runOnUiThread(() -> {
                        tvDlgWaitingOrder2.setText("확인");
                        tvDlgWaitingOrder3.setVisibility(View.GONE);
                        pbDlgWaitingOrder.setVisibility(View.GONE);
                        tvDlgWaitingOrderMessage.setText("현재 매장이 바빠 주문 접수가 불가능합니다\n나중에 다시 시도해주세요");
                        clDlgWaitingOrderCancel.setEnabled(true);
                        clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    });
                } else {

                    /* 기타 오류 발생 */

                    ((PayActivity) context).runOnUiThread(() -> {
                        tvDlgWaitingOrder2.setText("확인");
                        tvDlgWaitingOrder3.setVisibility(View.GONE);
                        pbDlgWaitingOrder.setVisibility(View.GONE);
                        tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                        clDlgWaitingOrderCancel.setEnabled(true);
                        clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    });
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
                ((PayActivity) context).runOnUiThread(() -> {
                    tvDlgWaitingOrder2.setText("확인");
                    tvDlgWaitingOrder3.setVisibility(View.GONE);
                    pbDlgWaitingOrder.setVisibility(View.GONE);
                    tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                    clDlgWaitingOrderCancel.setEnabled(true);
                    clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                });
            }
        }).start();

        /* 주문취소 버튼이 눌렸을 경우 */
        clDlgWaitingOrderCancel.setOnClickListener(view -> {
            if (tvDlgWaitingOrder2.getText().toString().equals("확인")) {

                /* 주문취소 버튼 내 텍스트가 확인일 경우 */

                dismiss();
            } else {

                /* 주문취소 버튼 내 텍스트가 확인이 아닐 경우 */

                orderCancel = true;
                tvDlgWaitingOrderMessage.setText("주문을 취소하셨습니다\n잠시만 기다려주세요...");
                tvDlgWaitingOrder2.setText("확인");
                tvDlgWaitingOrder3.setVisibility(View.GONE);
                clDlgWaitingOrderCancel.setEnabled(false);
                clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));
            }
        });
    }
}

