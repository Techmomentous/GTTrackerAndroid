
package com.lt.gpstracker.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lt.gpstracker.R;
import com.lt.gpstracker.gcm.ShareExternalServer;
import com.lt.gpstracker.utility.Constant;
import com.lt.gpstracker.utility.Preference;
import com.lt.gpstracker.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MessagingFragment extends Fragment {
	private static final String		TAG		= "MessagingFragment";
	private Button btnSend	= null;
	private EditText				message	= null;
	private Spinner					spinner	= null;
	AsyncTask<Void, Void, String>	asyncTask;
	private Context					context;

	public MessagingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_messaging, container, false);
		try {
			if (context == null) {
				context = getActivity().getApplicationContext();
			}
			//Before processing check device is connected to network
			checkforNetwork();

			spinner = (Spinner) rootView.findViewById(R.id.messageTypeSpinner);
			message = (EditText) rootView.findViewById(R.id.messageText);
			btnSend = (Button) rootView.findViewById(R.id.button);
			btnSend.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					checkforNetwork();
					sendData();
				}
			});
		}
		catch (NullPointerException e) {
			Log.e(TAG, "ERROR : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "ERROR : " + e);
		}
		return rootView;
	}

	/**
	 * Submit The Data
	 */
	private void sendData() {
		try {
			String messageType = spinner.getSelectedItem().toString();
			String msg = message.getText().toString();

			if (!TextUtils.isEmpty(messageType) && !TextUtils.isEmpty(msg)) {
				String messageCode="";
				//MessageTypeID
				if (messageType.equalsIgnoreCase("It`s Urgent")) {
					messageCode=Constant.MESSAGE_CODE_101;
				}
				else if (messageType.equalsIgnoreCase("Call Notice")) {
					messageCode=Constant.MESSAGE_CODE_102;
				}
				else if (messageType.equalsIgnoreCase("Request")) {
					messageCode=Constant.MESSAGE_CODE_103;
				}
				else if (messageType.equalsIgnoreCase("ASAP")) {
					messageCode=Constant.MESSAGE_CODE_104;
				}


				Preference preference = new Preference();
				final String messageServiceURL = preference.getMessageServiceURL(getActivity());
				int memberID = preference.getMemberID(getActivity());
				String memberName = preference.getMemberName(getActivity());

				final JSONObject memberJSON = new JSONObject();
				memberJSON.put("memberID", Integer.valueOf(memberID));
				memberJSON.put("memberName", memberName);
				memberJSON.put("messageType", messageCode);
				memberJSON.put("message", msg);
				//Get the current date and time

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
				String formattedTime = sdf.format(now);
				Calendar calendar = Calendar.getInstance();
				int date = calendar.get(Calendar.DATE);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);


				String time=formattedTime + " : " + date + " : " + month + " : " + year;
				memberJSON.put("time", time);

				final ShareExternalServer appUtil = new ShareExternalServer();
				asyncTask = new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						String result = appUtil.sendMessage(getActivity(), memberJSON.toString(), messageServiceURL);
						return result;
					}

					@Override
					protected void onPostExecute(String result) {
						asyncTask = null;
						//Show toast on post execute
						showMaterialDialog(result);
					}

				};
				asyncTask.execute(null, null, null);
			}
			else {

				message.setError("Required!");
				showMaterialDialog("Please Provide Required Details");
			}
		}
		catch (JSONException e) {
			Log.e(TAG, "ERROR : " + e);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "ERROR : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "ERROR : " + e);
		}
	}


	private void showMaterialDialog(String message) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();

			// Setting Dialog Title
			alertDialog.setTitle( getString(R.string.app_name));

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Setting alert dialog icon
			//alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener
					() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});


			// Showing Alert Message
			alertDialog.show();

		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in showMaterialDialog : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error  in showMaterialDialog: " + e);
		}
	}

	/**
	 * Check device is connected or not.
	 */
	private void checkforNetwork() {
		if (!Utility.isConnectedToInternet(context)) {
			try {
				Utility.ShowNetworkRequiredDialog(context);

			}
			catch (Exception e) {
				Log.e(TAG, "Error registrationButton : " + e.getMessage());
			}
		}
	}
}
