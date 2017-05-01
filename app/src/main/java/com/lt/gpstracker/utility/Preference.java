
package com.lt.gpstracker.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.lt.gpstracker.activity.TrackerMainActivity;

public class Preference {
	private static final String	APP_VERSION			= "appVersion";
	private static final String	CURRENTLY_TRACKING	= "currentlyTracking";
	private static final String	DISTANCE			= "totalDistanceInMeters";
	private static final String	FIRST_TIME_POSITION	= "firstTimeGettingPosition";
	private static final String	FIRT_TIME_LOAD		= "firstTimeLoadindApp";
	private static final String	INTERVAL_MINUTES	= "intervalInMinutes";
	private static final String	LOCATION_SERVICE	= "defaultUploadWebsite";
	private static final String	GCM_SERVICE			= "gcm_service";
	private static final String	MESSAGE_SERVICE		= "message_service";
	private static final String	MEMBER_ID			= "memberID";
	private static final String	MEMBER_NAME			= "memberName";
	private static final String	PROJECT_ID			= "projectID";
	public static final String	REG_ID				= "regId";
	private static final String	SESSION_ID			= "sessionID";
	private static final String	NOTIFICATIONS		= "notification";

	private static final String	PREV_LATITUDE		= "previousLatitude";
	private static final String	PREV_LONGITUDE		= "previousLongitude";
	private static final String	WEBAPP_IP			= "ipaddress";
	private static final String	WEBAPP_PORT			= "port";
	private static final String	TAG					= "Preference";
	private int					currentVersion		= 0;
	private String				projectID			= "";
	private int					registeredVersion	= 0;
	private String				registrationId		= "";

	/**
	 * Return the main activity context
	 * 
	 * @return
	 */
	private static Context getContext() {
		return TrackerMainActivity.getInstance();
	}

	public Preference() {
	}

	/**
	 * Get the application version using package info api
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersion(Context context) {
		int versionCode = 0;
		try {
			if (context == null) {
				context = getContext();
			}
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		}
		catch (NameNotFoundException e) {
			Log.d(TAG, "Error while Getting App Version :" + e.getMessage());
		}
		catch (RuntimeException e) {
			Log.d(TAG, "Error while Getting App Version :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting App Version :" + e.getMessage());
		}
		return versionCode;
	}

	/**
	 * Return the currently tracking value from shared prefernces
	 * 
	 * @param context
	 * @return
	 */
	public boolean getCurrenltyTracking(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getBoolean(CURRENTLY_TRACKING, false);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting currently Tracking  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting currently Tracking  :" + e.getMessage());
		}
		return false;
	}

	/**
	 * Return the distance value from shared prefernces
	 * 
	 * @param context
	 * @return
	 */
	public float getDistance(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			prefs.getFloat(DISTANCE, 0f);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting distance  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting distance  :" + e.getMessage());
		}
		return 0f;
	}

	/**
	 * Return the First Time App Loading value from shared prefernces
	 * 
	 * @param context
	 * @return
	 */
	public boolean getFirstTimeLoad(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getBoolean(FIRT_TIME_LOAD, true);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting First Time App Loading  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting  First Time App Loading :" + e.getMessage());
		}
		return false;
	}

	/**
	 * Return the First Time Position value from shared preferences
	 * 
	 * @param context
	 * @return
	 */
	public boolean getFirstTimePosition(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getBoolean(FIRST_TIME_POSITION, false);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting First Time Position   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting First Time Position   :" + e.getMessage());
		}
		return false;
	}

	/**
	 * Return the saved interval value from shared pereferences
	 * 
	 * @param context
	 * @return
	 */
	public int getIntervalValue(Context context) {
		try {

			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getInt(INTERVAL_MINUTES, 1);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Interval Value   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Interval Value   :" + e.getMessage());
		}
		return 1;
	}

	/**
	 * Return The Location Service URLhared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public String getLocationServiceURL(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Put the values in editor and commit the changes
			return prefs.getString(LOCATION_SERVICE, "");

		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Returning Location Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Returning Location Service URL:" + e.getMessage());
		}
		return "";
	}

	/**
	 * Get the Member ID
	 * 
	 * @param context
	 * @return
	 */
	public int getMemberID(Context context) {
		int memberID = 0;
		try {
			if (context == null) {
				context = getContext();
			}
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Get the Registration id by key
			memberID = prefs.getInt(MEMBER_ID, 0);

			//if not found return empty
			if (memberID == 0) {
				Log.i(TAG, "Member ID not found.");
				return 0;
			}
		}
		catch (RuntimeException e) {
			Log.d(TAG, "Error while Getting Member ID:" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Member ID :" + e.getMessage());
		}
		return memberID;
	}

	/**
	 * Get the Member ID
	 * 
	 * @param context
	 * @return
	 */
	public String getMemberName(Context context) {
		String memberName = "";
		try {
			if (context == null) {
				context = getContext();
			}
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Get the Registration id by key
			memberName = prefs.getString(MEMBER_NAME, "");

			//if not found return empty
			if (memberName.isEmpty()) {
				Log.i(TAG, "Member Name not found.");
				return "";
			}
		}
		catch (RuntimeException e) {
			Log.d(TAG, "Error while Getting Member Name:" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Member Name :" + e.getMessage());
		}
		return memberName;
	}

	/**
	 * Get the GCM Project ID
	 * 
	 * @param context
	 * @return
	 */
	public String getProjectID(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Initialize the shared preferences
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Get the Registration id by key
			projectID = prefs.getString(PROJECT_ID, "");

			//if not found return empty
			if (projectID.isEmpty()) {
				Log.i(TAG, "Project ID not found.");
				return "";
			}
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in getProjectID :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in getProjectID :" + e.getMessage());
		}
		return projectID;
	}

	/**
	 * Store the GCM Project ID
	 * 
	 * @param context
	 * @return
	 */
	public void storeProjectID(String value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Initialize the shared preferences
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putString(PROJECT_ID, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in storeProjectID :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in storeProjectID :" + e.getMessage());
		}
	}

	/**
	 * Get the Registration id from shared preferences. Also Check if app
	 * version is change in that case user have to re-regester the app
	 * 
	 * @param context
	 * @return
	 */
	public String getRegistrationId(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Initialize the shared preferences
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Get the Registration id by key
			registrationId = prefs.getString(REG_ID, "");

			//if not found return empty
			if (registrationId.isEmpty()) {
				Log.i(TAG, "Registration not found.");
				return "";
			}
			//Get the app version.
			registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);

			//Get the current version
			currentVersion = getAppVersion(context);

			//Check if app version is change
			if (registeredVersion != currentVersion) {
				Log.i(TAG, "App version changed.");
				return "";
			}
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error in getRegistrationId :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error in getRegistrationId :" + e.getMessage());
		}
		return registrationId;
	}

	/**
	 * Return the Session ID from Shared Preferences
	 * 
	 * @param context
	 * @return
	 */
	public String getSessionID(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getString(SESSION_ID, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Session ID   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Session ID   :" + e.getMessage());
		}
		return "";
	}

	/**
	 * Store the currently Tracking value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeCurrentlyTracking(boolean value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putBoolean(CURRENTLY_TRACKING, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing currently Tracking  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing currently Tracking  :" + e.getMessage());
		}
	}

	/**
	 * Store the distance value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeDistance(float value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putFloat(DISTANCE, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing distance  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing distance  :" + e.getMessage());
		}
	}

	/**
	 * Store the first time app loaded value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeFirstTimeLoad(boolean value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putBoolean(FIRT_TIME_LOAD, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing First Time App Loading  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing First Time App Loading  :" + e.getMessage());
		}
	}

	/**
	 * Store the First Time Position value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeFirstTimePosition(boolean value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putBoolean(FIRST_TIME_POSITION, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing First Time Position   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing First Time Position   :" + e.getMessage());
		}
	}

	/**
	 * Store the Interval Value value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeInterval(int value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putInt(INTERVAL_MINUTES, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Interval Value   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Interval Value   :" + e.getMessage());
		}
	}

	/**
	 * Store The Location Service URL in shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public void storeLocationServiceURL(String url, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Open The Editor
			SharedPreferences.Editor editor = prefs.edit();

			//Put the values in editor and commit the changes
			editor.putString(LOCATION_SERVICE, url);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Location Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Location Service URL:" + e.getMessage());
		}
	}

	/**
	 * Store The GCM Registration Service URL in shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public void storeGCMServiceURL(String url, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Open The Editor
			SharedPreferences.Editor editor = prefs.edit();

			//Put the values in editor and commit the changes
			editor.putString(GCM_SERVICE, url);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing GCM Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing GCM Service URL:" + e.getMessage());
		}
	}

	/**
	 * Return the GCM Registration Service url fron shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public String getGCMServiceURL(Context context) {
		String url = "";
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
			url = prefs.getString(GCM_SERVICE, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting GCM Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting GCM Service URL:" + e.getMessage());
		}
		return url;
	}
	/**
	 * Store The Message Service URL in shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public void storeMessageServiceURL(String url, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Open The Editor
			SharedPreferences.Editor editor = prefs.edit();

			//Put the values in editor and commit the changes
			editor.putString(MESSAGE_SERVICE, url);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Message Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Message Service URL:" + e.getMessage());
		}
	}
	
	/**
	 * Return the Message Service url from shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public String getMessageServiceURL(Context context) {
		String url = "";
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
			url = prefs.getString(MESSAGE_SERVICE, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Message Service URL :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Message Service URL:" + e.getMessage());
		}
		return url;
	}
	/**
	 * Store The Member details in shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public void storeMemberDetails(String memberName, int memberID, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Open The Editor
			SharedPreferences.Editor editor = prefs.edit();

			//Put the values in editor and commit the changes
			editor.putInt(MEMBER_ID, memberID);
			editor.putString(MEMBER_NAME, memberName);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Member Details :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Member Details  :" + e.getMessage());
		}
	}

	/**
	 * Store the Interval Value value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeMemberID(int value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putInt(MEMBER_ID, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Interval Value   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Interval Value   :" + e.getMessage());
		}
	}

	/**
	 * Store The Registration id and app version in shared preferences
	 * 
	 * @param context
	 * @param regId
	 */
	public void storeRegistrationId(String regId, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			//Get the app version
			int appVersion = getAppVersion(context);
			String appServerURL = getLocationServiceURL(context);
			String gcmProjectID = getProjectID(context);
			//Open The Editor
			SharedPreferences.Editor editor = prefs.edit();

			//Put the values in editor and commit the changes
			editor.putString(REG_ID, regId);
			editor.putInt(APP_VERSION, appVersion);
			editor.putString(LOCATION_SERVICE, appServerURL);
			editor.putString(PROJECT_ID, gcmProjectID);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Regisration ID  :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Regisration ID  :" + e.getMessage());
		}
	}

	/**
	 * Store the Session ID value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeSessionID(String value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putString(SESSION_ID, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Session ID   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Session ID   :" + e.getMessage());
		}
	}

	/**
	 * Store the previous latitude value
	 * 
	 * @param value
	 * @param context
	 */
	public void storePreviousLatitutde(float value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putFloat(PREV_LATITUDE, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Previous Latituted   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Previous Latituted :" + e.getMessage());
		}
	}

	/**
	 * Return the previous stored latitude value
	 * 
	 * @param context
	 * @return
	 */
	public float getPreviousLatitude(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
			return prefs.getFloat(PREV_LATITUDE, 0f);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Previous Latitude   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Previous Latitude :" + e.getMessage());
		}
		return 0f;
	}

	/**
	 * Store the previous longitude value
	 * 
	 * @param value
	 * @param context
	 */
	public void storePreviousLongitude(float value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putFloat(PREV_LONGITUDE, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Previous Longitude   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Previous Longitude :" + e.getMessage());
		}
	}

	/**
	 * Return the previous stored longitude value
	 * 
	 * @param context
	 * @return
	 */
	public float getPreviousLongitude(Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			return prefs.getFloat(PREV_LONGITUDE, 0f);
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Previous Longitude   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Previous Longitude :" + e.getMessage());
		}
		return 0f;
	}

	/**
	 * Store the Web App Ip Address value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeWebAPPIP(String value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putString(WEBAPP_IP, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Web App Ip Address   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Web App Ip Address :" + e.getMessage());
		}
	}

	/**
	 * Return the Web App Ip Address value
	 * 
	 * @param value
	 * @param context
	 */
	public String getWebAPPIP(Context context) {
		String ip = "";
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			ip = prefs.getString(WEBAPP_IP, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while getting Web App Ip Address   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while getting Web App Ip Address :" + e.getMessage());
		}
		return ip;
	}

	/**
	 * Store the Web App Port value
	 * 
	 * @param value
	 * @param context
	 */
	public void storeWebAPPPORT(String value, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();

			editor.putString(WEBAPP_PORT, value);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing Web App Port   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing Web App Port :" + e.getMessage());
		}
	}

	/**
	 * Store the Web App Port value
	 * 
	 * @param value
	 * @param context
	 */
	public String getWebAPPPORT(Context context) {
		String port = "";
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);

			port = prefs.getString(WEBAPP_PORT, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Web App Port   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting" + " Web App Port :" + e.getMessage());
		}
		return port;
	}

	/**
	 * Store the notification json message details. This will overwrite previous
	 * message
	 * 
	 * @param messageCode
	 * @param message
	 * @param date
	 * @param context
	 */
	public void storeNotification(String notificationJSON, Context context) {
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(NOTIFICATIONS, notificationJSON);
			editor.commit();
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing  Notification   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing  Notification:" + e.getMessage());
		}
	}

	/**
	 * Return the notification json
	 * 
	 * @param context
	 * @return
	 */
	public String getNotificationJSON(Context context) {
		String json = "";
		try {
			if (context == null) {
				context = getContext();
			}
			//Get the preferences object
			final SharedPreferences prefs = context.getSharedPreferences(TrackerMainActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
			json = prefs.getString(NOTIFICATIONS, "");
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Storing  Notification   :" + e.getMessage());
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Storing  Notification:" + e.getMessage());
		}
		return json;
	}
}
