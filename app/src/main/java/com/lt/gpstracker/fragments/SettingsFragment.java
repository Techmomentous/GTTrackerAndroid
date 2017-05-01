
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.lt.gpstracker.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.lt.gpstracker.gcm.ShareExternalServer;
import com.lt.gpstracker.utility.Preference;
import com.lt.gpstracker.utility.Utility;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class SettingsFragment extends Fragment {
	private static final String		TAG		= "SettingsFragment";
	private static EditText			txtUserName;
	private static EditText			txtMemberID;
	private static EditText			txtWebsiteIP;
	private static EditText			txtWebsitePort;
	private static EditText			txtGCMProjectID;
	private static EditText			txtRegID;
	private RadioGroup				intervalRadioGroup;
	private boolean					currentlyTracking;
	private Context					context;
	GoogleCloudMessaging			gcm;
	String							regId	= "";

	AsyncTask<Void, Void, String>	shareRegidTask;

	public SettingsFragment(Context context) {
		this.context = context;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		try {

			if (context == null) {
				context = getActivity().getApplicationContext();
			}
			//Before processing check device is connected to network
			checkforNetwork();

			//Enable display menu on action bar
			setHasOptionsMenu(true);

			initalizeControls(rootView);

			displaySettings();
			//Check tracking is currently on or off

			Preference preference = new Preference();

			currentlyTracking = preference.getCurrenltyTracking(context);

			//Check if the app is boot first time on devices
			boolean firstTimeLoadindApp = preference.getFirstTimeLoad(context);

			if (firstTimeLoadindApp) {
				//if app is first time installed then save the Unique ID in shared preferences
				preference.storeFirstTimeLoad(false, context);
				preference.storeSessionID(UUID.randomUUID().toString(), context);
			}

			intervalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup radioGroup, int i) {
					saveInterval(context);
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

	/**
	 * Submit the project id to google cloud and get the registration.
	 *
	 */
	private void registerdMemeberApp() {
		try {
			regId = txtRegID.getText().toString();
			if (TextUtils.isEmpty(regId)) {
				regId = registerGCM();
				txtRegID.setText(regId);

			}
			else {
				showMaterialDialog("You Already Registered.");
			}
		}
		catch (Exception e) {
			Log.e(TAG, "Error registerdMemeberApp : " + e.getMessage());
		}
	}

	/**
	 * Save the user settings
	 */
	private void saveSettings() {
		try {
			//if validation failed return
			if (!validateControls()) {
				return;
			}
			boolean isSaved = saveUserSettings(context);

			if (isSaved) {
				showMaterialDialog("Settings is Saved Successfully.");
			}
			else {
				showMaterialDialog("Unable To Saved Settings. Please Try Again Later.");
			}

		}
		catch (Exception e) {
			Log.e(TAG, "Error In Saved Button Click " + e);
		}
	}

	/**
	 * Start Registration Process in Background Thread.
	 *
	 * @return
	 */
	public String registerGCM() {
		try {
			Preference preference = new Preference();
			//Get the new instance of GCM
			gcm = GoogleCloudMessaging.getInstance(getActivity());

			//Get The Registration id if already registered
			String regId = preference.getRegistrationId(getActivity());

			//Registration id is empty begin the Registration process
			if (TextUtils.isEmpty(regId)) {

				registerInBackground();

				Log.d(TAG, "Registration Finished " + regId);
			}

		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while Getting Regisration ID  :" + e);
		}
		catch (Exception e) {
			Log.d(TAG, "Error while Getting Regisration ID  :" + e);
		}
		return regId;
	}

	/**
	 * Start Async Task in Background Thread to Fetch The Registration ID From
	 * GCM
	 */
	private void registerInBackground() {
		checkforNetwork();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {

					//if GCM Object is null re initialize it
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}

					regId = gcm.register(txtGCMProjectID.getText().toString());
					Log.d(TAG, "Got Registration ID :" + regId);
					msg = "Device registered successfully.";

					Preference preference = new Preference();
					preference.storeRegistrationId(regId, getActivity());
				}
				catch (IOException e) {
					msg = "Failed To Registered Device Please Check The Netowrk Connectivity.\n Also Check Project ID is correct";
					Log.d(TAG, "Error While Fetching Registration ID From GCM : " + e);
				}
				catch (Exception e) {
					msg = "Failed To Registered Device Please Check The Netowrk Connectivity.\n Also Check Project ID is correct";
					Log.d(TAG, "Error While Fetching Registration ID From GCM : " + e);
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				txtRegID.setText(regId);
				showMaterialDialog(msg);
			}
		}.execute(null, null, null);
	}

	/**
	 *
	 */
	private void syncWithGCMServer() {
		try {
			checkforNetwork();
			//Get the registration id and member id
			String memberID = txtMemberID.getText().toString();
			String memberName = txtUserName.getText().toString();
			regId = txtRegID.getText().toString();

			if (!TextUtils.isEmpty(regId) && !TextUtils.isEmpty(memberID) && !TextUtils.isEmpty(memberName)) {
				Preference preference = new Preference();
				final String gcmServiceURL = preference.getGCMServiceURL(getActivity());

				//Construct the json
				//Construct The JSON
				final JSONObject memberJSON = new JSONObject();
				memberJSON.put("memberID", Integer.valueOf(memberID));
				memberJSON.put("memberAppId", regId);
				memberJSON.put("memberName", memberName);

				final ShareExternalServer appUtil = new ShareExternalServer();
				//Get the app server url


				shareRegidTask = new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {

						String result = appUtil.syncWithAppServer(context, memberJSON.toString(), gcmServiceURL);
						return result;
					}

					@Override
					protected void onPostExecute(String result) {
						shareRegidTask = null;
						//Show toast on post execute
						showMaterialDialog(result);
					}

				};
				shareRegidTask.execute(null, null, null);

			}
			else {
				showMaterialDialog(getString(R.string.gcm_sync_field_empty_message));
			}
		}
		catch (NullPointerException e) {
			Log.d(TAG, "Error while syncWithGCMServer  :" + e);
		}
		catch (Exception e) {
			Log.d(TAG, "Error while syncWithGCMServer  :" + e);
		}
	}

	/**
	 * Validate controls
	 */
	private boolean validateControls() {
		boolean result = true;
		String port = txtWebsitePort.getText().toString().trim();
		String ip = txtWebsiteIP.getText().toString().trim();
		String projectID = txtGCMProjectID.getText().toString();
		String memberID = txtMemberID.getText().toString();
		String username = txtUserName.getText().toString();

		if (TextUtils.isEmpty(port)) {
			txtWebsitePort.setError("Required!!");
			result = false;
		}
		if (TextUtils.isEmpty(ip)) {
			txtWebsiteIP.setError("Required!!");
			result = false;
		}

		if (TextUtils.isEmpty(projectID)) {
			txtGCMProjectID.setError("Required!!");
			result = false;
		}
		if (TextUtils.isEmpty(memberID)) {
			txtMemberID.setError("Required!!");
			result = false;
		}
		if (TextUtils.isEmpty(username)) {
			txtUserName.setError("Required!!");
			result = false;
		}
		return result;

	}

	/**
	 * Save the interval settings in preferences
	 */
	private void saveInterval(Context context) {
		try {

			//if tracking is already running ask user to restart the tracking
			if (currentlyTracking) {
				showMaterialDialog(getString(R.string.user_needs_to_restart_tracking));
			}
			Preference preference = new Preference();

			switch (intervalRadioGroup.getCheckedRadioButtonId()) {
				case R.id.i1:
					preference.storeInterval(1, context);
					break;
				case R.id.i5:
					preference.storeInterval(5, context);
					break;
				case R.id.i15:
					preference.storeInterval(15, context);
					break;
			}

		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in saving interval : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in saving interval : " + e);
		}
	}

	/**
	 * Initialize all fragments controls
	 *
	 * @param rootView
	 */
	private void initalizeControls(View rootView) {
		try {

			//Initialize the controls
			txtWebsiteIP = (EditText) rootView.findViewById(R.id.txtWebsite);
			txtWebsitePort = (EditText) rootView.findViewById(R.id.txtPort);
			txtUserName = (EditText) rootView.findViewById(R.id.txtUserName);
			txtUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
			txtMemberID = (EditText) rootView.findViewById(R.id.txtMemberID);
			txtGCMProjectID = (EditText) rootView.findViewById(R.id.txtProjectID);
			txtRegID = (EditText) rootView.findViewById(R.id.txtRegID);
			intervalRadioGroup = (RadioGroup) rootView.findViewById(R.id.intervalRadioGroup);
			txtUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in initalizeControls :" + e);

		}
		catch (Exception e) {
			Log.e(TAG, "Error in initalizeControls :" + e);
		}
	}

	/**
	 * Display the previous saved settings
	 */
	private void displaySettings() {
		try {
			Preference preference = new Preference();
			txtWebsiteIP.setText(preference.getWebAPPIP(getActivity()));
			txtWebsitePort.setText(preference.getWebAPPPORT(getActivity()));
			txtUserName.setText(preference.getMemberName(getActivity()));
			txtMemberID.setText(Integer.toString(preference.getMemberID(getActivity())));
			txtGCMProjectID.setText(preference.getProjectID(getActivity()));
			txtRegID.setText(preference.getRegistrationId(getActivity()));
			int intervalValue = preference.getIntervalValue(getActivity());
			switch (intervalValue) {
				case 1:
					intervalRadioGroup.check(R.id.i1);
					break;
				case 5:
					intervalRadioGroup.check(R.id.i5);
					break;
				case 15:
					intervalRadioGroup.check(R.id.i15);
					break;
				default:
					break;
			}
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in displaySettings :" + e);

		}
		catch (Exception e) {
			Log.e(TAG, "Error in displaySettings :" + e);
		}
	}

	/**
	 * Save the user preference. <br>
	 * Following preferense will saved
	 * <ul>
	 * <li>Member Name</li>
	 * <li>Member ID</li>
	 * <li>Google Project ID</li>
	 * <li>Registration ID Obtain From GCM</li>
	 * <li>GCM Server Web APP IP Address</li>
	 * <li>GCM Server Web APP Port Number</li>
	 * <li>Location Service URL</li>
	 * <li>GCM Service URL</li>
	 * </ul>
	 * <p>
	 * The location service url and the gcm service url <br>
	 * method name and servalte name are obtain from the string.xml
	 * <p>
	 *
	 * @return
	 */
	private boolean saveUserSettings(Context context) {
		try {
			Preference preference = new Preference();
			if (textFieldsAreEmptyOrHaveSpaces()) {
				return false;
			}

			switch (intervalRadioGroup.getCheckedRadioButtonId()) {
				case R.id.i1:
					preference.storeInterval(1, context);
					break;
				case R.id.i5:
					preference.storeInterval(5, context);
					break;
				case R.id.i15:
					preference.storeInterval(15, context);
					break;
			}

			String ipAddress = txtWebsiteIP.getText().toString().trim();
			String port = txtWebsitePort.getText().toString().trim();

			String locationServiceURL = "http://" + ipAddress + ":" + port + getString(R.string.locationServiceURL);
			String gcmServiceURL = "http://" + ipAddress + ":" + port + getString(R.string.gcmServiceURL);
			String messageServiceURL = "http://" + ipAddress + ":" + port + getString(R.string.messageServiceURL);
			Toast.makeText(context, "Service URL " + locationServiceURL, Toast.LENGTH_LONG).show();

			Log.i(TAG, "Constructed URL : " + locationServiceURL);

			int memberid = Integer.valueOf(txtMemberID.getText().toString().trim());
			preference.storeMemberDetails(txtUserName.getText().toString().trim(), memberid, context);
			preference.storeLocationServiceURL(locationServiceURL, context);
			preference.storeGCMServiceURL(gcmServiceURL, context);
			preference.storeMessageServiceURL(messageServiceURL, context);
			preference.storeWebAPPPORT(txtWebsitePort.getText().toString().trim(), context);
			preference.storeWebAPPIP(txtWebsiteIP.getText().toString().trim(), context);
			preference.storeProjectID(txtGCMProjectID.getText().toString().trim(), context);
			preference.storeRegistrationId(txtRegID.getText().toString().trim(), context);

			return true;
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in saveUserSetting : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in saveUserSetting : " + e);
		}
		return false;
	}

	/**
	 * Validate if the text fields are empty
	 *
	 * @return
	 */
	private boolean textFieldsAreEmptyOrHaveSpaces() {
		try {
			String tempWebsite = txtWebsiteIP.getText().toString().trim();

			if (tempWebsite.length() == 0 || hasSpaces(tempWebsite)) {
				Toast.makeText(getActivity().getApplicationContext(), R.string.textfields_empty_or_spaces,
						Toast.LENGTH_LONG).show();
				return true;
			}
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error  : " + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error  : " + e);
		}
		return false;
	}

	/**
	 * Check for space in the specified string
	 *
	 * @param str
	 * @return
	 */
	private boolean hasSpaces(String str) {
		return ((str.split(" ").length > 1) ? true : false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.setting_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
			case R.id.sync:
				syncWithGCMServer();
				return true;
			case R.id.save:
				saveSettings();
				return true;
			case R.id.registerd:
				registerdMemeberApp();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Display Material Dialog Widget Box
	 *
	 * @param title
	 * @param message
	 */
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
}
