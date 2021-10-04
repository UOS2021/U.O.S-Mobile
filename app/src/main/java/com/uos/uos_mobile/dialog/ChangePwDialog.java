package com.uos.uos_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.R;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class ChangePwDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgChangePwClose;
    private TextInputLayout tilDlgChangePwCurrentPw;
    private TextInputLayout tilDlgChangePwChangePw;
    private TextInputLayout tilDlgChangePwCheckPw;
    private TextView tvDlgChangePwApply;

    public ChangePwDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_changepw);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    @Override
    public void dismiss() {
        Global.dialogs.remove(this);
        super.dismiss();
    }

    private void init() {
        for (Dialog dialog : Global.dialogs) {
            if (dialog instanceof ChangePwDialog) {
                dialog.dismiss();
            }
        }
        Global.dialogs.add(this);

        ibtnDlgChangePwClose = findViewById(R.id.ibtn_dlgchangepw_close);
        tilDlgChangePwCurrentPw = findViewById(R.id.til_dlgchangepw_currentpw);
        tilDlgChangePwChangePw = findViewById(R.id.til_dlgchangepw_changepw);
        tilDlgChangePwCheckPw = findViewById(R.id.til_dlgchangepw_checkpw);
        tvDlgChangePwApply = findViewById(R.id.tv_dlgchangepw_apply);

        tilDlgChangePwChangePw.setCounterEnabled(true);
        tilDlgChangePwChangePw.setCounterMaxLength(30);

        tvDlgChangePwApply.setTextColor(context.getResources().getColor(R.color.color_light));
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

                if (result == Global.Pattern.LENGTH_SHORT) {
                    tilDlgChangePwChangePw.setError("바꿀 비밀번호는 8자리 이상이어야 합니다");
                    tilDlgChangePwChangePw.setErrorEnabled(true);
                } else if (result == Global.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilDlgChangePwChangePw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilDlgChangePwChangePw.setErrorEnabled(true);
                } else {
                    tilDlgChangePwChangePw.setError(null);
                    tilDlgChangePwChangePw.setErrorEnabled(false);
                }
                if (!tilDlgChangePwCheckPw.getEditText().getText().toString().equals(tilDlgChangePwChangePw.getEditText().getText().toString())) {
                    tilDlgChangePwCheckPw.setError("비밀번호가 일치하지 않습니다");
                    tilDlgChangePwCheckPw.setErrorEnabled(true);
                } else {
                    tilDlgChangePwCheckPw.setError(null);
                    tilDlgChangePwCheckPw.setErrorEnabled(false);
                }
                checkInputState();
            }
        });

        tilDlgChangePwCheckPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilDlgChangePwChangePw.getEditText().getText().toString())) {
                    tilDlgChangePwCheckPw.setError("비밀번호가 일치하지 않습니다");
                    tilDlgChangePwCheckPw.setErrorEnabled(true);
                } else {
                    tilDlgChangePwCheckPw.setError(null);
                    tilDlgChangePwCheckPw.setErrorEnabled(false);
                }
                checkInputState();
            }
        });

        tvDlgChangePwApply.setOnClickListener(view -> {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(R.color.color_light));
            tvDlgChangePwApply.setEnabled(false);
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Global.Network.Request.CHECK_PW);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("pw", tilDlgChangePwCurrentPw.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHECKPW_SUCCESS)) {
                    // 비밀번호 확인 성공
                    sendData = new JSONObject();
                    sendData.put("request_code", Global.Network.Request.CHANGE_PW);

                    message = new JSONObject();
                    message.accumulate("id", Global.User.id);
                    message.accumulate("change_pw", tilDlgChangePwChangePw.getEditText().getText().toString());
                    message.accumulate("type", Global.User.type);

                    sendData.accumulate("message", message);

                    recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                    responseCode = recvData.getString("response_code");

                    if (responseCode.equals(Global.Network.Response.CHANGE_PW_SUCCESS)) {
                        // 비밀번호 변경 성공
                        Toast.makeText(context, "변경되었습니다", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else if (responseCode.equals(Global.Network.Response.CHANGE_PW_FAILED)) {
                        // 비밀번호 변경 실패
                        Toast.makeText(context, "비밀번호 변경 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                        // 서버 연결 실패
                        Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        // 비밀번호 변경 실패 - 기타 오류
                        Toast.makeText(context, "비밀번호 변경 실패(기타): " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (responseCode.equals(Global.Network.Response.LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT)) {
                        // 비밀번호 확인 실패
                        tilDlgChangePwCurrentPw.setError("비밀번호가 일치하지 않습니다");
                        tilDlgChangePwCurrentPw.setErrorEnabled(true);
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
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(R.color.black));
            tvDlgChangePwApply.setEnabled(true);
        });
    }

    private void checkInputState() {
        if (tilDlgChangePwCurrentPw.getEditText().getText().toString().length() > 0
                && PatternManager.checkPw(tilDlgChangePwChangePw.getEditText().getText().toString()) == Global.Pattern.OK
                && tilDlgChangePwChangePw.getEditText().getText().toString().equals(tilDlgChangePwCheckPw.getEditText().getText().toString())) {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(R.color.black));
            tvDlgChangePwApply.setEnabled(true);
        } else {
            tvDlgChangePwApply.setTextColor(context.getResources().getColor(R.color.color_light));
            tvDlgChangePwApply.setEnabled(false);
        }
    }
}
