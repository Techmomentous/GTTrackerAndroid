
package com.lt.gpstracker.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.lt.gpstracker.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.lt.gpstracker.TrackerApplication;
import com.lt.gpstracker.activity.NotificationActivity;
import com.lt.gpstracker.activity.TrackerMainActivity;
import com.lt.gpstracker.utility.Constant;

import org.json.JSONObject;

public class GCMNotificationIntentService extends IntentService {

	public static final int		NOTIFICATION_ID	= 1;
	NotificationCompat.Builder	builder;
	String						message			= "";
	TrackerApplication				application		= null;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String	TAG	= "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			application = (TrackerApplication) getApplicationContext();
			Bundle extras = intent.getExtras();
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

			String messageType = gcm.getMessageType(intent);

			if (!extras.isEmpty()) {
				if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
					CustomNotification("Send error: " + extras.toString());
				}
				else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
					CustomNotification("Deleted messages on server: " + extras.toString());
				}
				else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

					for (int i = 0; i < 3; i++) {
						Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
						try {
							Thread.sleep(5000);
						}
						catch (InterruptedException e) {
							Log.d(TAG, "Error in onHandleIntent :" + e.getMessage());
						}
						catch (NullPointerException e) {
							Log.d(TAG, "Error in onHandleIntent :" + e.getMessage());
						}
						catch (Exception e) {
							Log.d(TAG, "Error in onHandleIntent :" + e.getMessage());
						}

					}
					Log.i(TAG, "Completed work " + SystemClock.elapsedRealtime());
					message = extras.get(TrackerApplication.EXTRA_MESSAGE).toString();

					//Send the push dNotification and on screen notification
					CustomNotification(message);


				}
			}
			GcmBroadcastReceiver.completeWakefulIntent(intent);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error onHandleIntent : " + e.getMessage());
		}
		catch (Exception e) {
			Log.e(TAG, "Error onHandleIntent : " + e.getMessage());
		}

	}

	/**
	 * Display A Custom Notification On TaskBar
	 *
	 * @param message
	 */
	public void CustomNotification(String message) {
		try {
			//{"messageCode":"","messageText":"Your Going To Call ME","messageDate":"Mon Jul 13 2015"}
			JSONObject jsonObject = new JSONObject(message);
			String messageCode = jsonObject.get("messageCode").toString();
			String messageText = jsonObject.get("messageText").toString();
			String messageDate = jsonObject.get("messageDate").toString();
			String currentTime = jsonObject.get("currentTime").toString();
			String tickerText = getString(R.string.newMessage);
			if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_105)) {
				tickerText = "Registration Notification";
			}
			NotificationCompat.Builder builder = new NotificationCompat.Builder(TrackerMainActivity.getInstance());
			// Using RemoteViews to bind custom layouts into Notification
			RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
			// Set RemoteViews into Notification
			builder.setContent(remoteViews);

			if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_101)) {
				remoteViews.setImageViewResource(R.id.image, R.drawable.message101);
				builder.setSmallIcon(R.drawable.message101);
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_102)) {
				remoteViews.setImageViewResource(R.id.image, R.drawable.message102);
				builder.setSmallIcon(R.drawable.message102);
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_103)) {
				remoteViews.setImageViewResource(R.id.image, R.drawable.message103);
				builder.setSmallIcon(R.drawable.message103);
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_104)) {
				remoteViews.setImageViewResource(R.id.image, R.drawable.message104);
				builder.setSmallIcon(R.drawable.message104);
			}
			else {
				remoteViews.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
				builder.setSmallIcon(R.mipmap.ic_launcher);
			}

			remoteViews.setTextViewText(R.id.title, "GT Application");
			remoteViews.setTextViewText(R.id.text, messageText);

			// Set Ticker Message
			builder.setTicker(tickerText);
			// Dismiss Notification
			builder.setAutoCancel(true);

			//For  message type registration we disable the button
			if (!messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_105)) {
				// Open NotificationView Class on Notification Click
				Intent intent = new Intent(TrackerMainActivity.getInstance(), NotificationActivity.class);
				// Send data to NotificationView Class
				intent.putExtra("messageCode", messageCode);
				intent.putExtra("messageText", messageText);
				intent.putExtra("messageDate", messageDate);
				intent.putExtra("currentTime", currentTime);
				// Open NotificationView.java Activity
				PendingIntent pIntent = PendingIntent.getActivity(TrackerMainActivity.getInstance(), 1, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// Set PendingIntent into Notification
				builder.setContentIntent(pIntent);
			}

			// Create Notification Manager
			NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound101);
			builder.setSound(sound);

			//Vibration
			builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

			builder.setLights(Color.RED, 3000, 3000);

			// Build Notification with Notification Manager
			notificationmanager.notify(0, builder.build());
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in sendNotification :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in sendNotification :" + e.getMessage());
		}

	}

}
