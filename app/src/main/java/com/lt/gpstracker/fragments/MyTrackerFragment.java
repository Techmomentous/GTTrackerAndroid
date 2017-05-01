
package com.lt.gpstracker.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lt.gpstracker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lt.gpstracker.receiver.GpsTrackerAlarmReceiver;
import com.lt.gpstracker.utility.Preference;
import com.lt.gpstracker.utility.Utility;

import java.util.UUID;

public class MyTrackerFragment extends Fragment {
	private static final String	TAG					= "MyTrackerFragment";
	// use the websmithing defaultUploadWebsite for testing and then check your
	// location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php

	private static EditText		txtUserName;
	private static EditText		txtMemberID;
	private static EditText		txtWebsite;
	private static TextView		txtInterval;
	private static TextView		txtLatituted;
	private static TextView		txtLongtitued;
	private static Button		trackingButton;

	private boolean				currentlyTracking;
	private int					intervalInMinutes	= 1;
	private AlarmManager		alarmManager;
	private Intent				gpsTrackerIntent;
	private PendingIntent		pendingIntent;
	static MyTrackerFragment	tracker;
	private Context				context;

	public MyTrackerFragment(Context context) {
		this.context = context;

	}

	public static MyTrackerFragment getInstance() {
		return tracker;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_tracker, container, false);
		//Set signle instance
		tracker = this;
		try {
			Preference preference = new Preference();
			if (context == null) {
				context = getActivity().getApplicationContext();
			}

			//Initialize all controls
			initalizeControls(rootView);

			//Display previouse saved settings
			displayUserSettings(context);

			//Check tracking is currently on or off
			currentlyTracking = preference.getCurrenltyTracking(context);

			trackingButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					//Check user is connected to network
					if (!Utility.isConnectedToInternet(context)) {
						try {
							Utility.ShowNetworkRequiredDialog(context);

						}
						catch (Exception e) {
							Log.e(TAG, "Error feedback : " + e.getMessage());
						}
					}
					else {

						trackLocation(context);
					}
				}
			});
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in onCreateView :" + e);

		}
		catch (Exception e) {
			Log.e(TAG, "Error in onCreateView :" + e);
		}
		return rootView;
	}

	/**
	 * Initialze all fragments controls
	 *
	 * @param rootView
	 */
	private void initalizeControls(View rootView) {
		try {

			//Initialize the controls
			txtWebsite = (EditText) rootView.findViewById(R.id.txtWebsite);
			txtUserName = (EditText) rootView.findViewById(R.id.txtUserName);
			txtMemberID = (EditText) rootView.findViewById(R.id.txtMemberID);
			txtInterval = (EditText) rootView.findViewById(R.id.txtIntervale);
			txtLatituted = (TextView) rootView.findViewById(R.id.txtLatitude);
			txtLongtitued = (TextView) rootView.findViewById(R.id.txtLongitude);
			trackingButton = (Button) rootView.findViewById(R.id.trackingButton);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in initalizeControls :" + e);

		}
		catch (Exception e) {
			Log.e(TAG, "Error in initalizeControls :" + e);
		}
	}

	/**
	 * Initialize the Alarm server to given interval minutes
	 */
	private void startAlarmManager(Context context) {
		try {
			Log.d(TAG, "startAlarmManager");

			//Get the Alaram manager from alarm service context
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			//Create intent for GpsTrackerAlarmReceiver
			gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);

			//Create a pending intent to broadcast the gpsTrackerIntent
			pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

			//Get the interval minutes from preferences
			Preference preference = new Preference();

			intervalInMinutes = preference.getIntervalValue(context);

			//Set the repeating of alarm to iterval minutes to broad cast the pending intent
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
					intervalInMinutes * 60000, // 60000 = 1 minute
					pendingIntent);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in starting alarm manger : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in starting alarm manger : " + e);
		}
	}

	/**
	 * update the latitude and longitude
	 *
	 * @param lat
	 * @param lng
	 */
	public void updateLatLong(double lat, double lng) {
		try {
			txtLatituted.setText("" + lat);
			txtLongtitued.setText("" + lng);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error while updating LatLong : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error while updating LatLong : " + e);
		}
	}

	/**
	 * Display toast message to user from background task
	 *
	 * @param message
	 */
	public void showTostMessage(String message) {
		try {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in showTostMessage : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in showTostMessage : " + e);
		}
	}

	/**
	 * Cancel the alarm manger This will cancel all the pending intent
	 */
	private void cancelAlarmManager(Context context) {
		try {
			//Create the intent object for Gps Tracker Alarm Receiver
			Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);

			//Create a pending intent for gps tracker intent
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

			//Get the Alarm manager from system service
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			//Cancel the pending intent
			alarmManager.cancel(pendingIntent);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in cancel alarm manager : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in cancel alarm manager : " + e);
		}
	}

	/**
	 * Start the location service. This return to caller if user setting is not
	 * saved or Google Play service is not enable or not found in user device
	 *
	 * @param v
	 */
	public void trackLocation(Context context) {
		try {
			//Create the preferncec class object
			Preference preference = new Preference();

			//Enable gps if not enabled
			final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				buildAlertMessageNoGps();
				return;
			}

			//Check google play is enabled
			if (!checkIfGooglePlayEnabled()) {
				return;
			}

			// Check if tracking is already running
			if (currentlyTracking) {
				cancelAlarmManager(context);

				currentlyTracking = false;
				preference.storeCurrentlyTracking(false, context);
				preference.storeSessionID("", context);
			}
			else {

				//Start the alarm manger to broad cast the pending intent to user define interval
				startAlarmManager(context);

				currentlyTracking = true;
				preference.storeCurrentlyTracking(true, context);
				preference.storeDistance(0f, context);
				preference.storeFirstTimePosition(true, context);
				preference.storeSessionID(UUID.randomUUID().toString(), context);

			}

			//Change the track button state
			setTrackingButtonState();
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in tracklocation : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in tracklocation : " + e);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 0) {
			switch (requestCode) {
				case 100:
					trackLocation(getActivity().getApplicationContext());
					break;

				default:
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Your GPS seems to be disabled, please enable it?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
								100);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * Initialize the user setting ui from previous saved settings in the shared
	 * preferences
	 */
	private void displayUserSettings(Context context) {
		try {
			Preference preference = new Preference();
			//Set the text values
			txtWebsite.setText(preference.getLocationServiceURL(context));
			txtUserName.setText(preference.getMemberName(context));

			int memberID = preference.getMemberID(context);
			if (memberID == 0) {
				txtMemberID.setText("No Member ID Found");

			}
			else {

				txtMemberID.setText(Integer.toString(memberID));
			}
			intervalInMinutes = preference.getIntervalValue(context);
			txtInterval.setText(Integer.toString(intervalInMinutes));
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in displayUserSettings : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in displayUserSettings : " + e);
		}
	}

	/**
	 * Check if Google Play service in enabled or not
	 *
	 * @return
	 */
	private boolean checkIfGooglePlayEnabled() {
		try {
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS) {
				return true;
			}
			else {
				Log.e(TAG, "unable to connect to google play services.");
				Toast.makeText(getActivity().getApplicationContext(), R.string.google_play_services_unavailable,
						Toast.LENGTH_LONG).show();

			}
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in checkIfGooglePlayEnabled : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in checkIfGooglePlayEnabled : " + e);
		}
		return false;
	}

	@Override
	public void onResume() {
		try {
			super.onResume();

			displayUserSettings(getActivity().getApplicationContext());
			setTrackingButtonState();
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in onResume : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in onResume : " + e);
		}
	}

	/**
	 * Change the tracking button view state
	 */
	private void setTrackingButtonState() {
		try {
			if (currentlyTracking) {
				trackingButton.setBackgroundResource(R.drawable.green_tracking_button);
				trackingButton.setTextColor(Color.BLACK);
				trackingButton.setText(R.string.tracking_is_on);
			}
			else {
				trackingButton.setBackgroundResource(R.drawable.red_tracking_button);
				trackingButton.setTextColor(Color.WHITE);
				trackingButton.setText(R.string.tracking_is_off);
			}
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in setTrackingButtonState : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in setTrackingButtonState : " + e);
		}
	}
}