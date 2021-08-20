package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.Global;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.HttpManager;

import org.json.JSONObject;

public class ChangePhoneDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgChangePhoneClose;
    private AppCompatTextView tvDlgChangePhoneCurrentPhone;
    private TextInputLayout tilDlgChangePhoneChangePhone;
    private AppCompatButton btnDlgChangePhoneApply;

    public ChangePhoneDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_changephone);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgChangePhoneClose = findViewById(R.id.ibtn_dlgchangephone_close);
        tvDlgChangePhoneCurrentPhone = findViewById(R.id.tv_dlgchangephone_currentphone);
        tilDlgChangePhoneChangePhone = findViewById(R.id.til_dlgchangephone_changephone);
        btnDlgChangePhoneApply = findViewById(R.id.btn_dlgchangephone_apply);

        tvDlgChangePhoneCurrentPhone.setText(Global.User.phone);

        ibtnDlgChangePhoneClose.setOnClickListener(view -> {
            dismiss();
        });

        btnDlgChangePhoneApply.setOnClickListener(view -> {
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Global.Network.Request.CHANGE_PHONE);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("change_phone", tilDlgChangePhoneChangePhone.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHANGE_PHONE_SUCCESS)) {
                    // 전화번호 변경 성공
                    Global.User.phone = tilDlgChangePhoneChangePhone.getEditText().getText().toString();
                    Toast.makeText(context, "변경되었습니다", Toast.LENGTH_SHORT).show();
                } else if (responseCode.equals(Global.Network.Response.CHANGE_PHONE_FAILED)) {
                    // 전화번호 변경 실패
                    Toast.makeText(context, "전화번호 변경 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                } else {
                    // 전화번호 변경 실패 - 기타 오류
                    Toast.makeText(context, "전화번호 변경 실패(기타): " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

            dismiss();
        });
    }
}
