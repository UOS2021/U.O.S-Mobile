package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.activity.LoginActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class WithdrawalDialog extends UosDialog {
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

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        ibtnDlgWithdrawalClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgwithdrawal_close);
        tilDlgWithdrawalPw = findViewById(com.uos.uos_mobile.R.id.til_dlgwithdrawal_pw);
        llDlgWithdrawalApply = findViewById(com.uos.uos_mobile.R.id.ll_dlgwithdrawal_apply);

        llDlgWithdrawalApply.setEnabled(false);
        llDlgWithdrawalApply.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));

        /* 닫기 버튼이 눌렸을 경우 */
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

        /* 회원탈퇴 버튼이 눌렸을 경우 */
        llDlgWithdrawalApply.setOnClickListener(view -> {
            try {
                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("pw", tilDlgWithdrawalPw.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.WITHDRAWAL);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.WITHDRAWAL_SUCCESS)) {

                    /* 회원탈퇴 성공 */

                    Toast.makeText(context, "탈퇴되었습니다", Toast.LENGTH_SHORT).show();
                    SharedPreferencesManager.open(context, Global.SharedPreference.APP_DATA);
                    SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, false);
                    SharedPreferencesManager.save(Global.SharedPreference.USER_ID, "");
                    SharedPreferencesManager.save(Global.SharedPreference.USER_PW, "");
                    SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, "");
                    SharedPreferencesManager.close();

                    UosActivity.startFromActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
                } else if (responseCode.equals(Global.Network.Response.WITHDRAWAL_FAIL_PW_NOT_CORRECT)) {

                    /* 회원탈퇴 실패 - 비밀번호 불일치 */

                    tilDlgWithdrawalPw.setError("비밀번호가 일치하지 않습니다");
                    tilDlgWithdrawalPw.setErrorEnabled(true);

                } else {

                    /* 회원탈퇴 실패 - 기타 오류 */

                    Toast.makeText(context, "회원탈퇴 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
