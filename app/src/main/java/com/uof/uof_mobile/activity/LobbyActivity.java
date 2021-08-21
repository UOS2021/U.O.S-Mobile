package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.other.Global;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.CheckPwDialog;
import com.uof.uof_mobile.listitem.OrderingSetItem;
import com.uof.uof_mobile.adapter.LobbyListViewItemAdapter;

import org.json.JSONArray;

public class LobbyActivity extends AppCompatActivity {
    ImageButton btn_card, btn_orderlist, btn_setting;
    ImageView ivLobbyRecognizeQR;
    ListView lvLobbyNowOrderList;
    LobbyListViewItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init(){
        Global.activities.add(this);

        btn_card = findViewById(R.id.btn_lobby_card);
        btn_orderlist = findViewById(R.id.btn_lobby_orderlist);
        btn_setting = findViewById(R.id.btn_lobby_setting);
        ivLobbyRecognizeQR = findViewById(R.id.iv_lobby_recognizeqr);
        lvLobbyNowOrderList = findViewById(R.id.lv_lobby_noworderlist);

        btn_card.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, CardActivity.class);
            startActivity(intent);
        });

        btn_orderlist.setOnClickListener(view -> {
//            Intent intent = new Intent(LobbyActivity.this, OrderListActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(LobbyActivity.this, OrderingActivity.class);
            startActivity(intent);
        });

        btn_setting.setOnClickListener(view -> {
            new CheckPwDialog(LobbyActivity.this, true, true).show();
        });

        ivLobbyRecognizeQR.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, QRRecognitionActivity.class);
            startActivity(intent);
        });

        try {
            adapter = new LobbyListViewItemAdapter();
            JSONArray menulist = new JSONArray("[{name : \"홍익수제비\", count : 3},{name : \"쉑섹버거\", count : 3}]");
            adapter.addItem(new OrderingSetItem.LobbyListViewItem(1002, menulist));
            adapter.addItem(new OrderingSetItem.LobbyListViewItem(1003, menulist));
        } catch (Exception e) {
            e.printStackTrace();
        }
        lvLobbyNowOrderList.setAdapter(adapter);
    }
}