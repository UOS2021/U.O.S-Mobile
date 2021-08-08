package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.HttpManager;

import org.json.JSONObject;

public class ChangePwDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgChangePwClose;
    private TextInputLayout tilDlgChangePwChangePw;
    private AppCompatButton btnDlgChangePwApply;

    public ChangePwDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context);
        this.context = context;

        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_changepw);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    private void init() {
        ibtnDlgChangePwClose = findViewById(R.id.ibtn_dlgchangepw_close);
        tilDlgChangePwChangePw = findViewById(R.id.til_dlgchangepw_changepw);
        btnDlgChangePwApply = findViewById(R.id.btn_dlgchangepw_apply);

        ibtnDlgChangePwClose.setOnClickListener(view -> {
            dismiss();
        });

        btnDlgChangePwApply.setOnClickListener(view -> {
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.CHANGE_PW);

                JSONObject message = new JSONObject();
                message.accumulate("id", Constants.User.id);
                message.accumulate("change_pw", tilDlgChangePwChangePw.getEditText().getText().toString());
                message.accumulate("type", Constants.User.type);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Constants.Network.Response.CHANGE_PW_SUCCESS)) {
                    Toast.makeText(context, "변경되었습니다", Toast.LENGTH_SHORT).show();
                } else if (responseCode.equals(Constants.Network.Response.CHANGE_PW_FAILED)) {
                    // 비밀번호 변경 실패
                    Toast.makeText(context, "비밀번호 변경 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    // 비밀번호 변경 실패 - 기타 오류
                    Toast.makeText(context, "비밀번호 변경 실패(기타): " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

            dismiss();
        });
    }
}
