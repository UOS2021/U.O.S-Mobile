package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SharedPreferenceManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class ShowQRDialog extends Dialog {
    private final Context context;
    private AppCompatTextView tvDlgShowQrMessage;
    private AppCompatImageButton ibtnDlgShowQrClose;
    private AppCompatImageView ivDlgShowQrImage;

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
        ibtnDlgShowQrClose = findViewById(R.id.ibtn_dlgshowqr_close);
        tvDlgShowQrMessage = findViewById(R.id.tv_dlgshowqr_message);
        ivDlgShowQrImage = findViewById(R.id.iv_dlgshowqr_image);

        ibtnDlgShowQrClose.setOnClickListener(view -> {
            dismiss();
        });

        // QR 코드 Resource 변경하는 부분
        SharedPreferenceManager.open(context, Global.SharedPreference.APP_DATA);
        String strQrImage = SharedPreferenceManager.load(Global.SharedPreference.QR_IMAGE, "");

        if (strQrImage.length() == 0) {
            tvDlgShowQrMessage.setText("저장된 QR 코드가 없습니다");
            ivDlgShowQrImage.setImageDrawable(context.getDrawable(R.drawable.icon_btnclose));
        } else {
            tvDlgShowQrMessage.setText("QR 코드를 인식하여 주문하세요");
            ivDlgShowQrImage.setImageBitmap(UsefulFuncManager.convertStringToBitmap(strQrImage));
        }
    }
}
