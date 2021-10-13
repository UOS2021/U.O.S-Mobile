package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.other.Global;

import java.util.Timer;
import java.util.TimerTask;

/**
 * U.O.S-Mobile 실행 시 가장 먼저 호출되는 Activity.<br>
 * xml: activity_intro.xml<br><br>
 * 인트로 화면을 표시하는 역할을 맡고 있으며 추가적으로 외부에서 QR코드를 통해 접속 시 이에 대한 처리를 담당하고 있습
 * 니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class IntroActivity extends UosActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clear();
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_intro);

        init();
    }

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    private void init() {
        SharedPreferenceManager.open(IntroActivity.this, Global.SharedPreference.APP_DATA);
        if (SharedPreferenceManager.load(Global.SharedPreference.IS_FIRST, true) == true) {
            
            /* 만약 앱이 최초 실행일 경우 기본 데이터 초기화 */
            
            SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, false);
            SharedPreferenceManager.save(Global.SharedPreference.USER_ID, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_PW, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, "");
            SharedPreferenceManager.save(Global.SharedPreference.IS_FIRST, false);
            SharedPreferenceManager.save(Global.SharedPreference.LAST_NOTIFICATION_NUMBER, 0);


            /*
             * Firebase Cloud Messaging을 위한 Token 생성
             * Token은 앱 최초 설치시에만 부여
            */

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {

                            /* FCM Token 생성 실패 시 */

                            Log.w("UOS_MOBILE_FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String fcmToken = task.getResult();
                        Log.d("UOS_MOBILE_FCM", fcmToken);

                        /*
                         * 생성된 FCM Token을 Global.Firebase.FCM_TOKEN에 저장.
                         * 저장된 토큰은 주문 시 Pos기에 전송하여 Notification을 받는 용도로 사용.
                         */
                        Global.Firebase.FCM_TOKEN = fcmToken;
                    });
        }


        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            
            /* 외부에서 앱이 호출되었을 경우: 카메라를 통해 U.O.S QR코드를 인식했을 경우 */
            
            Uri uri = getIntent().getData();

            if (uri != null) {

                /*
                 * 해당 QR코드로부터 U.O.S 파트너의 ID를 추출 후 LoginActivity로 전달
                 */

                try {
                    intent.putExtra("uosPartnerId", uri.getQueryParameter("uosPartnerId"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(IntroActivity.this, "등록되지 않은 매장입니다", Toast.LENGTH_SHORT).show();
                }
            }
        } else if(getIntent().getDataString() != null){

            /* 외부에서 앱이 호출되었을 경우: 외부서버에서 전달받은 Notification을 클릭했을 경우 */

            intent.putExtra("orderCode", getIntent().getDataString());
        }

        /* 일정 시간이 지난 후 IntroActivity 종료 및 LoginActivity로 이동 */
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}