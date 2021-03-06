package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

public class RegisterTypeDialog extends UosDialog {
    private final Context context;
    private final RegisterTypeDialogListener registerTypeDialogListener;
    private AppCompatImageButton btnRegisterTypeCustomer;
    private AppCompatImageButton btnRegisterTypeUosPartner;
    private AppCompatButton btnRegisterTypeCancel;

    public RegisterTypeDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable
            , RegisterTypeDialogListener registerTypeDialogListener) {
        super(context);
        this.context = context;
        this.registerTypeDialogListener = registerTypeDialogListener;

        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_registertype);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        btnRegisterTypeCustomer = findViewById(com.uos.uos_mobile.R.id.btn_registertype_customer);
        btnRegisterTypeUosPartner = findViewById(com.uos.uos_mobile.R.id.btn_registertype_uospartner);
        btnRegisterTypeCancel = findViewById(com.uos.uos_mobile.R.id.btn_registertype_cancel);

        btnRegisterTypeCustomer.setOnClickListener(view -> {
            this.registerTypeDialogListener.onCustomerClick();
            dismiss();
        });

        btnRegisterTypeUosPartner.setOnClickListener(view -> {
            this.registerTypeDialogListener.onUosPartnerClick();
            dismiss();
        });

        btnRegisterTypeCancel.setOnClickListener(view -> {
            this.registerTypeDialogListener.onCancelClick();
            dismiss();
        });

        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(com.uos.uos_mobile.R.drawable.border_registertypedialog);
    }

    public interface RegisterTypeDialogListener {
        void onCustomerClick();

        void onUosPartnerClick();

        void onCancelClick();
    }
}
