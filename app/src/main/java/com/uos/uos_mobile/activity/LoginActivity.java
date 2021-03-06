package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.dialog.RegisterTypeDialog;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * U.O.S-Mobile의 로그인을 담당하고 있는 Activity.<br>
 * xml: activity_login.xml<br><br>
 *
 * 일반고객과 U.O.S 파트너의 로그인을 위한 Activity이며 로그인 성공시 앱 내에서 로그아웃, 또는 회원탈퇴를 하기
 * 전까지 마지막에 로그인에 성공한 계정으로 자동 로그인됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class LoginActivity extends UosActivity {

    /**
     * 아이디를 입력하는 TextInputLayout.
     */
    private TextInputLayout tilLoginId;

    /**
     * 비밀번호를 입력하는 TextInputLayout.
     */
    private TextInputLayout tilLoginPw;

    /**
     * 로그인 AppCompatButton.
     */
    private AppCompatButton btnLoginLogin;

    /**
     * 회원가입 다이얼로그를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvLoginRegister;

    /**
     * 일반고객과 U.O.S 파트너 로그인 구분을 위한 CheckBox.
     */
    private CheckBox cbLoginIsPartner;

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_login);

        /* xml 파일로부터 ui 불러오기 */
        tilLoginId = findViewById(com.uos.uos_mobile.R.id.til_login_id);
        tilLoginPw = findViewById(com.uos.uos_mobile.R.id.til_login_pw);
        btnLoginLogin = findViewById(com.uos.uos_mobile.R.id.btn_login_login);
        tvLoginRegister = findViewById(com.uos.uos_mobile.R.id.tv_login_register);
        cbLoginIsPartner = findViewById(com.uos.uos_mobile.R.id.cb_login_ispartner);

        /* 기본 UI 상태 설정 */
        btnLoginLogin.setEnabled(false);
        btnLoginLogin.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));

        /* 아이디 입력란이 수정되었을 경우 */
        tilLoginId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginable();
            }
        });

        /*  비밀번호 입력란이 수정되었을 경우 */
        tilLoginPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginable();
            }
        });

        /* 로그인 버튼이 눌렸을 경우 */
        btnLoginLogin.setOnClickListener(view -> {
            login();
        });

        /* 회원가입 버튼(TextView)이 눌렸을 경우 */
        tvLoginRegister.setOnClickListener(view -> {

            /* 회원가입 다이얼로그 호출 */

            new RegisterTypeDialog(LoginActivity.this, true, true,
                    new RegisterTypeDialog.RegisterTypeDialogListener() {
                        @Override
                        public void onCustomerClick() {

                            /* 회원가입 종류가 일반고객일 경우 */

                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 0);
                            startActivity(intent);
                        }

                        @Override
                        public void onUosPartnerClick() {

                            /* 회원가입 종류가 U.O.S 파트너일 경우 */

                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 1);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    }).show();
        });

        SharedPreferencesManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
        if ((Boolean) SharedPreferencesManager.load(Global.SharedPreference.IS_LOGINED, false)) {

            /* 자동 로그인이 활성화 되어있을 경우 */

            tilLoginId.getEditText().setText((String) SharedPreferencesManager.load(Global.SharedPreference.USER_ID, ""));
            tilLoginPw.getEditText().setText((String) SharedPreferencesManager.load(Global.SharedPreference.USER_PW, ""));
            cbLoginIsPartner.setChecked(SharedPreferencesManager.load(Global.SharedPreference.USER_TYPE, "").toString().equals("uospartner"));

            login();
        }
        SharedPreferencesManager.close();
    }

    /**
     * 로그인 과정이 구현되어있으며 아이디, 비밀번호 입력란에 입력된 데이터와 사용자 구분 체크박스의 데이터로 송신할
     * JSONObject를 생성합니다. 해당 JSONObject를 외부서버로 전송하고 반환된 결과에 따라 각각 처리하는 역할을
     * 담당하고 있습니다.
     */
    private void login() {
        btnLoginLogin.setEnabled(false);

        try {

            /* 외부서버로 송신할 데이터 설정 */
            JSONObject message = new JSONObject();
            message.accumulate("id", tilLoginId.getEditText().getText().toString());
            message.accumulate("pw", tilLoginPw.getEditText().getText().toString());
            if (cbLoginIsPartner.isChecked()) {
                message.accumulate("type", "uospartner");
            } else {
                message.accumulate("type", "customer");
                message.accumulate("fcm_token", Global.Firebase.FCM_TOKEN);
            }

            JSONObject sendData = new JSONObject();
            sendData.accumulate("request_code", Global.Network.Request.LOGIN);
            sendData.accumulate("message", message);

            /* HttpManager를 통해 외부서버와 http 통신 */
            JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
            String responseCode = recvData.getString("response_code");

            if (responseCode.equals(Global.Network.Response.LOGIN_SUCCESS_CUSTOMER)) {

                /* 일반고객 로그인 성공 시 */

                JSONObject userData = recvData.getJSONObject("message");
                Global.User.id = tilLoginId.getEditText().getText().toString();
                Global.User.name = userData.getString("name");
                Global.User.phone = userData.getString("phone");
                Global.User.type = "customer";

                /* 로그인한 사용자의 데이터를 Global.USER에 저장 */
                SharedPreferencesManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                SharedPreferencesManager.save(Global.SharedPreference.USER_ID, Global.User.id);
                SharedPreferencesManager.save(Global.SharedPreference.USER_PW, tilLoginPw.getEditText().getText().toString());
                SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, Global.User.type);
                SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, true);
                SharedPreferencesManager.close();

                Intent loginActivityIntent = getIntent();
                Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                if (loginActivityIntent.getStringExtra("uosPartnerId") != null) {

                    /* QR코드를 통해 앱을 실행했을 경우 */

                    intent.putExtra("uosPartnerId", loginActivityIntent.getStringExtra("uosPartnerId"));
                } else if (loginActivityIntent.getIntExtra("orderCode", -1) != -1) {

                    /* Notofication을 통해 앱을 실행했을 경우 */

                    intent.putExtra("orderCode", loginActivityIntent.getIntExtra("orderCode", -1));
                }
                startActivity(intent);
                finish();
            } else if (responseCode.equals(Global.Network.Response.LOGIN_SUCCESS_UOSPARTNER)) {

                /* U.O.S 파트너 로그인 성공 시 */

                JSONObject userData = recvData.getJSONObject("message");
                Global.User.id = tilLoginId.getEditText().getText().toString();
                Global.User.name = userData.getString("name");
                Global.User.phone = userData.getString("phone");
                Global.User.type = "uospartner";
                Global.User.companyName = userData.getString("company_name");
                SharedPreferencesManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                SharedPreferencesManager.save(Global.SharedPreference.QR_IMAGE, recvData.getJSONObject("message").getString("qr_img"));
                SharedPreferencesManager.close();

                /* 로그인한 사용자의 데이터를 Global.USER에 저장 */
                SharedPreferencesManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                SharedPreferencesManager.save(Global.SharedPreference.USER_ID, Global.User.id);
                SharedPreferencesManager.save(Global.SharedPreference.USER_PW, tilLoginPw.getEditText().getText().toString());
                SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, Global.User.type);
                SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, true);
                SharedPreferencesManager.close();

                Intent loginActivityIntent = getIntent();

                if (loginActivityIntent.getStringExtra("uosPartnerId") != null) {

                    /* QR코드를 통해 앱에 접속했을 경우 */

                    Toast.makeText(LoginActivity.this, "UOS 파트너는 매장 상품 구매가 불가능합니다", Toast.LENGTH_SHORT).show();
                } else if (loginActivityIntent.getStringExtra("orderCode") != null) {

                    /* Notofication을 통해 앱을 실행했을 경우 */

                    Toast.makeText(LoginActivity.this, "UOS 파트너는 매장 상품 구매가 불가능합니다", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(LoginActivity.this, UosPartnerLobbyActivity.class));
                finish();
            } else if (responseCode.equals(Global.Network.Response.LOGIN_FAIL_ID_NOT_EXIST)) {

                /* 로그인 실패 - 아이디 없음 */

                tilLoginId.setError("아이디가 존재하지 않습니다");
                tilLoginId.setErrorEnabled(true);
            } else if (responseCode.equals(Global.Network.Response.LOGIN_FAIL_PW_NOT_CORRECT)) {

                /* 로그인 실패 - 비밀번호 틀림 */

                tilLoginPw.setError("비밀번호가 일치하지 않습니다");
                tilLoginPw.setErrorEnabled(true);
            }  else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                /* 서버 연결 실패 */

                Toast.makeText(LoginActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
            } else {

                /* 로그인 실패 - 기타 오류 */

                Toast.makeText(LoginActivity.this, "로그인 시도 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "로그인 시도 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }

        btnLoginLogin.setEnabled(true);
    }

    /**
     * 로그인 가능한 조건을 만족시켰는지 확인 후 버튼 활성화 여부를 설정하는 함수. 실행 시 아이디, 비밀번호 입력란의
     * 오류 메세지 상태를 초기화시켜주며 입력란에 입력된 값이 있는지에 따라 로그인 버튼을 활성화/비활성화시킵니다.
     */
    private void checkLoginable() {

        /* 아이디와 비밀번호 입력란 에러상태 비활성화 */

        tilLoginId.setErrorEnabled(false);
        tilLoginPw.setErrorEnabled(false);
        tilLoginId.setError(null);
        tilLoginPw.setError(null);

        if (tilLoginId.getEditText().getText().toString().length() > 0 && tilLoginPw.getEditText().getText().toString().length() > 0) {

            /* 로그인 버튼 활성화 조건을 만족시켰을 경우 */

            btnLoginLogin.setEnabled(true);
            btnLoginLogin.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
        } else {

            /* 로그인 버튼 활성화 조건을 만족시키지 못했을 경우 */

            btnLoginLogin.setEnabled(false);
            btnLoginLogin.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        }
    }
}