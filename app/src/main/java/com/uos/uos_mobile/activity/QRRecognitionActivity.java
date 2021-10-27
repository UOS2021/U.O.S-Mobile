package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * QR코드를 인식 후 데이터를 추출하는 Activity.<br>
 * xml: activity_qrrecognition.xml<br><br>
 *
 * Intent로 넘어온 U.O.S 파트너 아이디가 있을 경우 QR코드를 인식하지 않고 바로 해당 U.O.S 파트너의 매장 상품 목록
 * 을 불러옵니다. 추출된 아이디가 없을 경우에는 QR코드 인식화면을 표시합니다.
 *
 * @author Yoon Jong Beom
 * @since 1.0.0
 */
public class QRRecognitionActivity extends UosActivity {

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_qrrecognition);

        Intent qrRecognitionActivityIntent = getIntent();
        if (qrRecognitionActivityIntent.getStringExtra("uosPartnerId") != null) {

            /* Intent로 U.O.S 파트너 아이디가 전달되었을 경우 */

            loadStoreProduct(qrRecognitionActivityIntent.getStringExtra("uosPartnerId"));
        } else {

            /* Intent로 U.O.S 파트너 아이디가 전달되지 않았을 경우 - QR코드 인식화면 표시 */
            IntentIntegrator qrScan = new IntentIntegrator(this);
            qrScan.setOrientationLocked(false);
            qrScan.setPrompt("QR 코드를 인식해주세요");
            qrScan.setBeepEnabled(false);
            qrScan.initiateScan();
        }
    }

    /**
     * QR코드 인식 화면이 종료된 후 처리를 구현하기 위해 오버라이딩한 함수입니다.
     *
     * @param requestCode Activity를 호출한 requestCode.
     * @param resultCode 호출한 Activity에서 설정한 값.
     * @param data 호출된 Activity에서 저장된 값.
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

            try {
                String uosPartnerId = result.getContents().substring(result.getContents().indexOf("uosPartnerId=") + 13, result.getContents().length() - 1);
                loadStoreProduct(uosPartnerId);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(QRRecognitionActivity.this, "유효하지 않은 QR코드입니다", Toast.LENGTH_SHORT).show();
            }
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

        /* 퉵 통신으로 매장 정보 불러오기 */

        new Thread(() -> {
            JSONObject sendData = new JSONObject();
            JSONObject message = new JSONObject();

            try {
                sendData.accumulate("request_code", Global.Network.Request.STORE_PRODUCT_INFO);
                message.accumulate("uospartner_id", uosPartnerId);
                sendData.accumulate("message", message);

                String temp = new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(5000), sendData.toString()}).get();
                JSONObject recvData = new JSONObject(temp);

                if (recvData == null) {

                    /* Pos기로부터 수신된 데이터가 없을 경우 */

                    Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                } else {

                    /* Pos기로부터 수신된 데이터가 있을 경우 */

                    String responseCode = recvData.getString("response_code");

                    SharedPreferencesManager.open(QRRecognitionActivity.this, Global.SharedPreference.APP_DATA);
                    SharedPreferencesManager.save(Global.SharedPreference.TEMP_MESSAGE, recvData.getJSONObject("message").toString());
                    SharedPreferencesManager.close();

                    /*
                     * 아래 if구문은 어떠한 매장에서 불러왔는지를 구분. 만약 새로운 형태의 상품 데이터를 가지고
                     * 있는 매장을 추가했을 경우 Global.Response에서 해당 매장에 대한 코드를 추가하고 아래
                     * if문에 else-if로 추가하여 구분.
                     */
                    Class targetClass = null;
                    if (responseCode.equals(Global.Network.Response.STORE_PRODUCT_INFO)) {

                        /* 일반 매장일 경우 */

                        targetClass = OrderingActivity.class;
                    } else if (responseCode.equals(Global.Network.Response.THEATER_PRODUCT_INFO)) {

                        /* 영화관일 경우 */

                        targetClass = MovieOrderingActivity.class;
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                        /* 서버 연결 실패 */

                        runOnUiThread(() -> {
                            Toast.makeText(QRRecognitionActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                        });
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
                }
                finish();
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(QRRecognitionActivity.this, "매장 연결 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }
}