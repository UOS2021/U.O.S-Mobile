package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uof.uof_mobile.R;

public class QRRecognitionActivity extends AppCompatActivity {
    private IntentIntegrator QRscan;

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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String targetIp = result.getContents().substring(result.getContents().indexOf("Ip") + 3, result.getContents().indexOf("&"));
        String Port = result.getContents().substring(result.getContents().indexOf("Port") + 5);
        int targetPort = Integer.parseInt(Port);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "성공: " + targetIp + " " + targetPort, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}