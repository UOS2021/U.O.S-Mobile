package com.uos.uos_mobile.activity;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.uos.uos_mobile.dialog.ShowQRDialog;
import com.uos.uos_mobile.other.Global;

/**
 * UOS파트너의 메인화면을 담당하는 Activity.<br>
 * xml: activity_uospartnerlobby.xml<br><br>
 *
 * 화면 중앙에 자신의 매장정보를 불러올 수 있는 QR코드 전시버튼이 있으며 우측 하단에는 계정관련 설정을 할 수 있는
 * 설정버튼이 있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class UosPartnerLobbyActivity extends UosActivity {

    /**
     * UOS파트너의 회사명(매장명)이 표시되는 AppCompatTextView.
     */
    private AppCompatTextView tvUosPartnerLobbyCompanyName;

    /**
     * 매장의 주문목록을 불러올 수 있는 QR코드를 표시하는 AppCompatButton.
     */
    private AppCompatButton btnUosPartnerLobbyDisplayQr;

    /**
     * 설정 Activity로 넘어가는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnUosPartnerLobbySetting;

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_uospartnerlobby);

        btnUosPartnerLobbyDisplayQr = findViewById(com.uos.uos_mobile.R.id.btn_uospartnerlobby_displayqr);
        ibtnUosPartnerLobbySetting = findViewById(com.uos.uos_mobile.R.id.ibtn_uospartnerlobby_setting);
        tvUosPartnerLobbyCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_uospartnerlobby_companyname);

        /* 매장명 불러오는 부분 */
        tvUosPartnerLobbyCompanyName.setText(Global.User.companyName);

        /* QR 보여주기 버튼 클릭 시 */
        btnUosPartnerLobbyDisplayQr.setOnClickListener(view -> {
            new ShowQRDialog(UosPartnerLobbyActivity.this, false, true).show();
        });

        /* 설정 버튼 클릭 시 */
        ibtnUosPartnerLobbySetting.setOnClickListener(view -> {
            Intent intent = new Intent(UosPartnerLobbyActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }
}