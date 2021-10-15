package com.uos.uos_mobile.activity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;

public class SettingActivity extends UosActivity {
    private AppCompatImageButton ibtnSettingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_setting);

        init();
    }

    private void init() {
        ibtnSettingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_setting_back);

        /* 뒤로가기 버튼이 눌렸을 때 */
        ibtnSettingBack.setOnClickListener(view -> {
            finish();
        });
    }
}