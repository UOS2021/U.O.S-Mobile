package com.uof.uof_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SharedPreferenceManager;
import com.uof.uof_mobile.other.Global;

import java.util.Timer;
import java.util.TimerTask;

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
        for (Activity activity : Global.activities) {
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
            SharedPreferenceManager.save(Global.SharedPreference.SP_KEY_LAST_NOTIFICATION_NUMBER, 0);
        }

        // FCM 앱 고유 토큰 부여
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("UOF_MOBILE_FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String fcmToken = task.getResult();
                    Log.d("UOF_MOBILE_FCM", fcmToken);
                    Global.Firebase.FCM_TOKEN = fcmToken;
                });

        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();

            if (uri != null) {
                try {
                    intent.putExtra("targetIp", uri.getQueryParameter("targetIp"));
                    intent.putExtra("targetPort", uri.getQueryParameter("targetPort"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(IntroActivity.this, "등록되지 않은 매장입니다", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if (getIntent().getDataString() != null) {
                intent.putExtra("orderNumber", getIntent().getDataString());
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}