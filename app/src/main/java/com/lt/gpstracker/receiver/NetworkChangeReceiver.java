
package com.lt.gpstracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lt.gpstracker.fragments.MyTrackerFragment;
import com.lt.gpstracker.utility.InternetManager;

public class NetworkChangeReceiver extends BroadcastReceiver {
	private static final String	TAG	= "NetworkChangeReceiver";

	public NetworkChangeReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			int status = InternetManager.getConnectivityStatusString(context);
			Log.e("Sulod sa network reciever", "Sulod sa network reciever");
			if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
				if (status == InternetManager.NETWORK_STATUS_NOT_CONNECTED) {
					//DO Nothing
				}
				else {
					//if network is connected trigger the tracking
					MyTrackerFragment.getInstance().trackLocation(context);
				}
			}
		}
		catch (Exception e) {
			Log.e(TAG, "Error " + e);
		}
	}
}
