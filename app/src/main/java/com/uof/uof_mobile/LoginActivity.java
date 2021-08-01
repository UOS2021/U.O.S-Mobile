package com.uof.uof_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivLoginRecognizeQR;
    private TextInputLayout tilLoginId;
    private TextInputLayout tilLoginPw;
    private TextInputLayout tilRegisterId;
    private TextInputLayout tilRegisterPw;
    private TextInputLayout tilRegisterName;
    private TextInputLayout tilRegisterPhoneNumber;
    private Button btnLoginLogin;
    private TextView tvLoginRegister;

    private boolean isLoginSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        ivLoginRecognizeQR = findViewById(R.id.iv_login_recognizeqr);
        tilLoginId = findViewById(R.id.til_login_id);
        tilLoginPw = findViewById(R.id.til_login_pw);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        tvLoginRegister = findViewById(R.id.tv_login_register);

        // 기본 데이터 설정
        isLoginSelected = true;

        // 기본 UI 상태 설정
        btnLoginLogin.setEnabled(false);

        // QR 코드 인식하기 ImageView가 눌렸을 경우
        ivLoginRecognizeQR.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, QRRecognitionActivity.class));
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
                btnLoginLogin.setEnabled(checkLogin());
            }
        });

        // 회원가입 - 아이디 입력란이 수정되었을 경우
        tilRegisterId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginLogin.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 비밀번호 입력란이 수정되었을 경우
        tilRegisterPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginLogin.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 이름 입력란이 수정되었을 경우
        tilRegisterName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginLogin.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 전화번호 입력란이 수정되었을 경우
        tilRegisterPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginLogin.setEnabled(checkRegister());
            }
        });

        // 로그인 버튼이 눌렸을 경우
        btnLoginLogin.setOnClickListener(view -> {
            btnLoginLogin.setEnabled(false);
            if (isLoginSelected) {
                // 로그인 창일 경우
                if (true/*GET, POST 통신 결과*/) {
                    // 로그인 성공 - LobbyActivity로 이동
                    btnLoginLogin.setEnabled(true);
                    Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인 실패
                }
            } else {
                // 회원가입 창일 경우
                if (true/*GET, POST 통신 결과*/) {
                    // 회원가입 성공 - 메세지 뜨면 로그인 창으로 변경

                    btnLoginLogin.setEnabled(checkLogin());
                } else {
                    // 회원가입 실패

                }
            }
        });

        // 회원가입 TextView가 눌렸을 경우
        tvLoginRegister.setOnClickListener(view -> {
            isLoginSelected = false;
            tvLoginRegister.setVisibility(View.INVISIBLE);
        });
    }

    // 로그인 시 아이디, 비밀번호 입력란 확인
    private boolean checkLogin() {
        return tilLoginId.getEditText().getText().toString().length() > 0 && tilLoginPw.getEditText().getText().toString().length() > 0;
    }

    // 회원가입 시 아이디, 비밀번호, 이름, 전화번호 확인
    private boolean checkRegister() {
        return checkRegisterId(tilLoginId.getEditText().getText().toString())
                && checkRegisterPw(tilLoginPw.getEditText().getText().toString())
                && checkRegisterName(tilRegisterName.getEditText().getText().toString())
                && checkRegisterPhoneNumber(tilRegisterPhoneNumber.getEditText().getText().toString());
    }

    // 회원가입 - 아이디 패턴 및 보안 확인
    private boolean checkRegisterId(String id) {
        return false;
    }

    // 회원가입 - 비밀번호 패턴 및 보안 확인
    private boolean checkRegisterPw(String pw) {
        return false;
    }

    // 회원가입 - 이름 패턴 확인
    private boolean checkRegisterName(String nick) {
        return false;
    }

    // 회원가입 - 전화번호 패턴 확인
    private boolean checkRegisterPhoneNumber(String phoneNumber) {
        return false;
    }
}