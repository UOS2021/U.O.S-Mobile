package com.uos.uos_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class QRRecognitionActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_qrrecognition);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void init() {
        Global.addActivity(this, false);

        Intent qrRecognitionActivityIntent = getIntent();
        if (qrRecognitionActivityIntent.getStringExtra("uosPartnerId") != null) {
            loadStoreProduct(qrRecognitionActivityIntent.getStringExtra("uosPartnerId"));
        } else {
            qrScan = new IntentIntegrator(this);
            qrScan.setOrientationLocked(false);
            qrScan.setPrompt("QR 코드를 인식해주세요");
            qrScan.setBeepEnabled(false);
            qrScan.initiateScan();
        }
    }

    /**
     * QR코드 인식창이 종료된 후 실행되는 함수.
     *
     * @param requestCode .
     * @param resultCode .
     * @param data .
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            /*
             * QR코드에서 데이터를 불러왔을 경우 QR코드 데이터에서 ip와 port, id를 분리 후 해당 주소로 접속하여
             * 상품정보를 불러옴
             */

            String uosPartnerId = result.getContents().substring(result.getContents().indexOf("uosPartnerId=", 13));

            loadStoreProduct(uosPartnerId);
        } else {

            /* QR코드에서 데이터를 불러오지 못했을 경우 */

            Toast.makeText(this, "QR코드 인식에 실패했습니다", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * 매개변수로 들어온 uospartner 접속하여 구매 가능한 상품 목록을 불러오는 함수.
     *
     * @param uosPartnerId 접속하려는 매장의 U.O.S 파트너 아이디.
     */
    private void loadStoreProduct(String uosPartnerId) {
        // 퉵 통신으로 매장 정보 불러오기
        new Thread(() -> {
            JSONObject sendData = new JSONObject();
            JSONObject message = new JSONObject();

            if (Global.User.type.equals("customer")) {

                /* 일반 고객일 경우 */

                try {
                    sendData.accumulate("request_code", Global.Network.Request.STORE_PRODUCT_INFO);
                    message.accumulate("uospartner_id", uosPartnerId);
                    sendData.accumulate("message", message);

                    JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(Global.Network.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(Global.Network.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                    if (recvData == null) {

                        /* Pos기로부터 수신된 데이터가 없을 경우 */

                        Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                        /* Pos기로부터 수신된 데이터가 있을 경우 */

                        String responseCode = recvData.getString("response_code");

                        SharedPreferenceManager.open(QRRecognitionActivity.this, Global.SharedPreference.APP_DATA);
                        SharedPreferenceManager.save(Global.SharedPreference.TEMP_MESSAGE, recvData.getJSONObject("message").toString());
                        SharedPreferenceManager.close();

                        /*
                         * 아래 if구문은 어떠한 매장에서 불러왔는지를 구분하기 위함. 만약 새로운 형태의 상품
                         * 데이터를 가지고 있는 매장을 추가했을 경우 Global.Response에서 해당 매장에 대한
                         * 코드를 추가하고 아래 if문에 else-if로 추가하여 구분하면 됨.
                         */
                        Class targetClass = null;
                        if (responseCode.equals(Global.Network.Response.STORE_PRODUCT_INFO)) {
                            /* 일반 매장일 경우 */

                            targetClass = OrderingActivity.class;
                        } else if (responseCode.equals(Global.Network.Response.THEATER_PRODUCT_INFO)) {

                            /* 영화관일 경우 */

                            targetClass = MovieOrderingActivity.class;
                        } else {

                            /* 등록된 ResponseCode가 없을 경우 */

                            runOnUiThread(() -> {
                                Toast.makeText(QRRecognitionActivity.this, "등록되지 않은 매장입니다", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                            return;
                        }

                        Intent intent = new Intent(QRRecognitionActivity.this, targetClass);
                        intent.putExtra("uosPartnerId", uosPartnerId);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } else if (Global.User.type.equals("uospartner")) {

                /* U.O.S 파트너일 경우 */

                try {
                    sendData.accumulate("request_code", Global.Network.Request.QR_IMAGE);
                    message.accumulate("uospartner_id", uosPartnerId);
                    sendData.accumulate("message", message);

                    JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(Global.Network.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(Global.Network.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                    if (recvData == null) {

                        /* 수신 데이터가 없을 경우 */

                        Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                        /* 수신 데이터가 있을 경우 */

                        String responseCode = recvData.getString("response_code");

                        if (responseCode.equals(Global.Network.Response.QR_IMAGE_SUCCESS)) {
                            SharedPreferenceManager.open(QRRecognitionActivity.this, Global.SharedPreference.APP_DATA);
                            SharedPreferenceManager.save(Global.SharedPreference.QR_IMAGE, recvData.getJSONObject("message").getString("image"));
                            SharedPreferenceManager.close();
                            runOnUiThread(() -> {
                                Toast.makeText(QRRecognitionActivity.this, "매장 QR코드를 저장했습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else if (responseCode.equals(Global.Network.Response.QR_IMAGE_FAILED)) {
                            runOnUiThread(() -> {
                                Toast.makeText(QRRecognitionActivity.this, "QR코드를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        }).start();
    }
}