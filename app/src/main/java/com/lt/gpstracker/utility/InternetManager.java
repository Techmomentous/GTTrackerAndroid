
package com.lt.gpstracker.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InternetManager {

	private static final String	TAG					= "InternetManager";
	public static int			TYPE_WIFI			= 1;
	public static int			TYPE_MOBILE			= 2;
	public static int			TYPE_NOT_CONNECTED	= 0;
	public static final int		NETWORK_STATUS_NOT_CONNECTED	= 0, NETWORK_STAUS_WIFI = 1, NETWORK_STATUS_MOBILE = 2;

	/**
	 * This method enable mobile data connection.
	 * 
	 * @param context
	 */
	public static void EnableInternet(Context context) {

		try {

			setMobileDataEnabled(context, true);
		}
		catch (NoSuchFieldException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (ClassNotFoundException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (IllegalAccessException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (NoSuchMethodException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (InvocationTargetException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
	}

	/**
	 * This method disable mobile data connection.
	 * 
	 * @param context
	 */
	public static void DisableInternet(Context context) {

		try {

			setMobileDataEnabled(context, false);
		}
		catch (NoSuchFieldException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (ClassNotFoundException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (IllegalAccessException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (NoSuchMethodException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
		catch (InvocationTargetException e) {
			Log.e(TAG, "Error occurred while enabling Internet " + e.getMessage());
		}
	}

	/**
	 * This handler will manager enable/disable operation for mobile data.
	 * 
	 * @param context
	 * @param enabled
	 * @throws NoSuchFieldException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	private static void setMobileDataEnabled(Context context, boolean enabled) throws NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {

		final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final Class<?> conmanClass = Class.forName(conman.getClass().getName());

		final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",
				Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	/**
	 * Return the connectivity status
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectivityStatus(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			if (null != activeNetwork) {
				if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
					return TYPE_WIFI;

				if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
					return TYPE_MOBILE;
			}
		}
		catch (Exception e) {
			Log.e(TAG, "Error : " + e);
		}
		return TYPE_NOT_CONNECTED;
	}

	/**
	 * Return the connectivity status string
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectivityStatusString(Context context) {
		int conn = getConnectivityStatus(context);
		int status = 0;
		if (conn == TYPE_WIFI) {
			status = NETWORK_STAUS_WIFI;
		}
		else if (conn == TYPE_MOBILE) {
			status = NETWORK_STATUS_MOBILE;
		}
		else if (conn == TYPE_NOT_CONNECTED) {
			status = NETWORK_STATUS_NOT_CONNECTED;
		}
		return status;
	}

}
