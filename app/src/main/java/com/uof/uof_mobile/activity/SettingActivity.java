package com.uof.uof_mobile.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.Global;
import com.uof.uof_mobile.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.add(this);
        super.onDestroy();
    }

    private void init(){
        Global.activities.add(this);
    }
}