package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uos.uos_mobile.manager.BasketManager;

public class PayResultDialog extends UosDialog {
    private final Context context;
    private AppCompatTextView tvDlgWaitingOrderAcceptExplain;
    private ConstraintLayout clDlgWaitingOrderAcceptOk;
    private BasketManager basketManager;

    public PayResultDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, BasketManager basketManager) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.basketManager = basketManager;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_payresult);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        tvDlgWaitingOrderAcceptExplain = findViewById(com.uos.uos_mobile.R.id.tv_dlgwaitingorderaccept_explain);
        clDlgWaitingOrderAcceptOk = findViewById(com.uos.uos_mobile.R.id.cl_dlgwaitingorderaccept_ok);

        if(basketManager.getCompanyType().equals("restaurant/pc")){
            tvDlgWaitingOrderAcceptExplain.setText(basketManager.getCompanyName() + "에서 주문을 확인하고 있습니다\n잠시만 기다려주세요");
        }else if(basketManager.getCompanyType().equals("theater")){
            tvDlgWaitingOrderAcceptExplain.setText("결제가 완료되었습니다");
        }

        /* 확인 버튼이 눌렸을 경우 */
        clDlgWaitingOrderAcceptOk.setOnClickListener(view -> {
            dismiss();
        });
    }
}

