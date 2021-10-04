package com.uos.uos_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.R;
import com.uos.uos_mobile.activity.SettingActivity;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class CheckPwDialog extends Dialog {
    private final Context context;
    private TextInputLayout tilDlgCheckPwPw;
    private AppCompatTextView tvDlgCheckPwCancel;
    private AppCompatTextView tvDlgCheckPwOk;

    public CheckPwDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_checkpw);

        init();
    }

    @Override
    public void dismiss() {
        Global.dialogs.remove(this);
        super.dismiss();
    }

    private void init() {
        for(Dialog dialog : Global.dialogs){
            if(dialog instanceof CheckPwDialog){
                dialog.dismiss();
            }
        }
        Global.dialogs.add(this);

        tilDlgCheckPwPw = findViewById(R.id.til_dlgcheckpw_pw);
        tvDlgCheckPwCancel = findViewById(R.id.tv_dlgcheckpw_cancel);
        tvDlgCheckPwOk = findViewById(R.id.tv_dlgcheckpw_ok);

        tvDlgCheckPwCancel.setOnClickListener(view -> {
            dismiss();
        });

        tvDlgCheckPwOk.setOnClickListener(view -> {
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Global.Network.Request.CHECK_PW);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("pw", tilDlgCheckPwPw.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHECKPW_SUCCESS)) {
                    // 비밀번호 확인 성공
                    context.startActivity(new Intent(context, SettingActivity.class));
                } else if (responseCode.equals(Global.Network.Response.LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT)) {
                    // 비밀번호 확인 실패
                    Toast.makeText(context, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                } else {
                    // 비밀번호 확인 실패 - 기타 오류
                    Toast.makeText(context, "비밀번호 확인 실패(기타)", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

            dismiss();
        });
    }
}
