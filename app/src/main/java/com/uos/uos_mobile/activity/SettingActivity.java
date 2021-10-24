package com.uos.uos_mobile.activity;

import androidx.appcompat.widget.AppCompatImageButton;

/**
 * 설정관련 메뉴를 보여주는 Activity.<br>
 * xml: activity_setting.xml<br><br>
 *
 * 실행 시 SettingPreferenceFragment를 표시하는 Activity입니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class SettingActivity extends UosActivity {

    /**
     * 설정 Activity를 종료하는 AppCompatImageButton.
     */
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