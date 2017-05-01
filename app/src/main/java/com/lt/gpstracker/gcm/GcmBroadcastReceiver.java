
package com.lt.gpstracker.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	private static final String	TAG	= "GcmBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			ComponentName comp = new ComponentName(context.getPackageName(),
					GCMNotificationIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in GcmBroadcastReceiver :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in GcmBroadcastReceiver :" + e.getMessage());
		}
	}
}
