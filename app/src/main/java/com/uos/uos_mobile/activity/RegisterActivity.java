package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends UosActivity {
    private ImageButton ibtnRegisterClose;
    private LinearLayoutCompat llRegisterCustomer;
    private LinearLayoutCompat llRegisterUosPartner;
    private TextInputLayout tilRegisterCustomerId;
    private TextInputLayout tilRegisterCustomerPw;
    private TextInputLayout tilRegisterCustomerPwChk;
    private TextInputLayout tilRegisterCustomerName;
    private TextInputLayout tilRegisterCustomerPhoneNumber;
    private LinearLayoutCompat llRegisterCustomerRegister;

    private TextInputLayout tilRegisterUosPartnerId;
    private TextInputLayout tilRegisterUosPartnerPw;
    private TextInputLayout tilRegisterUosPartnerPwChk;
    private TextInputLayout tilRegisterUosPartnerName;
    private TextInputLayout tilRegisterUosPartnerPhoneNumber;
    private TextInputLayout tilRegisterCompanyName;
    private TextInputLayout tilRegisterLicenseNumber;
    private Spinner spRegisterCompanyType;
    private TextInputLayout tilRegisterCompanyAddress;
    private LinearLayoutCompat llRegisterUosPartnerRegister;
    private AppCompatImageView ivRegisterLicenseImage;
    private AppCompatTextView tvRegisterImageMessage;
    private ExtendedFloatingActionButton efRegisterGotoCompanyInfo;
    private ScrollView svRegisterScrollView;
    private TextView tvRegisterCompanyInfo;
    private LinearLayoutCompat llRegisterCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_register);

        init();
    }

    private void init() {
        //데이터 받아오기
        int registerType = getIntent().getExtras().getInt("RegisterType");

        ibtnRegisterClose = findViewById(com.uos.uos_mobile.R.id.ibtn_register_close);

        llRegisterCustomer = findViewById(com.uos.uos_mobile.R.id.ll_register_customer);
        llRegisterUosPartner = findViewById(com.uos.uos_mobile.R.id.ll_register_uospartner);

        tilRegisterCustomerId = findViewById(com.uos.uos_mobile.R.id.til_register_customerid);
        tilRegisterCustomerPw = findViewById(com.uos.uos_mobile.R.id.til_register_customerpw);
        tilRegisterCustomerPwChk = findViewById(com.uos.uos_mobile.R.id.til_register_customerpwchk);
        tilRegisterCustomerName = findViewById(com.uos.uos_mobile.R.id.til_register_customername);
        tilRegisterCustomerPhoneNumber = findViewById(com.uos.uos_mobile.R.id.til_register_customerphonenumber);
        llRegisterCustomerRegister = findViewById(com.uos.uos_mobile.R.id.ll_register_customerregister);

        tilRegisterUosPartnerId = findViewById(com.uos.uos_mobile.R.id.til_register_uospartnerid);
        tilRegisterUosPartnerPw = findViewById(com.uos.uos_mobile.R.id.til_register_uospartnerpw);
        tilRegisterUosPartnerPwChk = findViewById(com.uos.uos_mobile.R.id.til_register_uospartnerpwchk);
        tilRegisterUosPartnerName = findViewById(com.uos.uos_mobile.R.id.til_register_uospartnername);
        tilRegisterUosPartnerPhoneNumber = findViewById(com.uos.uos_mobile.R.id.til_register_uospartnerphonenumber);
        tilRegisterCompanyName = findViewById(com.uos.uos_mobile.R.id.til_register_companyname);
        tilRegisterLicenseNumber = findViewById(com.uos.uos_mobile.R.id.til_register_licensenumber);
        ivRegisterLicenseImage = findViewById(com.uos.uos_mobile.R.id.iv_register_licenseimage);
        tvRegisterImageMessage = findViewById(com.uos.uos_mobile.R.id.tv_register_imagemessage);
        llRegisterUosPartnerRegister = findViewById(com.uos.uos_mobile.R.id.ll_register_uospartnerregister);
        spRegisterCompanyType = findViewById(com.uos.uos_mobile.R.id.sp_register_companytype);
        tilRegisterCompanyAddress = findViewById(com.uos.uos_mobile.R.id.til_register_companyaddress);

        efRegisterGotoCompanyInfo = findViewById(com.uos.uos_mobile.R.id.ef_register_gotocompanyinfo);
        svRegisterScrollView = findViewById(com.uos.uos_mobile.R.id.sv_register_scrollview);
        tvRegisterCompanyInfo = findViewById(com.uos.uos_mobile.R.id.tv_register_companyinfo);
        llRegisterCompany = findViewById(com.uos.uos_mobile.R.id.ll_register_company);

        /* 회원가입 유형 확인 */
        if (registerType == 0) {

            /* 일반고객 회원가입일 경우*/

            llRegisterCustomer.setVisibility(View.VISIBLE);
            llRegisterUosPartner.setVisibility(View.GONE);
            efRegisterGotoCompanyInfo.setVisibility(View.GONE);
        } else {

            /* U.O.S 파트너 회원가입일 경우*/

            llRegisterUosPartner.setVisibility(View.VISIBLE);
            llRegisterCustomer.setVisibility(View.GONE);
            efRegisterGotoCompanyInfo.setVisibility(View.VISIBLE);
            llRegisterCompany.setVisibility(View.GONE);
        }

        spRegisterCompanyType.setPrompt("회사 유형");
        ArrayAdapter companyType = ArrayAdapter.createFromResource(getApplicationContext(), com.uos.uos_mobile.R.array.array_companytype, android.R.layout.simple_spinner_dropdown_item);
        companyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegisterCompanyType.setAdapter(companyType);

        llRegisterCustomerRegister.setEnabled(false);
        llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
        llRegisterUosPartnerRegister.setEnabled(false);
        llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));

        ibtnRegisterClose.setOnClickListener(view -> {
            finish();
        });

        /* 일반 고객
         * 회원가입 - 아이디 입력란이 수정되었을 경우
         */
        tilRegisterCustomerId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkId(editable.toString());

                if (result == PatternManager.LENGTH_SHORT) {
                    tilRegisterCustomerId.setError("아이디는 8자리 이상이어야 합니다");
                    tilRegisterCustomerId.setErrorEnabled(true);
                } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerId.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilRegisterCustomerId.setErrorEnabled(true);
                } else {
                    tilRegisterCustomerId.setError(null);
                    tilRegisterCustomerId.setErrorEnabled(false);
                }

                llRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                if (checkCustomerRegister()) {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 비밀번호 입력란이 수정되었을 경우 */
        tilRegisterCustomerPw.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterCustomerPw.setError("비밀번호는 8자리 이상이어야 합니다");
                    tilRegisterCustomerPw.setErrorEnabled(true);
                } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerPw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilRegisterCustomerPw.setErrorEnabled(true);
                } else {
                    tilRegisterCustomerPw.setError(null);
                    tilRegisterCustomerPw.setErrorEnabled(false);
                }

                if (!editable.toString().equals(tilRegisterCustomerPwChk.getEditText().getText().toString())) {
                    tilRegisterCustomerPwChk.setError("비밀번호가 일치하지 않습니다");
                    tilRegisterUosPartnerPwChk.setErrorEnabled(true);
                } else {
                    tilRegisterCustomerPwChk.setError(null);
                    tilRegisterUosPartnerPwChk.setErrorEnabled(false);
                }

                llRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                if (checkCustomerRegister()) {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 비밀번호 재확인 입력란이 수정되었을 경우 */
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
                    tilRegisterCustomerPwChk.setError("비밀번호가 일치하지 않습니다");
                    tilRegisterCustomerPwChk.setErrorEnabled(true);
                    llRegisterCustomerRegister.setEnabled(false);
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                } else {
                    tilRegisterCustomerPwChk.setError(null);
                    tilRegisterCustomerPwChk.setErrorEnabled(false);
                    llRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                    if (checkCustomerRegister()) {
                        llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    } else {
                        llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                    }
                }
            }
        });


        /* 회원가입 - 이름 입력란이 수정되었을 경우 */
        tilRegisterCustomerName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkName(editable.toString());

                if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterCustomerName.setError("이름은 한글만 가능합니다");
                    tilRegisterCustomerName.setErrorEnabled(true);
                } else {
                    tilRegisterCustomerName.setError(null);
                    tilRegisterCustomerName.setErrorEnabled(false);
                }

                llRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                if (checkCustomerRegister()) {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 전화번호 입력란이 수정되었을 경우 */
        tilRegisterCustomerPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterCustomerPhoneNumber.setError("전화번호 형식이 맞지 않습니다");
                    tilRegisterCustomerPhoneNumber.setErrorEnabled(true);
                } else {
                    tilRegisterCustomerPhoneNumber.setError(null);
                    tilRegisterCustomerPhoneNumber.setErrorEnabled(false);
                }

                llRegisterCustomerRegister.setEnabled(checkCustomerRegister());
                if (checkCustomerRegister()) {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterCustomerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* U.O.S 파트너 회원가입
         * 회원가입 - 아이디 입력란이 수정되었을 경우
         */
        tilRegisterUosPartnerId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkId(editable.toString());

                if (result == PatternManager.LENGTH_SHORT) {
                    tilRegisterUosPartnerId.setError("아이디는 8자리 이상이어야 합니다");
                    tilRegisterUosPartnerId.setErrorEnabled(true);
                } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUosPartnerId.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilRegisterUosPartnerId.setErrorEnabled(true);
                } else {
                    tilRegisterUosPartnerId.setError(null);
                    tilRegisterUosPartnerId.setErrorEnabled(false);
                }

                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 비밀번호 입력란이 수정되었을 경우 */
        tilRegisterUosPartnerPw.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterUosPartnerPw.setError("비밀번호는 8자리 이상이어야 합니다");
                    tilRegisterUosPartnerPw.setErrorEnabled(true);
                } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUosPartnerPw.setError("알파벳, 숫자, !@#*만 사용할 수 있습니다");
                    tilRegisterUosPartnerPw.setErrorEnabled(true);
                } else {
                    tilRegisterUosPartnerPw.setError(null);
                    tilRegisterUosPartnerPw.setErrorEnabled(false);
                }

                if (!editable.toString().equals(tilRegisterUosPartnerPwChk.getEditText().getText().toString())) {
                    tilRegisterUosPartnerPwChk.setError("비밀번호가 일치하지 않습니다");
                    tilRegisterUosPartnerPwChk.setErrorEnabled(true);
                } else {
                    tilRegisterUosPartnerPwChk.setError(null);
                    tilRegisterUosPartnerPwChk.setErrorEnabled(false);
                }

                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 비밀번호 재확인 입력란이 수정되었을 경우 */
        tilRegisterUosPartnerPwChk.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tilRegisterUosPartnerPw.getEditText().getText().toString())) {
                    tilRegisterUosPartnerPwChk.setError("비밀번호가 일치하지 않습니다");
                    tilRegisterUosPartnerPwChk.setErrorEnabled(true);
                    llRegisterUosPartnerRegister.setEnabled(false);
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                } else {
                    tilRegisterUosPartnerPwChk.setError(null);
                    tilRegisterUosPartnerPwChk.setErrorEnabled(false);
                    llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                    if (checkUosPartnerRegister()) {
                        llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    } else {
                        llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                    }
                }
            }
        });


        /* 회원가입 - 이름 입력란이 수정되었을 경우 */
        tilRegisterUosPartnerName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkName(editable.toString());

                if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterUosPartnerName.setError("이름은 한글만 가능합니다");
                    tilRegisterUosPartnerName.setErrorEnabled(true);
                } else {
                    tilRegisterUosPartnerName.setError(null);
                    tilRegisterUosPartnerName.setErrorEnabled(false);
                }

                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 전화번호 입력란이 수정되었을 경우 */
        tilRegisterUosPartnerPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
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
                    tilRegisterUosPartnerPhoneNumber.setError("전화번호 형식이 맞지 않습니다");
                    tilRegisterUosPartnerPhoneNumber.setErrorEnabled(true);
                } else {
                    tilRegisterUosPartnerPhoneNumber.setError(null);
                    tilRegisterUosPartnerPhoneNumber.setErrorEnabled(false);
                }

                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 회사명 입력란이 수정되었을 경우 */
        tilRegisterCompanyName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 사업자번호 입력란이 수정되었을 경우 */
        tilRegisterLicenseNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkLicenseNumber(editable.toString());

                if (result == PatternManager.LENGTH_SHORT || result == PatternManager.NOT_ALLOWED_CHARACTER) {
                    tilRegisterLicenseNumber.setError("사업자번호는 10자리 숫자입니다");
                    tilRegisterLicenseNumber.setErrorEnabled(true);
                } else {
                    tilRegisterLicenseNumber.setError(null);
                    tilRegisterLicenseNumber.setErrorEnabled(false);
                }

                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회원가입 - 주소 입력란이 수정되었을 경우 */
        tilRegisterCompanyAddress.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
                if (checkUosPartnerRegister()) {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                } else {
                    llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                }
            }
        });

        /* 회사 유형 Spinner에서 특정 item이 선택되었을 경우 */
        spRegisterCompanyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* 사업자등록증 이미지 불러오기 버튼이 눌렸을 경우 */
        ivRegisterLicenseImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        /* 일반고객 회원가입 버튼이 눌렸을 경우 */
        llRegisterCustomerRegister.setOnClickListener(view -> {
            try {
                JSONObject message = new JSONObject();
                message.accumulate("customer_id", tilRegisterCustomerId.getEditText().getText().toString());
                message.accumulate("pw", tilRegisterCustomerPw.getEditText().getText().toString());
                message.accumulate("name", tilRegisterCustomerName.getEditText().getText().toString());
                message.accumulate("phone", tilRegisterCustomerPhoneNumber.getEditText().getText().toString());

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.REGISTER_CUSTOMER);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.REGISTER_SUCCESS)) {

                    /* 회원가입 성공 - 로그인창 표시 */

                    Toast.makeText(RegisterActivity.this, "가입되었습니다. 해당 계정으로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (responseCode.equals(Global.Network.Response.REGISTER_FAIL_ID_DUPLICATE)) {

                    /* 회원가입 실패 - 아이디 중복 */

                    tilRegisterCustomerId.setError("해당 아이디는 이미 사용중입니다");
                    tilRegisterCustomerId.setErrorEnabled(true);
                    tilRegisterCustomerId.getEditText().setFocusableInTouchMode(true);
                    tilRegisterCustomerId.getEditText().requestFocus();
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                    /* 서버 연결 실패 */

                    Toast.makeText(RegisterActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                } else {

                    /* 회원가입 실패 - 기타 오류 */

                    Toast.makeText(RegisterActivity.this, "회원가입 실패: " + recvData.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "회원가입 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        /* U.O.S 파트너 회사 정보 입력 버튼이 눌렀을 경우 */
        efRegisterGotoCompanyInfo.setOnClickListener(view -> {
            llRegisterCompany.setVisibility(View.VISIBLE);
            svRegisterScrollView.smoothScrollTo(0, llRegisterCompany.getTop());
            efRegisterGotoCompanyInfo.setVisibility(View.GONE);

        });

        /* U.O.S 파트너 회원가입 버튼이 눌렸을 경우 */
        llRegisterUosPartnerRegister.setOnClickListener(view -> {
            try {
                JSONObject company = new JSONObject();
                company.accumulate("name", tilRegisterCompanyName.getEditText().getText().toString());
                company.accumulate("license_num", tilRegisterCompanyName.getEditText().getText().toString());
                company.accumulate("type", spRegisterCompanyType.getSelectedItem().toString());
                company.accumulate("address", tilRegisterCompanyAddress.getEditText().getText().toString());
                company.accumulate("license_img", UsefulFuncManager.convertBitmapToString(((BitmapDrawable) ivRegisterLicenseImage.getDrawable()).getBitmap()));

                JSONObject message = new JSONObject();
                message.accumulate("uospartner_id", tilRegisterUosPartnerId.getEditText().getText().toString());
                message.accumulate("pw", tilRegisterUosPartnerPw.getEditText().getText().toString());
                message.accumulate("name", tilRegisterUosPartnerName.getEditText().getText().toString());
                message.accumulate("phone", tilRegisterUosPartnerPhoneNumber.getEditText().getText().toString());
                message.accumulate("company", company);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.REGISTER_UOSPARTNER);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.REGISTER_SUCCESS)) {

                    /* 회원가입 성공 - 로그인창 표시 */

                    Toast.makeText(RegisterActivity.this, "가입되었습니다. 해당 계정으로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (responseCode.equals(Global.Network.Response.REGISTER_FAIL_ID_DUPLICATE)) {

                    /* 회원가입 실패 - 아이디 중복 */

                    tilRegisterUosPartnerId.setError("해당 아이디는 이미 사용중입니다");
                    tilRegisterUosPartnerId.setErrorEnabled(true);
                    tilRegisterUosPartnerId.getEditText().setFocusableInTouchMode(true);
                    tilRegisterUosPartnerId.getEditText().requestFocus();
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                    /* 서버 연결 실패 */

                    Toast.makeText(RegisterActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                } else {

                    /* 회원가입 실패 - 기타 오류 */

                    Toast.makeText(RegisterActivity.this, "회원가입 실패: " + recvData.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "회원가입 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            /* 이미지 업로드 */

            Uri selectedImageUri = data.getData();
            ivRegisterLicenseImage.setImageURI(selectedImageUri);
            llRegisterUosPartnerRegister.setEnabled(checkUosPartnerRegister());
            if (checkUosPartnerRegister()) {
                llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            } else {
                llRegisterUosPartnerRegister.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            }
        }

        if (checkRegisterLicenseImage(ivRegisterLicenseImage)) {

            /* 사업자등록증 이미지가 선택되어있을 경우 */

            tvRegisterImageMessage.setVisibility(View.GONE);
        } else {

            /* 사업자등록증 이미지가 선택되어있지 않을 경우 */

            tvRegisterImageMessage.setVisibility(View.VISIBLE);
        }
    }

    /* 일반 고객 회원가입 시 아이디, 비밀번호, 이름, 전화번호 확인 */
    private boolean checkCustomerRegister() {
        return PatternManager.checkId(tilRegisterCustomerId.getEditText().getText().toString()) == PatternManager.OK
                && PatternManager.checkPw(tilRegisterCustomerPw.getEditText().getText().toString()) == PatternManager.OK
                && tilRegisterCustomerPw.getEditText().getText().toString().equals(tilRegisterCustomerPwChk.getEditText().getText().toString())
                && PatternManager.checkName(tilRegisterCustomerName.getEditText().getText().toString()) == PatternManager.OK
                && PatternManager.checkPhoneNumber(tilRegisterCustomerPhoneNumber.getEditText().getText().toString()) == PatternManager.OK;
    }

    /* U.O.S 파트너 회원가입 시 아이디, 비밀번호, 이름 , 전화번호, 회사정보 입력 확인 */
    private boolean checkUosPartnerRegister() {
        return PatternManager.checkId(tilRegisterUosPartnerId.getEditText().getText().toString()) == PatternManager.OK
                && PatternManager.checkPw(tilRegisterUosPartnerPw.getEditText().getText().toString()) == PatternManager.OK
                && tilRegisterUosPartnerPw.getEditText().getText().toString().equals(tilRegisterUosPartnerPwChk.getEditText().getText().toString())
                && PatternManager.checkName(tilRegisterUosPartnerName.getEditText().getText().toString()) == PatternManager.OK
                && PatternManager.checkPhoneNumber(tilRegisterUosPartnerPhoneNumber.getEditText().getText().toString()) == PatternManager.OK
                && tilRegisterCompanyName.getEditText().getText().toString().length() > 0
                && PatternManager.checkLicenseNumber(tilRegisterLicenseNumber.getEditText().getText().toString()) == PatternManager.OK
                && tilRegisterCompanyAddress.getEditText().getText().toString().length() > 0
                && checkRegisterLicenseImage(ivRegisterLicenseImage);
    }

    /* 회원가입 - 사업자등록증 첨부 확인 */
    private boolean checkRegisterLicenseImage(ImageView ivLicenseImage) {
        Drawable drawable = ivLicenseImage.getDrawable();
        boolean result = (drawable != null);

        if (result && (drawable instanceof BitmapDrawable)) {
            result = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return result;
    }
}