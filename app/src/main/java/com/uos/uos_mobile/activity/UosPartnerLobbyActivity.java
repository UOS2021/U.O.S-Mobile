package com.uos.uos_mobile.activity;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.uos.uos_mobile.dialog.ShowQRDialog;
import com.uos.uos_mobile.other.Global;

public class UosPartnerLobbyActivity extends UosActivity {
    private AppCompatTextView tvOwnerLobbyOwnerName;
    private AppCompatButton btnOwnerLobbyDisplayQr;
    private AppCompatImageButton btnOwnerLobbySetting;

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_ownerlobby);

        btnOwnerLobbyDisplayQr = findViewById(com.uos.uos_mobile.R.id.btn_ownerlobby_displayqr);
        btnOwnerLobbySetting = findViewById(com.uos.uos_mobile.R.id.ibtn_ownerlobby_setting);
        tvOwnerLobbyOwnerName = findViewById(com.uos.uos_mobile.R.id.tv_ownerlobby_companyname);

        /* 매장명 불러오는 부분 */
        tvOwnerLobbyOwnerName.setText(Global.User.companyName);

        /* QR 보여주기 버튼 클릭 시 */
        btnOwnerLobbyDisplayQr.setOnClickListener(view -> {
            new ShowQRDialog(UosPartnerLobbyActivity.this, false, true).show();
        });

        /* 설정 버튼 클릭 시 */
        btnOwnerLobbySetting.setOnClickListener(view -> {
            Intent intent = new Intent(UosPartnerLobbyActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }
}