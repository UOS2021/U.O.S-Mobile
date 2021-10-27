package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.activity.LoginActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ChangePwDialog extends UosDialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgChangePwClose;
    private TextInputLayout tilDlgChangePwCurrentPw;
    private TextInputLayout tilDlgChangePwChangePw;
    private TextInputLayout tilDlgChangePwChangePwCheck;
    private TextView tvDlgChangePwApply;

    public ChangePwDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_changepw);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    /**
     * Dialog 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    protected void init() {
        ibtnDlgChangePwClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgchangepw_close);
        tilDlgChangePwCurrentPw = findViewById(com.uos.uos_mobile.R.id.til_dlgchangepw_currentpw);
        tilDlgChangePwChangePw = findViewById(com.uos.uos_mobile.R.id.til_dlgchangepw_changepw);
        tilDlgChangePwChangePwCheck = findViewById(com.uos.uos_mobile.R.id.til_dlgchangepw_changepwcheck);
        tvDlgChangePwApply = findViewById(com.uos.uos_mobile.R.id.tv_dlgchangepw_apply);

        tilDlgChangePwChangePw.setCounterEnabled(true);
        tilDlgChangePwChangePw.setCounterMaxLength(30);

        tvDlgChangePwApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_light));
        tvDlgChangePwApply.setEnabled(false);

        ibtnDlgChangePwClose.setOnClickListener(view -> {
            dismiss();
        });

        tilDlgChangePwCurrentPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilDlgChangePwCurrentPw.setError(null);
                tilDlgChangePwCurrentPw.setErrorEnabled(false);
                checkInputState();
            }
        });

        tilDlgChangePwChangePw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkPw(editable.toString());

                if (result == PatternManager.LENGTH_SHORT) {

                    /* 변경할 비밀번호가 비밀번호 조건을 만족시키지 못할 경우 - 8자리 이하 */

                    tilDlgChangePwChangePw.setError("바꿀 비밀번호는 8자리 이상이어야 합니다");
                    tilDlgChangePwChangePw.setErrorEnabled(true);
                } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {

                    /* 변경할 비밀번호가 비밀번호 조건을 만족시키지 못할 경우 - 허용되지 않은 문자가 포함되어 있을
                     * 경우
                     */

                    tilDlgChangePwChangePw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilDlgChangePwChangePw.setErrorEnabled(true);
                } else {

                    /* 변경할 비밀번호가 비밀번호 조건을 만족할 경우 */

                    tilDlgChangePwChangePw.setError(null);
                    tilDlgChangePwChangePw.setErrorEnabled(false);
                }

                if (!tilDlgChangePwChangePwCheck.getEditText().getText().toString().equals(tilDlgChangePwChangePw.getEditText().getText().toString())) {

                    /* 변경할 비밀번호가 일치하지 않을 경우 */

                    tilDlgChangePwChangePwCheck.setError("비밀번호가 일치하지 않습니다");
                    tilDlgChangePwChangePwCheck.setErrorEnabled(true);
                } else {

                    /* 변경할 비밀번호가 일치할 경우 */

                    tilDlgChangePwChangePwCheck.setError(null);
                    tilDlgChangePwChangePwCheck.setErrorEnabled(false);
                }
                checkInputState();
            }
        });

        tilDlgChangePwChangePwCheck.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilDlgChangePwChangePw.getEditText().getText().toString())) {

                    /* 변경할 비밀번호가 일치하지 않을 경우 */

                    tilDlgChangePwChangePwCheck.setError("비밀번호가 일치하지 않습니다");
                    tilDlgChangePwChangePwCheck.setErrorEnabled(true);
                } else {

                    /* 변경할 비밀번호가 일치할 경우 */

                    tilDlgChangePwChangePwCheck.setError(null);
                    tilDlgChangePwChangePwCheck.setErrorEnabled(false);
                }
                checkInputState();
            }
        });

        tvDlgChangePwApply.setOnClickListener(view -> {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_light));
            tvDlgChangePwApply.setEnabled(false);
            try {
                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("pw", tilDlgChangePwCurrentPw.getEditText().getText().toString());
                message.accumulate("change_pw", tilDlgChangePwChangePw.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CHANGE_PW);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHANGE_PW_SUCCESS)) {

                    /* 비밀번호 변경 성공 */

                    if (Global.User.type.equals("customer")) {
                        try {
                            message = new JSONObject();
                            message.accumulate("customer_id", Global.User.id);

                            sendData = new JSONObject();
                            sendData.accumulate("request_code", Global.Network.Request.CUSTOMER_LOGOUT);
                            sendData.accumulate("message", message);

                            recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                            responseCode = recvData.getString("response_code");

                            if (responseCode.equals(Global.Network.Response.CUSTOMER_LOGOUT_SUCCESS)) {

                                /* 로그아웃 성공 시 */

                                Toast.makeText(context, "비밀번호가 변경되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                            } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                                /* 서버 연결 실패 */

                                Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                            } else {

                                /* 로그아웃 실패 시 */

                                Toast.makeText(context, "비밀번호 변경 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "비밀번호 변경 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    SharedPreferencesManager.open(context, Global.SharedPreference.APP_DATA);
                    SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, false);
                    SharedPreferencesManager.save(Global.SharedPreference.USER_ID, "");
                    SharedPreferencesManager.save(Global.SharedPreference.USER_PW, "");
                    SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, "");
                    SharedPreferencesManager.close();

                    UosActivity.startFromActivity(new Intent(context.getApplicationContext(), LoginActivity.class));

                    dismiss();
                } else if (responseCode.equals(Global.Network.Response.CHANGE_PW_FAIL_PW_NOT_CORRECT)) {

                    /* 비밀번호 변경 실패 - 비밀번호 틀림 */

                    tilDlgChangePwCurrentPw.setError("비밀번호가 일치하지 않습니다");
                    tilDlgChangePwCurrentPw.setErrorEnabled(true);
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                    /* 서버 연결 실패 */

                    Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                } else {

                    /* 비밀번호 변경 실패 - 기타 오류 */

                    Toast.makeText(context, "비밀번호 변경 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.black));
            tvDlgChangePwApply.setEnabled(true);
        });
    }

    private void checkInputState() {
        if (tilDlgChangePwCurrentPw.getEditText().getText().toString().length() > 0
                && PatternManager.checkPw(tilDlgChangePwChangePw.getEditText().getText().toString()) == PatternManager.OK
                && tilDlgChangePwChangePw.getEditText().getText().toString().equals(tilDlgChangePwChangePwCheck.getEditText().getText().toString())) {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.black));
            tvDlgChangePwApply.setEnabled(true);
        } else {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_light));
            tvDlgChangePwApply.setEnabled(false);
        }
    }
}
