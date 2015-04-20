package com.bookstore.app.base;

import com.bookstore.app.activity.AgentIndividualJobDetailsActivity;
import com.bookstore.app.activity.IndividualJobDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.PushNotification;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.in;
import com.google.gson.Gson;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
	public final static String TAG = "GCM";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras.getString("message"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    
    private void sendNotification(String msg) {
    	Gson gson = new Gson();
    	PushNotification pushNotification = gson.fromJson(msg, PushNotification.class);
    	if(pushNotification != null){
    		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
    		Intent intent = new Intent(this, AgentIndividualJobDetailsActivity.class);
    		intent.putExtra("JOB_ID", ""+pushNotification.id);
    		intent.putExtra("MODE", "0");
    		
    		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
    		NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setContentTitle("Book Store")
	            .setStyle(new NotificationCompat.BigTextStyle()
	            .bigText(pushNotification.Message))
	            .setContentText(pushNotification.Message);
    		mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    	} 
    }

}
