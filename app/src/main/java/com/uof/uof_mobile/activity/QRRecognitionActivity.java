package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SocketManager;

public class QRRecognitionActivity extends AppCompatActivity {
    private IntentIntegrator QRscan;
    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrrecognition);

        QRscan = new IntentIntegrator(this);
        QRscan.setOrientationLocked(false);
        QRscan.setPrompt("QR을 인식하여 주세요!");
        QRscan.setBeepEnabled(false);
        QRscan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String targetIp = result.getContents().substring(result.getContents().indexOf("Ip") + 3, result.getContents().indexOf("&"));
        int targetPort = Integer.parseInt(result.getContents().substring(result.getContents().indexOf("Port") + 5));

        if (result != null && result.getContents() != null) {
            // OrderingActivity로 Pos 접속 정보 전달
            Intent intent = new Intent(QRRecognitionActivity.this, OrderingActivity.class);
            intent.putExtra("targetIp", targetIp);
            intent.putExtra("targetPort", targetPort);
            startActivity(intent);
        } else {
            Toast.makeText(this, "QR코드 인식에 실패했습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}