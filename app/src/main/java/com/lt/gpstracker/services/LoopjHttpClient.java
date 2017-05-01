
package com.lt.gpstracker.services;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class LoopjHttpClient {
	private static final String		TAG		= "LoopjHttpClient";
	private static AsyncHttpClient	client	= new AsyncHttpClient();

	/**
	 * Get Data To Given Service UR
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		try {
			client.get(url, params, responseHandler);
		}
		catch (Exception e) {
			Log.e(TAG, "Error In GET : " + e);
		}
	}

	/**
	 * Post Data To Given Service URL
	 * 
	 * @param url
	 * @param requestParams
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
		try {
			client.post(url, requestParams, responseHandler);
		}
		catch (Exception e) {
			Log.e(TAG, "Error In POST : " + e);
		}
	}

	/**
	 * Construct the data headers
	 * 
	 * @param methodName
	 * @param url
	 * @param requestParams
	 * @param response
	 * @param headers
	 * @param statusCode
	 * @param throwable
	 */
	public static void debugLoopJ(String methodName, String url, RequestParams requestParams, byte[] response,
								  Header[] headers, int statusCode, Throwable throwable) {
		try {
			//Will encode url, if not disabled, and adds params on the end of it
			Log.d(TAG, AsyncHttpClient.getUrlWithQueryString(false, url, requestParams));

			if (headers != null) {
				Log.e(TAG, methodName);
				Log.d(TAG, "Return Headers:");
				for (@SuppressWarnings("deprecation")
				Header header : headers) {
					String _h = String.format(Locale.US, "%s : %s", header.getName(), header.getValue());
					Log.d(TAG, _h);
				}

				if (throwable != null) {
					Log.d(TAG, "Throwable:" + throwable);
				}

				Log.e(TAG, "StatusCode: " + statusCode);

				if (response != null) {
					Log.d(TAG, "Response: " + new String(response));
				}

			}
		}
		catch (Exception e) {
			Log.e(TAG, "Error in dbugLoopJ : " + e);
		}
	}
}
