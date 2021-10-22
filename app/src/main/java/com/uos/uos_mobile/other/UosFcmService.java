package com.uos.uos_mobile.other;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uos.uos_mobile.activity.IntroActivity;
import com.uos.uos_mobile.activity.LobbyActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.dialog.OrderDetailDialog;
import com.uos.uos_mobile.dialog.UosDialog;
import com.uos.uos_mobile.manager.SharedPreferencesManager;

import java.util.Map;

public class UosFcmService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("UOS_MOBILE_FCM", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {

            /* FCM이 data 형식으로 왔을 경우 */

            Map<String, String> recvData = remoteMessage.getData();
            String responseCode = recvData.get("response_code");
            String companyName = recvData.get("company_name");

            SharedPreferencesManager.open(getApplicationContext(), "");
            int notificationId = (Integer) SharedPreferencesManager.load(Global.SharedPreference.LAST_NOTIFICATION_ID, 0);
            SharedPreferencesManager.save(Global.SharedPreference.LAST_NOTIFICATION_ID, notificationId + 1);
            SharedPreferencesManager.close();

            NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this, Global.Notification.CHANNEL_ID)
                    .setSmallIcon(com.uos.uos_mobile.R.mipmap.icon_uos_round)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            if (responseCode.equals(Global.Network.Response.FCM_QUARANTINE_NOTICE)) {

                /* 방역동선 관련된 알림일 경우 */

                notificationCompatBuilder
                        .setContentTitle("UOS 재난 알리미")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("고객님께서 방문하셨던 " + companyName + "매장에서 확진자가 발생했습니다. " + recvData.get("message")))
                        .setContentText(companyName + "에서 확진자 발생");

                if (UosActivity.activities.size() == 0) {

                    /* 앱이 실행 중이지 않을 경우 */

                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
                    notificationCompatBuilder.setContentIntent(pendingIntent);
                }else{

                    /* 앱이 실행 중일 경우 */

                    Intent intent = new Intent(getApplicationContext(), UosActivity.get(-1).getClass());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
                    notificationCompatBuilder.setContentIntent(pendingIntent);
                }
            } else{

                /* 그 외의 알림일 경우 */

                int orderCode = Integer.valueOf(recvData.get("order_code"));

                if (responseCode.equals(Global.Network.Response.FCM_ORDER_ACCEPT)) {

                    /* 주문이 수락되었다는 알림일 경우 */

                    notificationCompatBuilder
                            .setContentTitle(companyName + "에서 주문을 수락하였습니다")
                            .setContentText("상품이 준비되는 동안 기다려주세요 (주문코드: " + orderCode + ")");

                } else if (responseCode.equals(Global.Network.Response.FCM_ORDER_REFUSE)) {

                    /* 주문이 거절되었다는 알림일 경우 */

                    orderCode = -1;

                    notificationCompatBuilder
                            .setContentTitle(companyName + "에서 주문을 거절하였습니다")
                            .setContentText("현재 매장이 바쁩니다. 다음에 다시 주문해주세요");
                } else if (responseCode.equals(Global.Network.Response.FCM_ORDER_PREPARED)) {

                    /* 상품이 준비되었다는 알림일 경우 */

                    notificationCompatBuilder
                            .setContentTitle(companyName + "에서 주문하신 상품이 준비되었습니다")
                            .setContentText("카운터에서 상품을 수령해주세요 (주문코드: " + orderCode + ")");
                }

                Intent intent;

                if (UosActivity.get(LobbyActivity.class) != null) {

                    /* LobbyActivity가 실행 중일 경우 */

                    ((LobbyActivity) UosActivity.get(LobbyActivity.class)).updateList(orderCode, false);

                    intent = new Intent(getApplicationContext(), LobbyActivity.class);

                    if(UosActivity.get(-1).getClass().equals(LobbyActivity.class)){

                        /* LobbyActivity가 최상단에서 실행 중일 경우 */

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }else{

                        /* LobbyActivity가 최상단에서 실행 중이지 않을 경우 */

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                }else{
                    
                    /* LobbyActivity가 실행 중이지 않을 경우 */
                    
                    intent = new Intent(getApplicationContext(), IntroActivity.class);
                }

                if (UosDialog.get(OrderDetailDialog.class) != null) {

                    /* OrderDetailDialog 실행 중일 경우 */

                    ((OrderDetailDialog)UosDialog.get(OrderDetailDialog.class)).updateOrderState(orderCode, responseCode);
                }

                intent.putExtra("orderCode", orderCode);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
                notificationCompatBuilder.setContentIntent(pendingIntent);
            }

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(notificationId, notificationCompatBuilder.build());
        }

        if (remoteMessage.getNotification() != null) {

            /* FCM이 notification 형식으로 왔을 경우 */

            Log.d("UOS_MOBILE_FCM", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }
    }
}
