package com.uof.uof_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivLoginRecognizeQR;
    private TextInputLayout tilLoginId;
    private TextInputLayout tilLoginPw;
    private Button btnLoginLogin;
    private TextView tvLoginRegister;

    private boolean isLoginSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        ivLoginRecognizeQR = findViewById(R.id.iv_login_recognizeqr);
        tilLoginId = findViewById(R.id.til_login_id);
        tilLoginPw = findViewById(R.id.til_login_pw);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        tvLoginRegister = findViewById(R.id.tv_login_register);

        isLoginSelected = true;

        // QR 코드 인식하기 ImageView가 눌렸을 경우
        ivLoginRecognizeQR.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, QRRecognitionActivity.class));
        });

        // ID 입력란이 수정되었을 경우
        tilLoginId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkIdPwAvailability();
            }
        });

        // 비밀번호 입력란이 수정되었을 경우
        tilLoginPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkIdPwAvailability();
            }
        });

        // 로그인 버튼이 눌렸을 경우
        btnLoginLogin.setOnClickListener(view -> {
            // 소켓 통신 후 로그인 성공 메세지 뜨면 LobbyActivity로 이동
            if(true/*소켓 통신 결과*/){
                Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });

        // 회원가입 TextView가 눌렸을 경우
        tvLoginRegister.setOnClickListener(view -> {
            isLoginSelected = !isLoginSelected;
            if(isLoginSelected){
                // 로그인 창 표시
            }else{
                // 회원가입 창 표시
            }
        });
    }

    // 아이디, 비밀번호 패턴 및 보안 확인
    private boolean checkIdPwAvailability(){
        return checkIdAvailability(tilLoginId.getEditText().getText().toString()) && checkPwAvailability(tilLoginPw.getEditText().getText().toString());
    }

    // 아이디 패턴 및 보안 확인
    private boolean checkIdAvailability(String id){
        return false;
    }

    // 비밀번호 패턴 및 보안 확인
    private boolean checkPwAvailability(String pw){
        return false;
    }
}