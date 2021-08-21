package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.other.Global;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SharedPreferenceManager;

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
        startActivity(intent);
        finish();

//        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
//            Uri uri = getIntent().getData();
//
//            if (uri == null) {
//                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(IntroActivity.this, OrderingActivity.class);
//                intent.putExtra("targetIp", ((Uri) uri).getQueryParameter("targetIp"));
//                intent.putExtra("targetPort", Integer.parseInt(uri.getQueryParameter("targetPort")));
//                startActivity(intent);
//            }
//        } else {
//            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
//            startActivity(intent);
//        }
    }
}