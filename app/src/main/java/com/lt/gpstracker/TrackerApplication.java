
package com.lt.gpstracker;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class TrackerApplication extends Application {
	public static final String	DISPLAY_MESSAGE_ACTION	= "com.saa.agent.application.DISPLAY_MESSAGE";
	public static final String	EXTRA_MESSAGE			= "message";
	private static final String	TAG						= "GTApplication";

	public TrackerApplication() {
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	public void onTerminate() {

		super.onTerminate();
	}

	private PowerManager.WakeLock	wakeLock;

	/**
	 * Get the wake lock
	 * 
	 * @param context
	 */
	public void acquireWakeLock(Context context) {
		try {
			if (wakeLock != null)
				wakeLock.release();

			PowerManager powerManagement = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

			wakeLock = powerManagement.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, "WakeLock");

			wakeLock.acquire();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in acquireWakeLock :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in acquireWakeLock :" + e.getMessage());
		}
	}

	/**
	 * Relase the Wake Lock Resources
	 */
	public void releaseWakeLock() {
		try {
			if (wakeLock != null)
				wakeLock.release();
			wakeLock = null;
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in releaseWakeLock :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in releaseWakeLock :" + e.getMessage());
		}
	}
}
