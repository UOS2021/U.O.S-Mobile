package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.LobbyListViewItemAdapter;
import com.uof.uof_mobile.dialog.ChangePhoneDialog;
import com.uof.uof_mobile.dialog.CheckPwDialog;
import com.uof.uof_mobile.dialog.ShowQRDialog;
import com.uof.uof_mobile.listitem.OrderingSetItem;
import com.uof.uof_mobile.other.Global;

import org.json.JSONArray;

public class OwnerLobbyActivity extends AppCompatActivity {
    TextView tvOwnerLobbyOwnerName;
    Button btn_displayqr,btn_getqr;
    ImageButton btn_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerlobby);

        init();
    }
    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);

        btn_displayqr = findViewById(R.id.btn_ownerlobby_displayqr);
        btn_getqr = findViewById(R.id.btn_ownerlobby_getqr);
        btn_setting = findViewById(R.id.ibtn_ownerlobby_setting);
        tvOwnerLobbyOwnerName = findViewById(R.id.tv_ownerlobby_ownername);

        btn_displayqr.setOnClickListener(view -> {
            new ShowQRDialog(OwnerLobbyActivity.this, false, true).show();
        });
        
        btn_setting.setOnClickListener(view -> {
            new CheckPwDialog(OwnerLobbyActivity.this, true, true).show();
        });
        
        //매장 명 불러오는 부분
        tvOwnerLobbyOwnerName.setText("버거킹");
    }
}