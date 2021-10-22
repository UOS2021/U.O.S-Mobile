package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WaitingOrderAcceptDialog extends UosDialog {
    private final Context context;
    private final String companyName;
    private AppCompatTextView tvDlgWaitingOrderAcceptExplain;
    private ConstraintLayout clDlgWaitingOrderAcceptOk;

    public WaitingOrderAcceptDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, String companyName) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.companyName = companyName;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_waitingorderaccept);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        tvDlgWaitingOrderAcceptExplain = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderaccept_explain);
        clDlgWaitingOrderAcceptOk = findViewById(com.uos.uos_mobile.R.id.cl_dlgwaitingorderaccept_ok);
        
        tvDlgWaitingOrderAcceptExplain.setText(companyName + "에서 주문을 확인하고 있습니다\n잠시만 기다려주세요");

        /* 주문취소 버튼이 눌렸을 경우 */
        clDlgWaitingOrderAcceptOk.setOnClickListener(view -> {
            dismiss();
        });
    }
}

