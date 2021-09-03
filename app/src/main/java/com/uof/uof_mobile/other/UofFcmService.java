package com.uof.uof_mobile.other;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.activity.IntroActivity;
import com.uof.uof_mobile.activity.LobbyActivity;
import com.uof.uof_mobile.activity.OrderListActivity;
import com.uof.uof_mobile.manager.SQLiteManager;
import com.uof.uof_mobile.manager.SharedPreferenceManager;

import java.util.Map;

public class UofFcmService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("UOF_MOBILE_FCM", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> recvData = remoteMessage.getData();
            String companyName = recvData.get("company_name");
            String orderNumber = recvData.get("order_number");

            SQLiteManager sqLiteManager = new SQLiteManager(getApplicationContext());
            sqLiteManager.openDatabase();
            sqLiteManager.setOrderState(Integer.valueOf(orderNumber), "prepared");
            sqLiteManager.closeDatabase();

            for (Activity activity : Global.activities) {
                if (activity instanceof LobbyActivity) {
                    activity.runOnUiThread(() -> {
                        ((LobbyActivity) activity).updateList();
                        ((LobbyActivity) activity).moveToOrderNumber(Integer.valueOf(orderNumber));
                    });
                    break;
                }else if(activity instanceof OrderListActivity){
                    activity.runOnUiThread(() -> {
                        ((OrderListActivity)activity).doUpdateOrderScreen();
                    });
                }
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), IntroActivity.class), PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, Global.Notification.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(companyName + "에서 주문하신 상품이 준비되었습니다")
                    .setContentText("카운터에서 상품을 수령해주세요")
                    .setFullScreenIntent(pendingIntent, true)
                    .setAutoCancel(true)
                    .setGroup(Global.Notification.GROUP_ID);

            NotificationCompat.Builder notificationGroup = new NotificationCompat.Builder(this, Global.Notification.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroup(Global.Notification.GROUP_ID)
                    .setAutoCancel(true)
                    .setGroupSummary(true);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            SharedPreferenceManager.open(getApplicationContext(), Global.SharedPreference.APP_DATA);
            int notificationNumber = SharedPreferenceManager.load(Global.SharedPreference.SP_KEY_LAST_NOTIFICATION_NUMBER, 0) + 1;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(Global.Notification.CHANNEL_ID, Global.Notification.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManagerCompat.createNotificationChannel(notificationChannel);
            }

            notificationManagerCompat.notify(notificationNumber, notification.build());
            notificationManagerCompat.notify(0, notificationGroup.build());
            SharedPreferenceManager.save(Global.SharedPreference.SP_KEY_LAST_NOTIFICATION_NUMBER, notificationNumber);
            SharedPreferenceManager.close();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("UOF_MOBILE_FCM", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }
    }
}
