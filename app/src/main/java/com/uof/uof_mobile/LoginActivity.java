package com.uof.uof_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivLoginRecognizeQR;
    private TextInputLayout tilLoginId;
    private TextInputLayout tilLoginPw;
    private TextInputLayout tilLoginRegisterId;
    private TextInputLayout tilLoginRegisterPw;
    private TextInputLayout tilLoginRegisterPwChk;
    private TextInputLayout tilLoginRegisterName;
    private TextInputLayout tilLoginRegisterPhoneNumber;
    private Button btnLoginLogin;
    private Button btnLoginRegister;
    private TextView tvLoginRegister;
    private TextView tvLoginReturnLogin;
    private LinearLayoutCompat llLoginLoginLayout;
    private LinearLayoutCompat llLoginRegisterLayout;

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
        tilLoginRegisterId = findViewById(R.id.til_login_registerid);
        tilLoginRegisterPw = findViewById(R.id.til_login_registerpw);
        tilLoginRegisterPwChk = findViewById(R.id.til_login_registerpwchk);
        tilLoginRegisterName = findViewById(R.id.til_login_registername);
        tilLoginRegisterPhoneNumber = findViewById(R.id.til_login_registerphonenumber);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        btnLoginRegister = findViewById(R.id.btn_login_register);
        tvLoginRegister = findViewById(R.id.tv_login_register);
        tvLoginReturnLogin = findViewById(R.id.tv_login_returnlogin);
        llLoginLoginLayout = findViewById(R.id.ll_login_loginlayout);
        llLoginRegisterLayout = findViewById(R.id.ll_login_registerlayout);

        // 기본 데이터 설정


        // 기본 UI 상태 설정
        btnLoginLogin.setEnabled(false);
        llLoginLoginLayout.setVisibility(View.VISIBLE);
        llLoginRegisterLayout.setVisibility(View.GONE);

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

        // 회원가입 - 이름 입력란이 수정되었을 경우
        tilLoginRegisterName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterName(editable.toString());

                if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilLoginRegisterName.setErrorEnabled(true);
                    tilLoginRegisterName.setError("이름은 한글만 가능합니다");
                } else {
                    tilLoginRegisterName.setErrorEnabled(false);
                    tilLoginRegisterName.setError(null);
                }

                btnLoginRegister.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 아이디 입력란이 수정되었을 경우
        tilLoginRegisterId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterId(editable.toString());

                if (result == Constants.Pattern.LENGTH_SHORT) {
                    tilLoginRegisterId.setErrorEnabled(true);
                    tilLoginRegisterId.setError("아이디는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilLoginRegisterId.setErrorEnabled(true);
                    tilLoginRegisterId.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilLoginRegisterId.setErrorEnabled(false);
                    tilLoginRegisterId.setError(null);
                }

                btnLoginRegister.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 비밀번호 입력란이 수정되었을 경우
        tilLoginRegisterPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterPw(editable.toString());

                if (result == Constants.Pattern.LENGTH_SHORT) {
                    tilLoginRegisterPw.setErrorEnabled(true);
                    tilLoginRegisterPw.setError("비밀번호는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilLoginRegisterPw.setErrorEnabled(true);
                    tilLoginRegisterPw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilLoginRegisterPw.setErrorEnabled(false);
                    tilLoginRegisterPw.setError(null);
                }

                if (!editable.toString().equals(tilLoginRegisterPwChk.getEditText().getText().toString())) {
                    tilLoginRegisterPw.setErrorEnabled(true);
                    tilLoginRegisterPwChk.setError("비밀번호가 일치하지 않습니다");
                } else {
                    tilLoginRegisterPw.setErrorEnabled(false);
                    tilLoginRegisterPwChk.setError(null);
                }

                btnLoginRegister.setEnabled(checkRegister());
            }
        });

        // 회원가입 - 비밀번호 재확인 입력란이 수정되었을 경우
        tilLoginRegisterPwChk.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilLoginRegisterPw.getEditText().getText().toString())) {
                    tilLoginRegisterPwChk.setErrorEnabled(true);
                    tilLoginRegisterPwChk.setError("비밀번호가 일치하지 않습니다");
                    btnLoginRegister.setEnabled(false);
                } else {
                    tilLoginRegisterPwChk.setErrorEnabled(false);
                    tilLoginRegisterPwChk.setError(null);
                    btnLoginRegister.setEnabled(checkRegister());
                }
            }
        });

        // 회원가입 - 전화번호 입력란이 수정되었을 경우
        tilLoginRegisterPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterPhoneNumber(editable.toString());

                if(result == Constants.Pattern.LENGTH_SHORT || result == Constants.Pattern.NOT_ALLOWED_CHARACTER){
                    tilLoginRegisterPhoneNumber.setErrorEnabled(true);
                    tilLoginRegisterPhoneNumber.setError("전화번호 형식이 맞지 않습니다");
                } else{
                    tilLoginRegisterPhoneNumber.setErrorEnabled(false);
                    tilLoginRegisterPhoneNumber.setError(null);
                }

                btnLoginRegister.setEnabled(checkRegister());
            }
        });

        // 로그인 버튼이 눌렸을 경우
        btnLoginLogin.setOnClickListener(view -> {
            btnLoginLogin.setEnabled(false);

            // 로그인 창일 경우
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", "1002");

                JSONObject message = new JSONObject();
                message.accumulate("id", tilLoginId.getEditText().getText().toString());
                message.accumulate("pw", tilLoginPw.getEditText().getText().toString());

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String requestCode = recvData.getString("request_code");

                if (requestCode.equals("0003")) {
                    // 로그인 성공 - LobbyActivity로 이동
                    Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                    startActivity(intent);
                } else if (requestCode.equals("0004")) {
                    // 로그인 실패 - 아이디 없음
                    tilLoginId.setErrorEnabled(true);
                    tilLoginId.setError("아이디가 존재하지 않습니다");
                } else if (requestCode.equals("0005")) {
                    // 로그인 실패 - 비밀번호 틀림
                    tilLoginId.setErrorEnabled(true);
                    tilLoginId.setError("비밀번호가 일치하지 않습니다");
                } else {
                    // 로그인 실패 - 기타 오류
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            btnLoginLogin.setEnabled(true);
        });

        // 회원가입 버튼이 눌렸을 경우
        btnLoginRegister.setOnClickListener(view -> {
            // 회원가입 창일 경우
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", "1001");

                JSONObject message = new JSONObject();
                message.put("id", tilLoginRegisterId.getEditText().getText().toString());
                message.put("pw", tilLoginRegisterPw.getEditText().getText().toString());
                message.put("name", tilLoginRegisterName.getEditText().getText().toString());
                message.put("phone", tilLoginRegisterPhoneNumber.getEditText().getText().toString());

                sendData.putOpt("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://" + Constants.Network.EXTERNAL_SERVER_IP + ":" + Constants.Network.EXTERNAL_SERVER_PORT, sendData.toString()}).get());

                String requestCode = recvData.getString("request_code");

                if (requestCode.equals("0001")) {
                    // 회원가입 성공 - 로그인창 표시
                    llLoginLoginLayout.setVisibility(View.VISIBLE);
                    llLoginRegisterLayout.setVisibility(View.GONE);
                } else if (requestCode.equals("0002")) {
                    // 회원가입 실패 - 아이디 중복
                    tilLoginRegisterId.setErrorEnabled(true);
                    tilLoginRegisterId.setError("해당 아이디는 이미 사용중입니다");
                } else {
                    // 회원가입 실패 - 기타 오류
                    Toast.makeText(LoginActivity.this, "회원가입 실패: " + recvData.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // 회원가입 TextView가 눌렸을 경우
        tvLoginRegister.setOnClickListener(view -> {
            llLoginLoginLayout.setVisibility(View.GONE);
            llLoginRegisterLayout.setVisibility(View.VISIBLE);
        });

        // 로그인 창 돌아가기 TextView가 눌렸을 경우
        tvLoginReturnLogin.setOnClickListener(view -> {
            llLoginLoginLayout.setVisibility(View.VISIBLE);
            llLoginRegisterLayout.setVisibility(View.GONE);
        });
    }

    // 로그인 시 아이디, 비밀번호 입력란 확인
    private boolean checkLogin() {
        return tilLoginId.getEditText().getText().toString().length() > 0 && tilLoginPw.getEditText().getText().toString().length() > 0;
    }

    // 회원가입 시 아이디, 비밀번호, 이름, 전화번호 확인
    private boolean checkRegister() {
        return checkRegisterId(tilLoginRegisterId.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPw(tilLoginRegisterPw.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterName(tilLoginRegisterName.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPhoneNumber(tilLoginRegisterPhoneNumber.getEditText().getText().toString()) == Constants.Pattern.OK;
    }

    // 회원가입 - 아이디 패턴 및 보안 확인
    private int checkRegisterId(String id) {
        if (id.length() < 8) {
            return Constants.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", id)) {
            return Constants.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Constants.Pattern.OK;
        }
    }

    // 회원가입 - 비밀번호 패턴 및 보안 확인
    private int checkRegisterPw(String pw) {
        if (pw.length() < 8) {
            return Constants.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", pw)) {
            return Constants.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Constants.Pattern.OK;
        }
    }

    // 회원가입 - 이름 패턴 확인
    private int checkRegisterName(String name) {
        if (name.length() == 0) {
            return Constants.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[ㄱ-ㅎ가-힣]+$", name)) {
            return Constants.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Constants.Pattern.OK;
        }
    }

    // 회원가입 - 전화번호 패턴 확인
    private int checkRegisterPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            return Constants.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", phoneNumber)) {
            return Constants.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Constants.Pattern.OK;
        }
    }
}