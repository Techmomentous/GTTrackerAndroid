
package com.lt.gpstracker.utility;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	private static final String			TAG				= "Utility";
	private static ConnectivityManager	connectivity	= null;
	private static NetworkInfo[]		info			= null;
	private static Pattern				pattern;
	private static Matcher				matcher;
	//Email Pattern
	private static final String			EMAIL_PATTERN		= "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
																	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public Utility() {

	}
	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Play a audio file when push notification is received
	 * 
	 * @param path
	 * @param fileName
	 * @param context
	 */
	public void audioPlayer(Context context, String soundFile) {

		try {
			//set up MediaPlayer    
			AssetFileDescriptor afd = context.getAssets().openFd(soundFile);
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while playing audio  :" + e.getMessage());
		}
		catch (IOException e) {
			Log.d(TAG, "Error while playing audio :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while playing audio  :" + e.getMessage());
		}
	}

	
	/**
	 * Check for network connection (WiFi or Data Service).
	 * 
	 * @return boolean true if network is connected else false.
	 */
	public static boolean isConnectedToNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		Log.d(TAG, "No internet access.");
		return false;
	}

	/**
	 * This method only check weather 2G/3G or Wifi connection is establish or
	 * not.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectedToInternet(Context context) {
		connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {

						return true;
					}

		}
		return false;
	}

	/**
	 * Show the Alert Dialog to user to enable network connection.
	 * 
	 * @param Context
	 *            context
	 */
	public static void ShowNetworkRequiredDialog(final Context ctxt) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
		builder.setMessage("Please enable wifi or data access via mobile network. Data charges may apply.").setTitle(
				"No internet access.");
		builder.setPositiveButton("Enable wifi", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				WifiManager wifiManager = (WifiManager) ctxt.getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);

			}
		});

		builder.setNegativeButton("Enable mobile data", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
				ComponentName cn = new ComponentName("com.android.phone", "com.android.phone.Settings");
				intent.setComponent(cn);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctxt.startActivity(intent);

			}
		});
		builder.show();

	}

	
}
