package com.uos.uos_mobile.other;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uos.uos_mobile.activity.IntroActivity;
import com.uos.uos_mobile.activity.LobbyActivity;
import com.uos.uos_mobile.activity.OrderListActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.manager.SQLiteManager;
import com.uos.uos_mobile.manager.SharedPreferenceManager;

import java.util.Map;

public class UosFcmService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("UOS_MOBILE_FCM", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            
            /* FCM이 data 형식으로 왔을 경우 */
            
            Map<String, String> recvData = remoteMessage.getData();
            String type = recvData.get("type");

            Intent intent = new Intent(this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this, Global.Notification.CHANNEL_ID)
                    .setSmallIcon(com.uos.uos_mobile.R.mipmap.ic_uos_logo_round)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if(type.equals("order")){
                
                /* 상품이 준비되었다는 FCM일 경우 */
                
                String companyName = recvData.get("company_name");
                String orderCode = recvData.get("order_code");

                SQLiteManager sqLiteManager = new SQLiteManager(getApplicationContext());
                sqLiteManager.openDatabase();
                sqLiteManager.setOrderState(Integer.valueOf(orderCode), Global.SQLite.ORDER_STATE_PREPARED);
                sqLiteManager.closeDatabase();

                final UosActivity lobbyActivity = UosActivity.get(LobbyActivity.class);

                if (lobbyActivity != null) {
                    lobbyActivity.runOnUiThread(() -> {
                        ((LobbyActivity) lobbyActivity).updateList();
                        ((LobbyActivity) lobbyActivity).moveToOrderCode(Integer.valueOf(orderCode));
                    });
                } else {
                    final UosActivity orderListActivity = UosActivity.get(OrderListActivity.class);

                    if (orderListActivity != null) {
                        orderListActivity.runOnUiThread(() -> {
                            ((OrderListActivity) orderListActivity).doUpdateOrderScreen();
                        });
                    }
                }

                intent.setData(Uri.parse(orderCode));

                notificationCompatBuilder
                        .setContentTitle(companyName + "에서 주문하신 상품이 준비되었습니다")
                        .setContentText("카운터에서 상품을 수령해주세요 (주문코드: " + orderCode + ")");
            }else if(type.equals("alert")){

                /* 방역동선 관련된 알림일 경우 */

                String companyName = recvData.get("company_name");
                String message = recvData.get("message");

                notificationCompatBuilder
                        .setContentTitle("UOS 재난 알리미")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("고객님께서 방문하셨던 " + companyName + "매장에서 확진자가 발생했습니다. " + message))
                        .setContentText(companyName + "에서 확진자 발생");
            }

            SharedPreferenceManager.open(this, "");
            int notificationId = SharedPreferenceManager.load(Global.SharedPreference.LAST_NOTIFICATION_ID, 0);
            SharedPreferenceManager.save(Global.SharedPreference.LAST_NOTIFICATION_ID, notificationId + 1);
            SharedPreferenceManager.close();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(notificationId, notificationCompatBuilder.build());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("UOS_MOBILE_FCM", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(this, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }
    }
}
