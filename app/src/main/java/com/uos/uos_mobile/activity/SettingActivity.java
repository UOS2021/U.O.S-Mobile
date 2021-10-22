package com.uos.uos_mobile.activity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;

public class SettingActivity extends UosActivity {
    private AppCompatImageButton ibtnSettingBack;

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_setting);

        ibtnSettingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_setting_back);

        /* 뒤로가기 버튼이 눌렸을 때 */
        ibtnSettingBack.setOnClickListener(view -> {
            finish();
        });
    }
}