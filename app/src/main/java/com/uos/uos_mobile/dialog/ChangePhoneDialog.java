package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ChangePhoneDialog extends UosDialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgChangePhoneClose;
    private AppCompatTextView tvDlgChangePhoneCurrentPhone;
    private TextInputLayout tilDlgChangePhoneChangePhone;
    private TextView tvDlgChangePhoneApply;

    public ChangePhoneDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.dialog_changephone);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(com.uos.uos_mobile.R.style.Anim_FullScreenDialog);

        init();
    }

    protected void init() {
        ibtnDlgChangePhoneClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgchangephone_close);
        tvDlgChangePhoneCurrentPhone = findViewById(com.uos.uos_mobile.R.id.tv_dlgchangephone_currentphone);
        tilDlgChangePhoneChangePhone = findViewById(com.uos.uos_mobile.R.id.til_dlgchangephone_changephone);
        tvDlgChangePhoneApply = findViewById(com.uos.uos_mobile.R.id.tv_dlgchangephone_apply);

        tvDlgChangePhoneCurrentPhone.setText(Global.User.phone);
        tvDlgChangePhoneApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_light));
        tvDlgChangePhoneApply.setEnabled(false);

        ibtnDlgChangePhoneClose.setOnClickListener(view -> {
            dismiss();
        });

        tilDlgChangePhoneChangePhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkPhoneNumber(editable.toString());

                if (result == PatternManager.LENGTH_SHORT || result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    
                    /* ????????? ?????? ???????????? ????????? ?????? ?????? ?????? */
                    
                    tilDlgChangePhoneChangePhone.setError("???????????? ????????? ?????? ????????????");
                    tilDlgChangePhoneChangePhone.setErrorEnabled(true);
                    tvDlgChangePhoneApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.color_light));
                    tvDlgChangePhoneApply.setEnabled(false);
                } else {

                    /* ????????? ?????? ???????????? ????????? ?????? ?????? */

                    tilDlgChangePhoneChangePhone.setError(null);
                    tilDlgChangePhoneChangePhone.setErrorEnabled(false);
                    tvDlgChangePhoneApply.setTextColor(context.getResources().getColor(com.uos.uos_mobile.R.color.black));
                    tvDlgChangePhoneApply.setEnabled(true);
                }
            }
        });
        
        tvDlgChangePhoneApply.setOnClickListener(view -> {
            try {
                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);
                message.accumulate("change_phone", tilDlgChangePhoneChangePhone.getEditText().getText().toString());
                message.accumulate("type", Global.User.type);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CHANGE_PHONE);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CHANGE_PHONE_SUCCESS)) {

                    /* ???????????? ?????? ?????? */
                    Global.User.phone = tilDlgChangePhoneChangePhone.getEditText().getText().toString();
                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                    /* ?????? ?????? ?????? */

                    Toast.makeText(context, "?????? ?????? ????????????", Toast.LENGTH_SHORT).show();
                } else {

                    /* ???????????? ?????? ?????? - ?????? ?????? */

                    Toast.makeText(context, "???????????? ?????? ?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(context, "???????????? ?????? ?????? ????????? ??????????????????", Toast.LENGTH_LONG).show();
            }

            dismiss();
        });
    }
}
