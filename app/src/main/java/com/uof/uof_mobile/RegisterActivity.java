package com.uof.uof_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayoutCompat llregistercustomer;
    private LinearLayoutCompat llregisterpartner;
    private TextInputLayout tilregistercustomerid;
    private TextInputLayout tilregistercustomerpw;
    private TextInputLayout tilregistercustomerpwchk;
    private TextInputLayout tilregistercustomername;
    private TextInputLayout tilregistercustomerphone;
    private Button btnregistercustomerregister;

    private TextInputLayout tilregisterpartnerid;
    private TextInputLayout tilregisterpartnerpw;
    private TextInputLayout tilregisterpartnerpwchk;
    private TextInputLayout tilregisterpartnername;
    private TextInputLayout tilregisterpartnerphone;
    private TextInputLayout tilregistercompanyname;
    private TextInputLayout tilregistercompanynumber;
    private Spinner spregistertype;
    private Spinner spregisterlocal;
    private Button btnregisterpartnerregister;
    private ImageView imgregistercompanytimageview;
    private Button btnregistercompanyimageg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }
    private void init() {


        //데이터 받아오기
        Intent intent = getIntent(); /*데이터 수신*/
        tilregistercustomerid = findViewById(R.id.til_register_customerid);
        tilregistercustomerpw = findViewById(R.id.til_register_customerpw);
        tilregistercustomerpwchk = findViewById(R.id.til_register_customerpwchk);
        tilregistercustomername = findViewById(R.id.til_register_customername);
        tilregistercustomerphone = findViewById(R.id.til_register_customerphone);
        btnregistercustomerregister = findViewById(R.id.btn_register_customerregister);

        tilregisterpartnerid = findViewById(R.id.til_register_partnerid);
        tilregisterpartnerpw = findViewById(R.id.til_register_partnerpw);
        tilregisterpartnerpwchk = findViewById(R.id.til_register_partnerpwchk);
        tilregisterpartnername = findViewById(R.id.til_register_partnername);
        tilregisterpartnerphone = findViewById(R.id.til_register_partnerphone);
        tilregistercompanyname = findViewById(R.id.til_register_companyname);
        tilregistercompanynumber = findViewById(R.id.til_register_companynumber);
        btnregisterpartnerregister = findViewById(R.id.btn_register_partnerregister);
        llregistercustomer = findViewById(R.id.ll_register_customer);
        llregisterpartner = findViewById(R.id.ll_register_partner);
        btnregistercompanyimageg = (Button)findViewById(R.id.btn_register_companyimage);
        imgregistercompanytimageview = (ImageView)findViewById(R.id.img_register_companyimageview);
        
        //데이터 받기
        int str1 = intent.getExtras().getInt("RegisterType");
        if(str1==1){
            llregistercustomer.setVisibility(View.VISIBLE);
            llregisterpartner.setVisibility(View.GONE);
        }
        else{
            llregisterpartner.setVisibility(View.VISIBLE);
            llregistercustomer.setVisibility(View.GONE);
        }

        //스피너
        spregistertype = findViewById(R.id.sp_register_type);
        spregistertype.setPrompt("회사 유형");

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.companytype_array, android.R.layout.simple_spinner_dropdown_item);
        //R.array.test는 저희가 정의해놓은 1월~12월 / android.R.layout.simple_spinner_dropdown_item은 기본으로 제공해주는 형식입니다.
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spregistertype.setAdapter(monthAdapter); //어댑터에 연결해줍니다.


        spregistertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),(String)spregistertype.getItemAtPosition(position)+"이 선택되었습니다.",Toast.LENGTH_SHORT).show();
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        spregisterlocal = findViewById(R.id.sp_register_local);
        spregisterlocal.setPrompt("지역");

        ArrayAdapter monthAdapter2 = ArrayAdapter.createFromResource(this, R.array.local_array, android.R.layout.simple_spinner_dropdown_item);
        //R.array.test는 저희가 정의해놓은 1월~12월 / android.R.layout.simple_spinner_dropdown_item은 기본으로 제공해주는 형식입니다.
        monthAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spregisterlocal.setAdapter(monthAdapter2); //어댑터에 연결해줍니다.


        spregisterlocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),(String)spregisterlocal.getItemAtPosition(position)+"이 선택되었습니다.",Toast.LENGTH_SHORT).show();
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        btnregistercompanyimageg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    //이미지 업로드
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imgregistercompanytimageview.setImageURI(selectedImageUri);
        }
    }
}