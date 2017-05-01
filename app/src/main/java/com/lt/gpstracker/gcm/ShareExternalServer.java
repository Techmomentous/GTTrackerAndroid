
package com.lt.gpstracker.gcm;

import android.content.Context;
import android.util.Log;

import com.lt.gpstracker.R;
import com.lt.gpstracker.utility.Constant;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ShareExternalServer {
	private static final String	TAG	= "ShareExternalServer";
	private String	responseMessage;

	/**Sync with GCM APP Server
	 * @param context
	 * @param jsonData
	 * @param AppServerURL
	 * @return
	 */
	public String syncWithAppServer(final Context context, final String jsonData, final String AppServerURL) {

		String result = "";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("request", Constant.DEVICE_REGISTRATION_REQUEST);
		paramsMap.put("jsonData", jsonData);
		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(AppServerURL);
			}
			catch (MalformedURLException e) {
				Log.e(TAG, "URL Connection Error: " + AppServerURL, e);
				result = "Invalid URL: " + AppServerURL;
			}

			StringBuilder postBody = new StringBuilder();
			Iterator<Entry<String, String>> iterator = paramsMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				postBody.append(param.getKey()).append('=').append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverUrl.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setUseCaches(false);
				httpCon.setConnectTimeout(60000); //1 Minute
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				responseMessage = httpCon.getResponseMessage();
				if (status == 200) {
					result = context.getString(R.string.gcm_registration_success_message) + "\n " + responseMessage;
					//OK

				}
				else {
					result = responseMessage;
				}
			}
			finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
			}

		}
		catch (IOException e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		catch (NullPointerException e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		catch (Exception e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		return result;
	}

	/**Send The Message To APP Server
	 * @param context
	 * @param jsonData
	 * @param AppServerURL
	 * @return
	 */
	public String sendMessage(final Context context, final String jsonData, final String AppServerURL) {

		String result = "";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("request", Constant.ADD_NOTIFICATION_REQUEST);
		paramsMap.put("jsonData", jsonData);
		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(AppServerURL);
			}
			catch (MalformedURLException e) {
				Log.e(TAG, "URL Connection Error: " + AppServerURL, e);
				result = "Invalid URL: " + AppServerURL;
			}

			StringBuilder postBody = new StringBuilder();
			Iterator<Entry<String, String>> iterator = paramsMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				postBody.append(param.getKey()).append('=').append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverUrl.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setUseCaches(false);
				httpCon.setConnectTimeout(60000); //1 Minute
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				responseMessage = httpCon.getResponseMessage();
				if (status == 200) {
					result = responseMessage;
					//OK

				}
				else {
					result = responseMessage;
				}
			}
			finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
			}

		}
		catch (IOException e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		catch (NullPointerException e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		catch (Exception e) {
			result =context.getString(R.string.gcm_registration_failur_message) + "\n Please Check the IP & Port";
			Log.e(TAG, "Error in sharing with App Server: " + e);
		}
		return result;
	}
}
