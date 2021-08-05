package com.uof.uof_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayoutCompat llRegisterCustomer;
    private LinearLayoutCompat llRegisterUofPartner;
    private TextInputLayout tilRegisterCustomerId;
    private TextInputLayout tilRegisterCustomerPw;
    private TextInputLayout tilRegisterCustomerPwChk;
    private TextInputLayout tilRegisterCustomerName;
    private TextInputLayout tilRegisterCustomerPhoneNumber;
    private AppCompatButton btnRegisterCustomerRegister;

    private TextInputLayout tilRegisterUofPartnerId;
    private TextInputLayout tilRegisterUofPartnerPw;
    private TextInputLayout tilRegisterUofPartnerPwChk;
    private TextInputLayout tilRegisterUofPartnerName;
    private TextInputLayout tilRegisterUofPartnerPhoneNumber;
    private TextInputLayout tilRegisterCompanyName;
    private TextInputLayout tilRegisterLicenseNumber;
    private Spinner spRegisterCompanyType;
    private TextInputLayout tilRegisterCompanyAddress;
    private AppCompatButton btnRegisterUofPartnerRegister;
    private AppCompatImageView ivRegisterLicenseImage;
    private AppCompatButton btnRegisterLicenseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        //데이터 받아오기
        Intent loadIntent = getIntent();
        int registerType = loadIntent.getExtras().getInt("RegisterType");

        llRegisterCustomer = findViewById(R.id.ll_register_customer);
        llRegisterUofPartner = findViewById(R.id.ll_register_uofpartner);

        tilRegisterCustomerId = findViewById(R.id.til_register_customerid);
        tilRegisterCustomerPw = findViewById(R.id.til_register_customerpw);
        tilRegisterCustomerPwChk = findViewById(R.id.til_register_customerpwchk);
        tilRegisterCustomerName = findViewById(R.id.til_register_customername);
        tilRegisterCustomerPhoneNumber = findViewById(R.id.til_register_customerphonenumber);
        btnRegisterCustomerRegister = findViewById(R.id.btn_register_customerregister);

        tilRegisterUofPartnerId = findViewById(R.id.til_register_uofpartnerid);
        tilRegisterUofPartnerPw = findViewById(R.id.til_register_uofpartnerpw);
        tilRegisterUofPartnerPwChk = findViewById(R.id.til_register_uofpartnerpwchk);
        tilRegisterUofPartnerName = findViewById(R.id.til_register_uofpartnername);
        tilRegisterUofPartnerPhoneNumber = findViewById(R.id.til_register_uofpartnerphonenumber);
        tilRegisterCompanyName = findViewById(R.id.til_register_companyname);
        tilRegisterLicenseNumber = findViewById(R.id.til_register_licensenumber);
        btnRegisterLicenseImage = findViewById(R.id.btn_register_licenseimage);
        ivRegisterLicenseImage = findViewById(R.id.iv_register_licenseimage);
        btnRegisterUofPartnerRegister = findViewById(R.id.btn_register_uofpartnerregister);
        spRegisterCompanyType = findViewById(R.id.sp_register_companytype);
        tilRegisterCompanyAddress = findViewById(R.id.til_register_companyaddress);

        // UI 초기 상태 설정
        // 회원가입 유형 확인
        if (registerType == 0) {
            // 일반 고객
            llRegisterCustomer.setVisibility(View.VISIBLE);
            llRegisterUofPartner.setVisibility(View.GONE);
        } else {
            // U.O.F 파트너
            llRegisterUofPartner.setVisibility(View.VISIBLE);
            llRegisterCustomer.setVisibility(View.GONE);
        }

        spRegisterCompanyType.setPrompt("회사 유형");
        ArrayAdapter companyType = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_companytype, android.R.layout.simple_spinner_dropdown_item);
        companyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegisterCompanyType.setAdapter(companyType);

        btnRegisterCustomerRegister.setEnabled(false);
        btnRegisterUofPartnerRegister.setEnabled(false);

        //// 일반 고객
        // 회원가입 - 아이디 입력란이 수정되었을 경우
        tilRegisterCustomerId.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterCustomerId.setErrorEnabled(true);
                    tilRegisterCustomerId.setError("아이디는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerId.setErrorEnabled(true);
                    tilRegisterCustomerId.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilRegisterCustomerId.setErrorEnabled(false);
                    tilRegisterCustomerId.setError(null);
                }

                btnRegisterCustomerRegister.setEnabled(checkCustomerRegister());
            }
        });

        // 회원가입 - 비밀번호 입력란이 수정되었을 경우
        tilRegisterCustomerPw.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterCustomerPw.setErrorEnabled(true);
                    tilRegisterCustomerPw.setError("비밀번호는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerPw.setErrorEnabled(true);
                    tilRegisterCustomerPw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilRegisterCustomerPw.setErrorEnabled(false);
                    tilRegisterCustomerPw.setError(null);
                }

                if (!editable.toString().equals(tilRegisterCustomerPwChk.getEditText().getText().toString())) {
                    tilRegisterCustomerPw.setErrorEnabled(true);
                    tilRegisterCustomerPwChk.setError("비밀번호가 일치하지 않습니다");
                } else {
                    tilRegisterCustomerPw.setErrorEnabled(false);
                    tilRegisterCustomerPwChk.setError(null);
                }

                btnRegisterCustomerRegister.setEnabled(checkCustomerRegister());
            }
        });

        // 회원가입 - 비밀번호 재확인 입력란이 수정되었을 경우
        tilRegisterCustomerPwChk.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilRegisterCustomerPw.getEditText().getText().toString())) {
                    tilRegisterCustomerPwChk.setErrorEnabled(true);
                    tilRegisterCustomerPwChk.setError("비밀번호가 일치하지 않습니다");
                    btnRegisterCustomerRegister.setEnabled(false);
                } else {
                    tilRegisterCustomerPwChk.setErrorEnabled(false);
                    tilRegisterCustomerPwChk.setError(null);
                    btnRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                }
            }
        });


        // 회원가입 - 이름 입력란이 수정되었을 경우
        tilRegisterCustomerName.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterCustomerName.setErrorEnabled(true);
                    tilRegisterCustomerName.setError("이름은 한글만 가능합니다");
                } else {
                    tilRegisterCustomerName.setErrorEnabled(false);
                    tilRegisterCustomerName.setError(null);
                }

                btnRegisterCustomerRegister.setEnabled(checkCustomerRegister());
            }
        });

        // 회원가입 - 전화번호 입력란이 수정되었을 경우
        tilRegisterCustomerPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterPhoneNumber(editable.toString());

                if (result == Constants.Pattern.LENGTH_SHORT || result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerPhoneNumber.setErrorEnabled(true);
                    tilRegisterCustomerPhoneNumber.setError("전화번호 형식이 맞지 않습니다");
                } else {
                    tilRegisterCustomerPhoneNumber.setErrorEnabled(false);
                    tilRegisterCustomerPhoneNumber.setError(null);
                }

                btnRegisterCustomerRegister.setEnabled(checkCustomerRegister());
            }
        });

        //// U.O.F 파트너
        // 회원가입 - 아이디 입력란이 수정되었을 경우
        tilRegisterUofPartnerId.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterUofPartnerId.setErrorEnabled(true);
                    tilRegisterUofPartnerId.setError("아이디는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUofPartnerId.setErrorEnabled(true);
                    tilRegisterUofPartnerId.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilRegisterUofPartnerId.setErrorEnabled(false);
                    tilRegisterUofPartnerId.setError(null);
                }

                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 비밀번호 입력란이 수정되었을 경우
        tilRegisterUofPartnerPw.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterUofPartnerPw.setErrorEnabled(true);
                    tilRegisterUofPartnerPw.setError("비밀번호는 8자리 이상이어야 합니다");
                } else if (result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUofPartnerPw.setErrorEnabled(true);
                    tilRegisterUofPartnerPw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                } else {
                    tilRegisterUofPartnerPw.setErrorEnabled(false);
                    tilRegisterUofPartnerPw.setError(null);
                }

                if (!editable.toString().equals(tilRegisterUofPartnerPwChk.getEditText().getText().toString())) {
                    tilRegisterUofPartnerPw.setErrorEnabled(true);
                    tilRegisterUofPartnerPwChk.setError("비밀번호가 일치하지 않습니다");
                } else {
                    tilRegisterUofPartnerPw.setErrorEnabled(false);
                    tilRegisterUofPartnerPwChk.setError(null);
                }

                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 비밀번호 재확인 입력란이 수정되었을 경우
        tilRegisterUofPartnerPwChk.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilRegisterUofPartnerPw.getEditText().getText().toString())) {
                    tilRegisterUofPartnerPwChk.setErrorEnabled(true);
                    tilRegisterUofPartnerPwChk.setError("비밀번호가 일치하지 않습니다");
                    btnRegisterUofPartnerRegister.setEnabled(false);
                } else {
                    tilRegisterUofPartnerPwChk.setErrorEnabled(false);
                    tilRegisterUofPartnerPwChk.setError(null);
                    btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
                }
            }
        });


        // 회원가입 - 이름 입력란이 수정되었을 경우
        tilRegisterUofPartnerName.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterUofPartnerName.setErrorEnabled(true);
                    tilRegisterUofPartnerName.setError("이름은 한글만 가능합니다");
                } else {
                    tilRegisterUofPartnerName.setErrorEnabled(false);
                    tilRegisterUofPartnerName.setError(null);
                }

                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 전화번호 입력란이 수정되었을 경우
        tilRegisterUofPartnerPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterPhoneNumber(editable.toString());

                if (result == Constants.Pattern.LENGTH_SHORT || result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUofPartnerPhoneNumber.setErrorEnabled(true);
                    tilRegisterUofPartnerPhoneNumber.setError("전화번호 형식이 맞지 않습니다");
                } else {
                    tilRegisterUofPartnerPhoneNumber.setErrorEnabled(false);
                    tilRegisterUofPartnerPhoneNumber.setError(null);
                }

                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 회사명 입력란이 수정되었을 경우
        tilRegisterCompanyName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 사업자번호 입력란이 수정되었을 경우
        tilRegisterLicenseNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = checkRegisterLicenseNumber(editable.toString());

                if (result == Constants.Pattern.LENGTH_SHORT || result == Constants.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilRegisterLicenseNumber.setErrorEnabled(true);
                    tilRegisterLicenseNumber.setError("사업자번호는 10자리 숫자입니다");
                } else {
                    tilRegisterLicenseNumber.setErrorEnabled(false);
                    tilRegisterLicenseNumber.setError(null);
                }

                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회원가입 - 주소 입력란이 수정되었을 경우
        tilRegisterCompanyAddress.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
            }
        });

        // 회사 유형 Spinner에서 특정 item이 선택되었을 경우
        spRegisterCompanyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), spRegisterCompanyType.getItemAtPosition(position) + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        // 사업자등록증 이미지 불러오기 버튼이 눌렸을 경우
        btnRegisterLicenseImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        // 일반고객 회원가입 버튼이 눌렸을 경우
        btnRegisterCustomerRegister.setOnClickListener(view -> {
            // 회원가입 창일 경우
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.REGISTER_CUSTOMER);

                JSONObject message = new JSONObject();
                message.put("id", tilRegisterCustomerId.getEditText().getText().toString());
                message.put("pw", tilRegisterCustomerPw.getEditText().getText().toString());
                message.put("name", tilRegisterCustomerName.getEditText().getText().toString());
                message.put("phone", tilRegisterCustomerPhoneNumber.getEditText().getText().toString());

                sendData.putOpt("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String requestCode = recvData.getString("response_code");

                if (requestCode.equals(Constants.Network.Response.REGISTER_SUCCESS)) {
                    // 회원가입 성공 - 로그인창 표시
                    Toast.makeText(RegisterActivity.this, "가입되었습니다. 해당 계정으로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (requestCode.equals(Constants.Network.Response.REGISTER_FAILED_ID_DUPLICATE)) {
                    // 회원가입 실패 - 아이디 중복
                    tilRegisterCustomerId.setErrorEnabled(true);
                    tilRegisterCustomerId.setError("해당 아이디는 이미 사용중입니다");
                    tilRegisterCustomerId.getEditText().setFocusableInTouchMode(true);
                    tilRegisterCustomerId.getEditText().requestFocus();
                } else {
                    // 회원가입 실패 - 기타 오류
                    Toast.makeText(RegisterActivity.this, "회원가입 실패: " + recvData.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // U.O.F 파트너 회원가입 버튼이 눌렸을 경우
        btnRegisterUofPartnerRegister.setOnClickListener(view -> {
            // 회원가입 창일 경우
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.REGISTER_UOFPARTNER);

                JSONObject message = new JSONObject();
                message.put("id", tilRegisterUofPartnerId.getEditText().getText().toString());
                message.put("pw", tilRegisterUofPartnerPw.getEditText().getText().toString());
                message.put("name", tilRegisterUofPartnerName.getEditText().getText().toString());
                message.put("phone", tilRegisterUofPartnerPhoneNumber.getEditText().getText().toString());

                JSONObject company = new JSONObject();
                company.put("name", tilRegisterCompanyName.getEditText().getText().toString());
                company.put("license_num", tilRegisterCompanyName.getEditText().getText().toString());
                company.put("type", spRegisterCompanyType.getSelectedItem().toString());
                company.put("address", tilRegisterCompanyAddress.getEditText().getText().toString());
                company.put("license_img", convertImageToString(ivRegisterLicenseImage));

                message.putOpt("company", company);
                sendData.putOpt("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String requestCode = recvData.getString("response_code");

                if (requestCode.equals(Constants.Network.Response.REGISTER_SUCCESS)) {
                    // 회원가입 성공 - 로그인창 표시
                    Toast.makeText(RegisterActivity.this, "가입되었습니다. 해당 계정으로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (requestCode.equals(Constants.Network.Response.REGISTER_FAILED_ID_DUPLICATE)) {
                    // 회원가입 실패 - 아이디 중복
                    tilRegisterUofPartnerId.setErrorEnabled(true);
                    tilRegisterUofPartnerId.setError("해당 아이디는 이미 사용중입니다");
                    tilRegisterUofPartnerId.getEditText().setFocusableInTouchMode(true);
                    tilRegisterUofPartnerId.getEditText().requestFocus();
                } else {
                    // 회원가입 실패 - 기타 오류
                    Toast.makeText(RegisterActivity.this, "회원가입 실패: " + recvData.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //이미지 업로드
            Uri selectedImageUri = data.getData();
            ivRegisterLicenseImage.setImageURI(selectedImageUri);
            btnRegisterUofPartnerRegister.setEnabled(checkUofPartnerRegister());
        }
    }

    // 일반 고객 회원가입 시 아이디, 비밀번호, 이름, 전화번호 확인
    private boolean checkCustomerRegister() {
        return checkRegisterId(tilRegisterCustomerId.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPw(tilRegisterCustomerPw.getEditText().getText().toString()) == Constants.Pattern.OK
                && tilRegisterCustomerPw.getEditText().getText().toString().equals(tilRegisterCustomerPwChk.getEditText().getText().toString())
                && checkRegisterName(tilRegisterCustomerName.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPhoneNumber(tilRegisterCustomerPhoneNumber.getEditText().getText().toString()) == Constants.Pattern.OK;
    }

    // U.O.F 파트너 회원가입 시 아이디, 비밀번호, 이름 , 전화번호, 회사정보 입력 확인
    private boolean checkUofPartnerRegister() {
        return checkRegisterId(tilRegisterUofPartnerId.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPw(tilRegisterUofPartnerPw.getEditText().getText().toString()) == Constants.Pattern.OK
                && tilRegisterUofPartnerPw.getEditText().getText().toString().equals(tilRegisterUofPartnerPwChk.getEditText().getText().toString())
                && checkRegisterName(tilRegisterUofPartnerName.getEditText().getText().toString()) == Constants.Pattern.OK
                && checkRegisterPhoneNumber(tilRegisterUofPartnerPhoneNumber.getEditText().getText().toString()) == Constants.Pattern.OK
                && tilRegisterCompanyName.getEditText().getText().toString().length() > 0
                && checkRegisterLicenseNumber(tilRegisterLicenseNumber.getEditText().getText().toString()) == Constants.Pattern.OK
                && tilRegisterCompanyAddress.getEditText().getText().toString().length() > 0
                && checkRegisterLicenseImage(ivRegisterLicenseImage);
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

    // 회원가입 - 사업자번호 패턴 확인
    private int checkRegisterLicenseNumber(String licenseNumber) {
        if (licenseNumber.length() < 10) {
            return Constants.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", licenseNumber)) {
            return Constants.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Constants.Pattern.OK;
        }
    }

    // 회원가입 - 사업자등록증 첨부 확인
    private boolean checkRegisterLicenseImage(ImageView ivLicenseImage) {
        Drawable drawable = ivLicenseImage.getDrawable();
        boolean result = (drawable != null);

        if (result && (drawable instanceof BitmapDrawable)) {
            result = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return result;
    }

    // 이미지를 문자열로 변경
    private String convertImageToString(ImageView imageView) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
    }
}