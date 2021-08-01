package com.uof.uof_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyActivity extends AppCompatActivity {
    Button btn_card, btn_orderlist, btn_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btn_card = findViewById(R.id.btn_lobby_card);
        btn_orderlist = findViewById(R.id.btn_lobby_orderlist);
        btn_setting = findViewById(R.id.btn_lobby_setting);

        btn_card.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, PayActivity.class);
            startActivity(intent);
        });

        btn_orderlist.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        btn_setting.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }

}