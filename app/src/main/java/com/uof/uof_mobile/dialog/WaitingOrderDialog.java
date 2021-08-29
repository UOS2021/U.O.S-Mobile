package com.uof.uof_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.activity.PayActivity;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;

public class WaitingOrderDialog extends AppCompatDialog {
    private final Context context;
    private AppCompatTextView tvDlgWaitingOrderMessage;
    private ConstraintLayout clDlgWaitingOrderCancel;
    private boolean orderCancel;

    public WaitingOrderDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
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

    private void init() {
        tvDlgWaitingOrderMessage = findViewById(R.id.tv_dlgwaitingorder_message);
        clDlgWaitingOrderCancel = findViewById(R.id.cl_dlgwaitingorder_cancel);

        // 주문접수 상태 불러오기
        new Thread(() -> {
            String strRecvData = Global.socketManager.recv();
            try {
                JSONObject recvData = new JSONObject(strRecvData);
                String responseCode = recvData.getString("response_code");

                JSONObject sendData = new JSONObject();
                sendData.accumulate("resquest_code", Global.Network.Request.ORDER_CANCEL);

                JSONObject message = new JSONObject();
                message.accumulate("cancel", orderCancel);

                sendData.accumulate("message", message);

                if(Global.socketManager.isSocketConnected()){
                    Global.socketManager.send(sendData.toString());

                    strRecvData = Global.socketManager.recv();
                    recvData = new JSONObject(strRecvData);

                    if (responseCode.equals(Global.Network.Response.ORDER_ACCEPT)) {
                        ((PayActivity)context).runOnUiThread(() -> {
                            Toast.makeText(context, "매장에서 주문을 수락하였습니다", Toast.LENGTH_SHORT).show();
                        });
                    } else if (responseCode.equals(Global.Network.Response.ORDER_REFUSE)) {
                        ((PayActivity)context).runOnUiThread(() -> {
                            Toast.makeText(context, "매장에서 주문을 거절하였습니다", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        ((PayActivity)context).runOnUiThread(() -> {
                            Toast.makeText(context, "매장 통신 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        });
                    }
                }else{
                    ((PayActivity)context).runOnUiThread(() -> {
                        Toast.makeText(context, "매장 통신 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                ((PayActivity)context).runOnUiThread(() -> {
                    Toast.makeText(context, "매장 통신 중 문제가 발생했습니다: " + e.toString(), Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
            dismiss();
        }).start();

        // 주문취소 버튼이 눌렸을 경우
        clDlgWaitingOrderCancel.setOnClickListener(view -> {
            orderCancel = true;
            tvDlgWaitingOrderMessage.setText("주문을 취소하셨습니다\n잠시만 기다려주세요...");
            clDlgWaitingOrderCancel.setEnabled(false);
            clDlgWaitingOrderCancel.setBackgroundColor(context.getResources().getColor(R.color.gray));
        });
    }
}

