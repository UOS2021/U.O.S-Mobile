package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.uof.uof_mobile.R;

public class ShowQRDialog extends Dialog {
    private final Context context;
    private ImageButton ibtnDlgShowqrClose;
    private ImageView ivDlgShowqrQRcode;

    public ShowQRDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_showqr);

        init();
    }

    private void init() {
        ibtnDlgShowqrClose = findViewById(R.id.ibtn_dlgshowqr_close);
        ivDlgShowqrQRcode = findViewById(R.id.iv_dlgshowqr_qrcode);

        ibtnDlgShowqrClose.setOnClickListener(view -> {
            dismiss();
        });

        //qr코드 Resource 변경하는 부분
        ivDlgShowqrQRcode.setImageResource(R.drawable.qrcode);
    }
}
