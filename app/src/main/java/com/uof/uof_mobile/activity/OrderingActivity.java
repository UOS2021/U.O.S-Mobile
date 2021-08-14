package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderingActivity extends AppCompatActivity {
    private String targetIp;
    private int targetPort;
    private SocketManager socketManager;
    private AppCompatImageButton ibtnOrderingBack;
    private AppCompatTextView tvOrderingCompanyName;
    private AppCompatTextView tvOrderingCompanyDesc;
    private AppCompatTextView tvOrderingCompanyAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);

        init();
    }

    private void init() {
        ibtnOrderingBack = findViewById(R.id.ibtn_ordering_back);
        tvOrderingCompanyName = findViewById(R.id.tv_ordering_companyname);
        tvOrderingCompanyDesc = findViewById(R.id.tv_ordering_companydesc);
        tvOrderingCompanyAddress = findViewById(R.id.tv_ordering_companyaddress);

        Intent intent = getIntent();
        targetIp = intent.getStringExtra("targetIp");
        targetPort = intent.getIntExtra("targetPort", 0);

        socketManager = new SocketManager();
        socketManager.setSocket(targetIp, targetPort);

        new Thread(() -> {
            if (socketManager.connect(2000)) {
                JSONObject sendData = new JSONObject();

                try {
                    sendData.accumulate("request_code", Constants.Network.Request.STORE_PRODUCT_INFO);
                    socketManager.send(sendData.toString());

                    String recvData = socketManager.recv();

                    if (recvData == null) {
                        Toast.makeText(OrderingActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject data = new JSONObject(recvData);
                        String responseCode = data.getString("response_code");

                        if (responseCode.equals(Constants.Network.Response.SERVER_NOT_ONLINE)) {
                            Toast.makeText(OrderingActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject companyData = data.getJSONObject("message").getJSONObject("company");
                            JSONArray productData = data.getJSONObject("message").getJSONArray("product_list");

                            tvOrderingCompanyName.setText(companyData.getString("name"));
                            tvOrderingCompanyDesc.setText(companyData.getString("desc"));
                            tvOrderingCompanyAddress.setText(companyData.getString("address"));

                        }
                    }
                } catch (JSONException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderingActivity.this, "매장 연결 중 문제가 발생했습니다: " + e.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(OrderingActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                });
            }
            finish();
        }).start();
    }
}