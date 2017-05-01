
package com.lt.gpstracker.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

public class GpsTrackerBootReceiver extends BroadcastReceiver {
	private static final String	TAG	= "GpsTrackerBootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		//Once Receive of pending intent broad cast by Alarm Service 
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		//Create the tracker intent
		Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);

		//Create the pending intent to trigger it again at user define interval
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

		//Get the shared sharedPreferences object to get the interval minutes values   
		SharedPreferences sharedPreferences = context.getSharedPreferences("com.rizem.gpstracker.prefs",
				Context.MODE_PRIVATE);
		int intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 1);

		//Check if tracking is already running
		Boolean currentlyTracking = sharedPreferences.getBoolean("currentlyTracking", false);

		//if tracking is already running
		if (currentlyTracking) {

			//Set the alarm repeating for the user define interval  of the pending intent
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
					intervalInMinutes * 60000, // 60000 = 1 minute,
					pendingIntent);
		}
		else {

			//Cancel the alarm manager if tracking is not running we don't want to reduce the resource of user device
			alarmManager.cancel(pendingIntent);
			Log.d(TAG, "Alaram caneled");
		}
		Log.d(TAG, "Created Alaram");
	}
}
