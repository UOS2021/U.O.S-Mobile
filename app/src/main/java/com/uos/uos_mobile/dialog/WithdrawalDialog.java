package com.uos.uos_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

import com.uos.uos_mobile.activity.LoginActivity;
import com.uos.uos_mobile.activity.SettingActivity;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class WithdrawalDialog extends AppCompatDialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgWithdrawalClose;
    private TextInputLayout tilDlgWithdrawalPw;
    private LinearLayoutCompat llDlgWithdrawalApply;

    public WithdrawalDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.uos.uos_mobile.R.layout.dialog_withdrawal);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    @Override
    public void dismiss() {
        Global.removeDialog(this);
        super.dismiss();
    }

    private void init() {
        Global.addDialog(this, false);

        ibtnDlgWithdrawalClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgwithdrawal_close);
        tilDlgWithdrawalPw = findViewById(com.uos.uos_mobile.R.id.til_dlgwithdrawal_pw);
        llDlgWithdrawalApply = findViewById(com.uos.uos_mobile.R.id.ll_dlgwithdrawal_apply);

        llDlgWithdrawalApply.setEnabled(false);
        llDlgWithdrawalApply.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));

        // 닫기 버튼이 눌렸을 경우
        ibtnDlgWithdrawalClose.setOnClickListener(view -> {
            dismiss();
        });

        tilDlgWithdrawalPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    llDlgWithdrawalApply.setEnabled(false);
                    llDlgWithdrawalApply.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));
                } else {
                    llDlgWithdrawalApply.setEnabled(true);
                    llDlgWithdrawalApply.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                }
                tilDlgWithdrawalPw.setError(null);
                tilDlgWithdrawalPw.setErrorEnabled(false);
            }
        });

        llDlgWithdrawalApply.setOnClickListener(view -> {
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Global.Network.Request.CHECK_PW);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("pw", tilDlgWithdrawalPw.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(Global.Network.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(Global.Network.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHECKPW_SUCCESS)) {
                    // 비밀번호 확인 성공
                    try {
                        sendData = new JSONObject();
                        sendData.put("request_code", Global.Network.Request.WITHDRAWAL);

                        message = new JSONObject();
                        message.accumulate("id", Global.User.id);
                        message.accumulate("type", Global.User.type);

                        sendData.accumulate("message", message);

                        recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(Global.Network.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(Global.Network.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                        responseCode = recvData.getString("response_code");

                        if (responseCode.equals(Global.Network.Response.WITHDRAWAL_SUCCESS)) {
                            Toast.makeText(context, "탈퇴되었습니다", Toast.LENGTH_SHORT).show();
                            SharedPreferenceManager.open(context, Global.SharedPreference.APP_DATA);
                            SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, false);
                            SharedPreferenceManager.save(Global.SharedPreference.USER_ID, "");
                            SharedPreferenceManager.save(Global.SharedPreference.USER_PW, "");
                            SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, "");
                            SharedPreferenceManager.close();
                            for (AppCompatActivity activity : Global.activities) {
                                if (!(activity instanceof SettingActivity)) {
                                    activity.finish();
                                }
                            }
                            dismiss();
                            context.startActivity(new Intent(context, LoginActivity.class));
                        } else if (responseCode.equals(Global.Network.Response.WITHDRAWAL_FAILED)) {
                            // 전화번호 변경 실패
                            Toast.makeText(context, "탈퇴 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            // 전화번호 변경 실패 - 기타 오류
                            Toast.makeText(context, "탈퇴 실패(기타): " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (responseCode.equals(Global.Network.Response.LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT)) {
                        // 비밀번호 확인 실패
                        tilDlgWithdrawalPw.setError("비밀번호가 일치하지 않습니다");
                        tilDlgWithdrawalPw.setErrorEnabled(true);
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                        // 서버 연결 실패
                        Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        // 비밀번호 확인 실패 - 기타 오류
                        Toast.makeText(context, "비밀번호 확인 실패(기타)", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
