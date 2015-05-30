package com.bookstore.app.base;

import com.bookstore.app.activity.ActivityTADADetails;
import com.bookstore.app.activity.AdminJobAcceptRejectActivity;
import com.bookstore.app.activity.AgentDonationAcceptRejectResultActivity;
import com.bookstore.app.activity.AgentIndividualJobDetailsActivity;
import com.bookstore.app.activity.AgentJobAcceptRejectResultActivity;
import com.bookstore.app.activity.AgentTADAResultActivity;
import com.bookstore.app.activity.DonationAcceptRejectActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.PushNotification;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
	public final static String TAG = "GCM";
	public static int NOTIFICATION_ID = 8;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	// Notification Status
	public static final String DONATION_ACCEPT = "DONATION_COMPLETE";
	public static final String DONATION_REJECT = "DONATION_REJECTED";
	public static final String DONATION_SEND = "DONATION_SEND";

	public static final String TADA_ACCEPT = "TADA_ACCEPT";
	public static final String TADA_SEND = "TADA_SEND";

	public static final String JOB_COMPLETE = "JOB_COMPLETE";
	public static final String JOB_SUBMIT = "JOB_SUBMIT";
	public static final String JOB_CREATE = "JOB_CREATE";
	public static final String JOB_REJECTED = "JOB_REJECTED";

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(extras.getString("message"));
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		
		Log.d("BSS", "GCM : "+msg);
		Gson gson = new Gson();
		PushNotification pushNotification = gson.fromJson(msg,
				PushNotification.class);
		if (pushNotification != null) {
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Intent intent = null;

			if (pushNotification.Status.equals(DONATION_ACCEPT)) {
				intent = new Intent(this,
						AgentDonationAcceptRejectResultActivity.class);
				intent.putExtra("DONATION_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 0;

			} else if (pushNotification.Status.equals(DONATION_REJECT)) {
				intent = new Intent(this,
						AgentDonationAcceptRejectResultActivity.class);
				intent.putExtra("DONATION_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 0;

			} else if (pushNotification.Status.equals(DONATION_SEND)) {
				intent = new Intent(getApplicationContext(),
						DonationAcceptRejectActivity.class);
				intent.putExtra("DONATION_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 1;

			}/* else if (pushNotification.Status.equals(JOB_COMPLETE)) {
				intent = new Intent(this, AdminJobAcceptRejectActivity.class);
				intent.putExtra("JOB_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 2;

			}*/ else if (pushNotification.Status.equals(JOB_CREATE)) {
				intent = new Intent(this,
						AgentIndividualJobDetailsActivity.class);
				intent.putExtra("JOB_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 3;

			} else if (pushNotification.Status.equals(JOB_SUBMIT)) {
				intent = new Intent(getApplicationContext(),
						AdminJobAcceptRejectActivity.class);
				intent.putExtra("JOB_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 2;

			} else if (pushNotification.Status.equals(JOB_REJECTED)||pushNotification.Status.equals(JOB_COMPLETE)) {
				intent = new Intent(getApplicationContext(),
						AgentJobAcceptRejectResultActivity.class);
				intent.putExtra("JOB_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 3;

			} else if (pushNotification.Status.equals(TADA_ACCEPT)) {
				intent = new Intent(this, AgentTADAResultActivity.class);
				intent.putExtra("TADA_ID", "" + pushNotification.id);
				NOTIFICATION_ID = 4;

			} else if (pushNotification.Status.equals(TADA_SEND)) {
				intent = new Intent(getApplicationContext(),
						ActivityTADADetails.class);

				intent.putExtra("TADA", "" + pushNotification.id);
				NOTIFICATION_ID = 5;

			} else {
				return;
			}

			//
			intent.putExtra("MODE", "0");

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Book Store")
					.setStyle(
							new NotificationCompat.BigTextStyle()
									.bigText(pushNotification.Message))
					.setContentText(pushNotification.Message);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}

}
