package com.uos.uos_mobile.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;


import com.uos.uos_mobile.other.Global;

public class SettingActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnSettingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_setting);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.removeActivity(this);
        super.onDestroy();
    }

    private void init() {
        Global.addActivity(this, false);

        ibtnSettingBack = findViewById(com.uos.uos_mobile.R.id.ibtn_setting_back);

        // 뒤로가기 버튼이 눌렸을 때
        ibtnSettingBack.setOnClickListener(view -> {
            finish();
        });
    }
}