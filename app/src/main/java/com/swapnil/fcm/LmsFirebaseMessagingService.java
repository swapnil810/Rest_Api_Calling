package com.swapnil.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swapnil.MainActivity;
import com.swapnil.R;
import com.swapnil.utills.MyConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class LmsFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String notificationType, title, meetupId, messageId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "onMessageReceived for FCM");
        if (remoteMessage.getData() != null) {
            Log.d(TAG, "c: " + remoteMessage.getData());
            JSONObject jsonObject = null;
            try {
                // object = new JSONObject(remoteMessage.getData().get("data"));
                jsonObject = new JSONObject(remoteMessage.getData());
                if (jsonObject.has("data")) {
                    //Application notification
                    JSONObject object = new JSONObject(remoteMessage.getData().get("data"));
                    Log.i("onMessageReceived: ", object.toString());
                    String title = object.optString("title");
                    notificationType = object.optString("type");
                    String message = object.optString("message");
                    meetupId = object.optString("meetup_id");
                    messageId = object.optString("message_id");
                    sendNotification(message, title);

                    /*Intent intent = new Intent(MyConstants.MESSAGE_ID);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);*/


                   /*if (notificationType.equalsIgnoreCase(getResources().getString(R.string.txt_appointment))) {
                        sendNotification(message, title);
                        Intent intent = new Intent(MyConstants.APPOINTMENT_NOTIFICATION);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }*/
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        // Check if message contains a data payload.

    }

       /* if (remoteMessage.getData() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());
            title = remoteMessage.getNotification().getTitle();
            sendNotification(remoteMessage.getNotification().getBody());
        }*/

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message
     * @param title
     */

    private void sendNotification(String message, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MyConstants.NOTIFICATION_TYPE, notificationType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder.setAutoCancel(true);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public interface MessageReceiver {
        void onReceive(String strMsg, String reservation_number);
    }
}
