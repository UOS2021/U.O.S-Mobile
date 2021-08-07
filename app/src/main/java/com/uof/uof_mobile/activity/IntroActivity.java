package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();
    }

    private void init() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);

        /* QR 실행 및 소켓 통신 테스트
        Intent socketTestActivity = new Intent(LoginActivity.this, SocketTestActivity.class);

        if(Intent.ACTION_VIEW.equals(getIntent().getAction())){
            Uri uri = getIntent().getData();

            if(uri != null){
                String targetIp = uri.getQueryParameter("targetIp");
                int targetPort = Integer.parseInt(uri.getQueryParameter("targetPort"));

                socketTestActivity.putExtra("targetIp", targetIp);
                socketTestActivity.putExtra("targetPort", targetPort);
            }
        }
        startActivity(socketTestActivity);
         */
    }
}