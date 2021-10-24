package com.uos.uos_mobile.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 가장 먼저 호출되는 Launcher Activity.<br>
 * xml: activity_intro.xml<br><br>
 *
 * 인트로 화면을 표시하는 역할을 맡고 있으며 추가적으로 외부에서 QR코드를 통해 접속 시 이에 대한 처리를 담당하고
 * 있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class IntroActivity extends UosActivity {

    /**
     * IntroActivity는 예외적으로 onCreate() 내에서 clear() 함수를 실행합니다. 이는 기존에 실행 중인
     * Activity가 있을 경우 모든 Activity를 종료하여 불필요한 Activity가 스택에 쌓여있는 것을 방지해줍니다.
     *
     * @param savedInstanceState null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clear();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_intro);

        SharedPreferencesManager.open(IntroActivity.this, Global.SharedPreference.APP_DATA);
        if ((Boolean) SharedPreferencesManager.load(Global.SharedPreference.IS_FIRST, true) == true) {

            /* 만약 앱이 최초 실행일 경우 기본 데이터 초기화 */

            SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, false);
            SharedPreferencesManager.save(Global.SharedPreference.USER_ID, "");
            SharedPreferencesManager.save(Global.SharedPreference.USER_PW, "");
            SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, "");
            SharedPreferencesManager.save(Global.SharedPreference.IS_FIRST, false);
            SharedPreferencesManager.save(Global.SharedPreference.LAST_NOTIFICATION_ID, 0);

            /*
             * NotificationChannel 생성
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                /* Android 8.0 이상일 경우 */

                NotificationChannel channel = new NotificationChannel(Global.Notification.CHANNEL_ID, Global.Notification.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("UOS 푸시알림입니다");
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        /*
         * Firebase Cloud Messaging을 위한 Token 생성
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
        } else if (getIntent().getIntExtra("orderCode", -1) != -1) {

            /* 외부에서 앱이 호출되었을 경우: 외부서버에서 전달받은 주문 수락, 상품 준비됨에 해당하는
             * Notification을 클릭했을 경우
             */

            int orderCode = getIntent().getIntExtra("orderCode", -1);
            if (orderCode != -1) {
                intent.putExtra("orderCode", orderCode);
            }
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