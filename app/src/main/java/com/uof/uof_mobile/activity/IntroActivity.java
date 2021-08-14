package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.net.Uri;
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
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();

            if (uri == null) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(IntroActivity.this, RestaurantOrderingActivity.class);
                intent.putExtra("targetIp", ((Uri) uri).getQueryParameter("targetIp"));
                intent.putExtra("targetPort", Integer.parseInt(uri.getQueryParameter("targetPort")));
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}