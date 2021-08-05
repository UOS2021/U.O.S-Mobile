package com.uof.uof_mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayoutCompat llregistercustomer;
    private LinearLayoutCompat llregisterpartner;
    private TextInputLayout tilregistercustomerid;
    private TextInputLayout tilregistercustomerpw;
    private TextInputLayout tilregistercustomerpwchk;
    private TextInputLayout tilregistercustomername;
    private TextInputLayout tilregistercustomerphone;
    private AppCompatButton btnregistercustomerregister;

    private TextInputLayout tilRegisterUofPartnerId;
    private TextInputLayout tilRegisterUofPartnerPw;
    private TextInputLayout tilRegisterUofPartnerPwChk;
    private TextInputLayout tilRegisterUofPartnerName;
    private TextInputLayout tilRegisterUofPartnerPhone;
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

        llregistercustomer = findViewById(R.id.ll_register_customer);
        llregisterpartner = findViewById(R.id.ll_register_uofpartner);

        tilregistercustomerid = findViewById(R.id.til_register_customerid);
        tilregistercustomerpw = findViewById(R.id.til_register_customerpw);
        tilregistercustomerpwchk = findViewById(R.id.til_register_customerpwchk);
        tilregistercustomername = findViewById(R.id.til_register_customername);
        tilregistercustomerphone = findViewById(R.id.til_register_customerphone);
        btnregistercustomerregister = findViewById(R.id.btn_register_customerregister);

        tilRegisterUofPartnerId = findViewById(R.id.til_register_uofpartnerid);
        tilRegisterUofPartnerPw = findViewById(R.id.til_register_uofpartnerpw);
        tilRegisterUofPartnerPwChk = findViewById(R.id.til_register_uofpartnerpwchk);
        tilRegisterUofPartnerName = findViewById(R.id.til_register_uofpartnername);
        tilRegisterUofPartnerPhone = findViewById(R.id.til_register_uofpartnerphone);
        tilRegisterCompanyName = findViewById(R.id.til_register_companyname);
        tilRegisterLicenseNumber = findViewById(R.id.til_register_licensenumber);
        btnRegisterLicenseImage = findViewById(R.id.btn_register_licenseimage);
        ivRegisterLicenseImage = findViewById(R.id.iv_register_licenseimage);
        btnRegisterUofPartnerRegister = findViewById(R.id.btn_register_uofpartnerregister);
        spRegisterCompanyType = findViewById(R.id.sp_register_companytype);
        tilRegisterCompanyAddress = findViewById(R.id.til_register_companyaddress);

        // 회원가입 유형 확인
        if (registerType == 0) {
            // 일반 고객
            llregistercustomer.setVisibility(View.VISIBLE);
            llregisterpartner.setVisibility(View.GONE);
        } else {
            // U.O.F 파트너
            llregisterpartner.setVisibility(View.VISIBLE);
            llregistercustomer.setVisibility(View.GONE);
        }

        spRegisterCompanyType.setPrompt("회사 유형");

        ArrayAdapter companyType = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_companytype, android.R.layout.simple_spinner_dropdown_item);
        companyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegisterCompanyType.setAdapter(companyType);

        spRegisterCompanyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), (String) spRegisterCompanyType.getItemAtPosition(position) + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        btnRegisterLicenseImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //이미지 업로드
            Uri selectedImageUri = data.getData();
            ivRegisterLicenseImage.setImageURI(selectedImageUri);
        }
    }
}