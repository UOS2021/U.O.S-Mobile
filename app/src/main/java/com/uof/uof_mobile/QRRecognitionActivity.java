package com.uof.uof_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "성공: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}