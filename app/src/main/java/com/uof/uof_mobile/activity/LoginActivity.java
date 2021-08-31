package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.RegisterTypeDialog;
import com.uof.uof_mobile.manager.HttpManager;
import com.uof.uof_mobile.manager.SharedPreferenceManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;

import java.security.acl.Owner;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilLoginId;
    private TextInputLayout tilLoginPw;
    private Button btnLoginLogin;
    private TextView tvLoginRegister;
    private LinearLayoutCompat llLoginLoginLayout;
    private CheckBox cbloginispartner;
    private Button btnLoginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);

        tilLoginId = findViewById(R.id.til_login_id);
        tilLoginPw = findViewById(R.id.til_login_pw);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        tvLoginRegister = findViewById(R.id.tv_login_register);
        llLoginLoginLayout = findViewById(R.id.ll_login_loginlayout);
        cbloginispartner = findViewById(R.id.cb_login_ispartner);
        btnLoginPass = findViewById(R.id.btn_login_pass);

        // 기본 데이터 설정
        Intent intent = getIntent();

        // 기본 UI 상태 설정
        btnLoginLogin.setEnabled(false);
        llLoginLoginLayout.setVisibility(View.VISIBLE);


        // 프리패스
        btnLoginPass.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, LobbyActivity.class));
            finish();
        });

        // 로그인 - 아이디 입력란이 수정되었을 경우
        tilLoginId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilLoginId.setErrorEnabled(false);
                tilLoginPw.setErrorEnabled(false);
                tilLoginId.setError(null);
                tilLoginPw.setError(null);
                btnLoginLogin.setEnabled(checkLogin());
            }
        });

        // 로그인 - 비밀번호 입력란이 수정되었을 경우
        tilLoginPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilLoginId.setErrorEnabled(false);
                tilLoginPw.setErrorEnabled(false);
                tilLoginId.setError(null);
                tilLoginPw.setError(null);
                btnLoginLogin.setEnabled(checkLogin());
            }
        });

        // 로그인 버튼이 눌렸을 경우
        btnLoginLogin.setOnClickListener(view -> {
            login();
        });

        // 회원가입 TextView가 눌렸을 경우
        tvLoginRegister.setOnClickListener(view -> {
            new RegisterTypeDialog(LoginActivity.this, true, true,
                    new RegisterTypeDialog.RegisterTypeDialogListener() {
                        @Override
                        public void onCustomerClick() {
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 0);    //고객
                            startActivity(intent);  //다음 activity로 넘어가기
                        }

                        @Override
                        public void onUofPartnerClick() {
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 1);    //파트너
                            startActivity(intent);  //다음 activity로 넘어가기
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    }).show();
        });

        // 자동 로그인 시
        SharedPreferenceManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
        if (SharedPreferenceManager.load(Global.SharedPreference.IS_LOGINED, false)) {
            tilLoginId.getEditText().setText(SharedPreferenceManager.load(Global.SharedPreference.USER_ID, ""));
            tilLoginPw.getEditText().setText(SharedPreferenceManager.load(Global.SharedPreference.USER_PW, ""));
            cbloginispartner.setChecked(SharedPreferenceManager.load(Global.SharedPreference.USER_TYPE, "").equals("uofpartner"));
            login();
        }
        SharedPreferenceManager.close();
    }

    // 로그인 동작
    private void login() {
        btnLoginLogin.setEnabled(false);

        // 로그인 창일 경우
        try {
            JSONObject sendData = new JSONObject();
            sendData.put("request_code", Global.Network.Request.LOGIN);

            JSONObject message = new JSONObject();
            message.accumulate("id", tilLoginId.getEditText().getText().toString());
            message.accumulate("pw", tilLoginPw.getEditText().getText().toString());
            if (cbloginispartner.isChecked()) {
                message.accumulate("type", "uofpartner");
            } else {
                message.accumulate("type", "customer");
            }

            sendData.accumulate("message", message);

            JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

            String responseCode = recvData.getString("response_code");

            if (responseCode.equals(Global.Network.Response.LOGIN_SUCCESS)) {
                // 로그인 성공
                JSONObject userData = recvData.getJSONObject("message");
                Global.User.id = tilLoginId.getEditText().getText().toString();
                Global.User.name = userData.getString("name");
                Global.User.phone = userData.getString("phone");
                Global.User.type = userData.getString("type");

                SharedPreferenceManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                SharedPreferenceManager.save(Global.SharedPreference.USER_ID, Global.User.id);
                SharedPreferenceManager.save(Global.SharedPreference.USER_PW, tilLoginPw.getEditText().getText().toString());
                SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, Global.User.type);
                SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, true);
                SharedPreferenceManager.close();
                if(cbloginispartner.isChecked()){
                    //파트너 로그인
                    startActivity(new Intent(LoginActivity.this, OwnerLobbyActivity.class));
                }
                else{
                    startActivity(new Intent(LoginActivity.this, LobbyActivity.class));
                }
                finish();
            } else if (responseCode.equals(Global.Network.Response.LOGIN_FAILED_ID_NOT_EXIST)) {
                // 로그인 실패 - 아이디 없음
                tilLoginId.setError("아이디가 존재하지 않습니다");
                tilLoginId.setErrorEnabled(true);
            } else if (responseCode.equals(Global.Network.Response.LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT)) {
                // 로그인 실패 - 비밀번호 틀림
                tilLoginPw.setError("비밀번호가 일치하지 않습니다");
                tilLoginPw.setErrorEnabled(true);
            } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                // 서버 연결 실패
                Toast.makeText(LoginActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
            } else {
                // 로그인 실패 - 기타 오류
                Toast.makeText(LoginActivity.this, "로그인 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        btnLoginLogin.setEnabled(true);
    }

    // 로그인 시 아이디, 비밀번호 입력란 확인
    private boolean checkLogin() {
        return tilLoginId.getEditText().getText().toString().length() > 0 && tilLoginPw.getEditText().getText().toString().length() > 0;
    }
}