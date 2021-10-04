package com.uos.uos_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uos.uos_mobile.R;
import com.uos.uos_mobile.activity.PayActivity;
import com.uos.uos_mobile.manager.SQLiteManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class WaitingOrderDialog extends AppCompatDialog {
    private final Context context;
    private final String companyName;
    private final JSONObject orderData;
    private ProgressBar pbDlgWaitingOrder;
    private AppCompatTextView tvDlgWaitingOrderMessage;
    private ConstraintLayout clDlgWaitingOrderCancel;
    private AppCompatTextView tvDlgWaitingOrder2;
    private AppCompatTextView tvDlgWaitingOrder3;
    private boolean orderCancel;

    public WaitingOrderDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, String companyName, JSONObject orderData) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.companyName = companyName;
        this.orderData = orderData;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_waitingorder);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    @Override
    public void dismiss() {
        Global.dialogs.remove(this);
        super.dismiss();
    }

    private void init() {
        for(Dialog dialog : Global.dialogs){
            if(dialog instanceof WaitingOrderDialog){
                dialog.dismiss();
            }
        }
        Global.dialogs.add(this);

        pbDlgWaitingOrder = findViewById(R.id.pb_dlgwaitingorder);
        tvDlgWaitingOrderMessage = findViewById(R.id.tv_dlgwaitingorder_message);
        clDlgWaitingOrderCancel = findViewById(R.id.cl_dlgwaitingorder_cancel);
        tvDlgWaitingOrder2 = findViewById(R.id.tv_dlgwaitingorder_2);
        tvDlgWaitingOrder3 = findViewById(R.id.tv_dlgwaitingorder_3);

        tvDlgWaitingOrderMessage.setText("매장에서 주문을 확인하고 있습니다\n잠시만 기다려주세요...");

        // 주문접수 상태 불러오기
        new Thread(() -> {
            if (Global.socketManager.isSocketConnected()) {
                try {
                    String strRecvData = Global.socketManager.recv();
                    JSONObject recvData = new JSONObject(strRecvData);
                    String responseCode = recvData.getString("response_code");

                    if (responseCode.equals(Global.Network.Response.ORDER_ACCEPT)) {
                        // 주문이 수락되었을 때
                        if (!orderCancel) {
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("결제 중입니다\n잠시만 기다려주세요...");
                                tvDlgWaitingOrder2.setText("확인");
                                tvDlgWaitingOrder3.setVisibility(View.GONE);
                                clDlgWaitingOrderCancel.setEnabled(false);
                                clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.gray));
                            });
                        }

                        JSONObject sendData = new JSONObject();
                        sendData.accumulate("request_code", Global.Network.Request.ORDER_CANCEL);

                        JSONObject message = new JSONObject();
                        message.accumulate("cancel", orderCancel);

                        sendData.accumulate("message", message);
                        Global.socketManager.send(sendData.toString());

                        strRecvData = Global.socketManager.recv();
                        recvData = new JSONObject(strRecvData);
                        responseCode = recvData.getString("response_code");

                        ((PayActivity) context).runOnUiThread(() -> {
                            pbDlgWaitingOrder.setVisibility(View.GONE);
                            clDlgWaitingOrderCancel.setEnabled(true);
                            clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
                        });

                        if (responseCode.equals(Global.Network.Response.PAY_SUCCESS)) {
                            // 결제 성공시
                            int orderNumber = recvData.getJSONObject("message").getInt("order_number");

                            SQLiteManager sqLiteManager = new SQLiteManager(context);
                            sqLiteManager.openDatabase();
                            if (sqLiteManager.saveOrder(orderNumber, companyName, orderData.getJSONObject("message"))) {

                            } else {
                                ((PayActivity) context).runOnUiThread(() -> {
                                    Toast.makeText(context, "주문내역 저장 실패", Toast.LENGTH_SHORT).show();
                                });
                            }
                            sqLiteManager.closeDatabase();

                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("결제가 완료되었습니다\n주문하신 상품이 준비되면 알려드리겠습니다");
                            });
                        } else if (responseCode.equals(Global.Network.Response.PAY_FAILED_WRONG_PASSWORD)) {
                            // 결제 실패시
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("카드 비밀번호가 틀렸습니다\n확인 후 다시 시도해주세요");
                            });
                        } else if (responseCode.equals(Global.Network.Response.PAY_FAILED_NOT_ENOUGH_MONEY)) {
                            // 결제 실패시
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("잔액이 부족합니다\n확인 후 다시 시도해주세요");
                            });
                        } else if (responseCode.equals(Global.Network.Response.ORDER_CANCEL_SUCCESS)) {
                            // 주문취소 성공시
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("주문이 취소되었습니다");
                            });
                        } else if (responseCode.equals(Global.Network.Response.ORDER_CANCEL_FAILED)) {
                            // 주문취소 실패시
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("주문취소에 실패했습니다\n매장에 문의해주세요");
                            });
                        } else {
                            ((PayActivity) context).runOnUiThread(() -> {
                                tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                            });
                        }
                    } else if (responseCode.equals(Global.Network.Response.ORDER_REFUSE)) {
                        // 주문이 거부되었을 때
                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrder2.setText("확인");
                            tvDlgWaitingOrder3.setVisibility(View.GONE);
                            pbDlgWaitingOrder.setVisibility(View.GONE);
                            tvDlgWaitingOrderMessage.setText("현재 매장이 바빠 주문 접수가 불가능합니다\n나중에 다시 시도해주세요");
                            clDlgWaitingOrderCancel.setEnabled(true);
                            clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
                        });
                    } else {
                        ((PayActivity) context).runOnUiThread(() -> {
                            tvDlgWaitingOrder2.setText("확인");
                            tvDlgWaitingOrder3.setVisibility(View.GONE);
                            pbDlgWaitingOrder.setVisibility(View.GONE);
                            tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                            clDlgWaitingOrderCancel.setEnabled(true);
                            clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
                        });
                    }
                } catch (Exception e) {
                    ((PayActivity) context).runOnUiThread(() -> {
                        tvDlgWaitingOrder2.setText("확인");
                        tvDlgWaitingOrder3.setVisibility(View.GONE);
                        pbDlgWaitingOrder.setVisibility(View.GONE);
                        tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                        clDlgWaitingOrderCancel.setEnabled(true);
                        clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
                    });
                    e.printStackTrace();
                }
            } else {
                ((PayActivity) context).runOnUiThread(() -> {
                    tvDlgWaitingOrder2.setText("확인");
                    tvDlgWaitingOrder3.setVisibility(View.GONE);
                    pbDlgWaitingOrder.setVisibility(View.GONE);
                    tvDlgWaitingOrderMessage.setText("매장과 연결 도중 문제가 발생했습니다\n매장에 문의해주세요");
                    clDlgWaitingOrderCancel.setEnabled(true);
                    clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
                });
            }
        }).start();

        // 주문취소 버튼이 눌렸을 경우
        clDlgWaitingOrderCancel.setOnClickListener(view -> {
            if (tvDlgWaitingOrder2.getText().toString().equals("확인")) {
                dismiss();
            } else {
                orderCancel = true;
                tvDlgWaitingOrderMessage.setText("주문을 취소하셨습니다\n잠시만 기다려주세요...");
                tvDlgWaitingOrder2.setText("확인");
                tvDlgWaitingOrder3.setVisibility(View.GONE);
                clDlgWaitingOrderCancel.setEnabled(false);
                clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.gray));
            }
        });
    }
}

