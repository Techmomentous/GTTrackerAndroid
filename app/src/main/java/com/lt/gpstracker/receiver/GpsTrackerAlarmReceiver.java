
package com.lt.gpstracker.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.lt.gpstracker.services.GLocationService;



// make sure we use a WakefulBroadcastReceiver so that we acquire a partial wakelock
public class GpsTrackerAlarmReceiver extends WakefulBroadcastReceiver {
	private static final String	TAG	= "GpsTrackerAlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			//Once the broad cast is received from pending intent trigger the Location Service
			context.startService(new Intent(context, GLocationService.class));
			Log.d(TAG, "Started Service");
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error : " + e);
		}
	}
}
