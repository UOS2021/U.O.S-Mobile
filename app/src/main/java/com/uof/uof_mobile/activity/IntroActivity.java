package com.uof.uof_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SharedPreferenceManager;
import com.uof.uof_mobile.other.Global;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        for(Activity activity : Global.activities){
            activity.finish();
        }

        Global.activities.add(this);

        SharedPreferenceManager.open(IntroActivity.this, Global.SharedPreference.APP_DATA);
        if (SharedPreferenceManager.load(Global.SharedPreference.IS_FIRST, true) == true) {
            SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, false);
            SharedPreferenceManager.save(Global.SharedPreference.USER_ID, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_PW, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, "");
            SharedPreferenceManager.save(Global.SharedPreference.IS_FIRST, false);
        }

        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();

            if (uri != null) {
                try {
                    intent.putExtra("targetIp", uri.getQueryParameter("targetIp"));
                    intent.putExtra("targetPort", uri.getQueryParameter("targetPort"));
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(IntroActivity.this, "등록되지 않은 매장입니다", Toast.LENGTH_SHORT).show();
               }
            }
        }
        startActivity(intent);
        finish();
    }
}