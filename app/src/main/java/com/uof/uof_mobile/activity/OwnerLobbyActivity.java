package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.CheckPwDialog;
import com.uof.uof_mobile.dialog.ShowQRDialog;
import com.uof.uof_mobile.other.Global;

public class OwnerLobbyActivity extends AppCompatActivity {
    private AppCompatTextView tvOwnerLobbyOwnerName;
    private AppCompatButton btnOwnerLobbyDisplayQr;
    private AppCompatButton btnOwnerLobbyLoadQr;
    private AppCompatImageButton btnOwnerLobbySetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerlobby);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);

        btnOwnerLobbyDisplayQr = findViewById(R.id.btn_ownerlobby_displayqr);
        btnOwnerLobbyLoadQr = findViewById(R.id.btn_ownerlobby_loadqr);
        btnOwnerLobbySetting = findViewById(R.id.ibtn_ownerlobby_setting);
        tvOwnerLobbyOwnerName = findViewById(R.id.tv_ownerlobby_companyname);

        //매장 명 불러오는 부분
        tvOwnerLobbyOwnerName.setText("버거킹");

        // QR 보여주기 버튼 클릭 시
        btnOwnerLobbyDisplayQr.setOnClickListener(view -> {
            new ShowQRDialog(OwnerLobbyActivity.this, false, true).show();
        });

        // QR 불러오기 버튼 클릭 시
        btnOwnerLobbyLoadQr.setOnClickListener(view -> {
            Intent intent = new Intent(OwnerLobbyActivity.this, QRRecognitionActivity.class);
            startActivity(intent);
        });

        // 설정 버튼 클릭 시
        btnOwnerLobbySetting.setOnClickListener(view -> {
            new CheckPwDialog(OwnerLobbyActivity.this, true, true).show();
        });


    }
}