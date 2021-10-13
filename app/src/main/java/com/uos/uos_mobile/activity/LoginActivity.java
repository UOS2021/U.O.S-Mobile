package com.uos.uos_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

import com.uos.uos_mobile.dialog.RegisterTypeDialog;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * U.O.S-Mobile의 로그인을 담당하고 있는 Activity.<br>
 * xml: activity_login.xml<br><br>
 * 일반고객과 U.O.S 파트너의 로그인을 위한 Activity이며 로그인 성공시 앱 내에서 로그아웃, 또는 회원탈퇴를 하기 전까
 * 지 마지막에 로그인에 성공한 계정으로 자동 로그인됩니다.
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
     * 로그인 Button.
     */
    private AppCompatButton btnLoginLogin;

    /**
     * 회원가입 다이얼로그를 표시하는 TextView.
     */
    private AppCompatTextView tvLoginRegister;

    /**
     * 일반고객과 U.O.S 파트너 로그인 구분을 위한 CheckBox.
     */
    private CheckBox cbLoginIsPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_login);

        init();
    }

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    private void init() {
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

        SharedPreferenceManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
        if (SharedPreferenceManager.load(Global.SharedPreference.IS_LOGINED, false)) {

            /* 자동 로그인이 활성화 되어있을 경우 */

            tilLoginId.getEditText().setText(SharedPreferenceManager.load(Global.SharedPreference.USER_ID, ""));
            tilLoginPw.getEditText().setText(SharedPreferenceManager.load(Global.SharedPreference.USER_PW, ""));
            cbLoginIsPartner.setChecked(SharedPreferenceManager.load(Global.SharedPreference.USER_TYPE, "").equals("uospartner"));

            login();
        }
        SharedPreferenceManager.close();
    }

    /**
     * 전체적인 로그인 과정을 모아놓은 함수. 아이디, 비밀번호 입력란에 입력된 데이터와 사용자 구분 체크박스의 데이터
     * 로 송신 데이터를 만들어 외부서버로 전송하고 반환된 결과에 따라 각각 처리하는 역할읗 담당하고 있습니다.
     */
    private void login() {
        btnLoginLogin.setEnabled(false);
        
        try {

            /* 외부서버로 송신할 데이터 설정 */
            JSONObject sendData = new JSONObject();
            JSONObject message = new JSONObject();

            sendData.put("request_code", Global.Network.Request.LOGIN);
            message.accumulate("id", tilLoginId.getEditText().getText().toString());
            message.accumulate("pw", tilLoginPw.getEditText().getText().toString());

            if (cbLoginIsPartner.isChecked()) {
                message.accumulate("type", "uospartner");
            } else {
                message.accumulate("type", "customer");
            }
            sendData.accumulate("message", message);

            /* HttpManager를 통해 외부서버와 http 통신 */
            JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
            String responseCode = recvData.getString("response_code");

            if (responseCode.equals(Global.Network.Response.LOGIN_SUCCESS)) {

                /* 로그인 성공 시 */

                JSONObject userData = recvData.getJSONObject("message");
                Global.User.id = tilLoginId.getEditText().getText().toString();
                Global.User.name = userData.getString("name");
                Global.User.phone = userData.getString("phone");
                Global.User.type = userData.getString("type");

                if(Global.User.type.equals("uospartner")){
                    
                    /* 만약 로그인한 사용자가 U.O.S 파트너일 경우 수신 데이터에서 매장명과 QR코드 추출 */
                    
                    Global.User.companyName = userData.getString("company_name");
                    SharedPreferenceManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                    SharedPreferenceManager.save(Global.SharedPreference.QR_IMAGE, recvData.getJSONObject("message").getString("qr_image"));
                    SharedPreferenceManager.close();
                }

                /* 로그인한 사용자의 데이터를 Global.USER에 저장 */
                SharedPreferenceManager.open(LoginActivity.this, Global.SharedPreference.APP_DATA);
                SharedPreferenceManager.save(Global.SharedPreference.USER_ID, Global.User.id);
                SharedPreferenceManager.save(Global.SharedPreference.USER_PW, tilLoginPw.getEditText().getText().toString());
                SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, Global.User.type);
                SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, true);
                SharedPreferenceManager.close();

                Intent loginActivityIntent = getIntent();

                if (cbLoginIsPartner.isChecked()) {

                    /* U.O.S 파트너가 로그인일 경우 */

                    if (loginActivityIntent.getStringExtra("uosPartnerId") != null) {

                        /* QR코드를 통해 앱에 접속했을 경우 */

                        Toast.makeText(LoginActivity.this, "UOS 파트너는 매장 상품 구매가 불가능합니다", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(LoginActivity.this, OwnerLobbyActivity.class));
                } else {

                    /* 일반고객이 로그인할 경우 */

                    Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                    if (loginActivityIntent.getStringExtra("uosPartnerId") != null) {

                        /* QR코드를 통해 앱을 실행했을 경우 */

                        intent.putExtra("uosPartnerId", loginActivityIntent.getStringExtra("uosPartnerId"));
                    }else if (loginActivityIntent.getStringExtra("orderNumber") != null) {
                        
                        /* Notofication을 통해 앱을 실행했을 경우 */
                        
                        intent.putExtra("orderNumber", loginActivityIntent.getStringExtra("orderNumber"));
                    }
                    startActivity(intent);
                }
                finish();
            } else if (responseCode.equals(Global.Network.Response.LOGIN_FAILED_ID_NOT_EXIST)) {

                /* 로그인 실패 - 아이디 없음 */

                tilLoginId.setError("아이디가 존재하지 않습니다");
                tilLoginId.setErrorEnabled(true);
            } else if (responseCode.equals(Global.Network.Response.LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT)) {

                /* 로그인 실패 - 비밀번호 틀림 */

                tilLoginPw.setError("비밀번호가 일치하지 않습니다");
                tilLoginPw.setErrorEnabled(true);
            } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                /* 로그인 실패 - 서버 연결 실패 */

                Toast.makeText(LoginActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
            } else {

                /* 로그인 실패 - 기타 오류 */

                Toast.makeText(LoginActivity.this, "로그인 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnLoginLogin.setEnabled(true);
    }

    /**
     * 로그인 가능한 조건을 만족시켰는지 확인 후 버튼 활성화 여부를 설정하는 함수. 실행 시 아이디, 비밀번호 입력란의
     * 오류 메세지 상태를 초기화시켜주며 입력란에 입력된 값이 있는지에 따라 로그인 버튼을 활성화/비활성화 시킵니다.
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