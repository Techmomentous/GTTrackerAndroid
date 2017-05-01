
package com.lt.gpstracker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.gpstracker.R;
import com.lt.gpstracker.utility.Constant;

public class NotificationActivity extends Activity {
	private static final String	TAG			= "NotificationActivity";
	private ImageView			messageIcon	= null;
	private TextView			messageTxt	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.x = -20;
			params.height = 50;
			params.width = 550;
			params.y = -10;

			this.getWindow().setAttributes(params);
			setContentView(R.layout.activity_notification);

			initalizeView();
			// Create Notification Manager
			NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// Dismiss Notification
			notificationmanager.cancel(0);

			// Retrieve the data from MainActivity.java
			Intent intent = getIntent();

			String messageCode = intent.getStringExtra("messageCode");
			String messageText = intent.getStringExtra("messageText");
			String messageDate = intent.getStringExtra("messageDate");
			String currentTime = intent.getStringExtra("currentTime");

			//Set the icon
			setIcon(messageCode);
			messageTxt.setText(messageText + " \n Date :" + messageDate + " \n Time " + currentTime);

		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error :" + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error :" + e);
		}
	}

	private void initalizeView() {
		try {
			messageIcon = (ImageView) findViewById(R.id.messageIcon);
			messageTxt = (TextView) findViewById(R.id.txt_gcm_message);
		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in initalizeView:" + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in initalizeView:" + e);
		}
	}

	@SuppressLint("NewApi")
	private void setIcon(String messageCode) {
		try {
			if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_101)) {
				messageIcon.setBackground(getResources().getDrawable(R.drawable.message101));
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_102)) {
				messageIcon.setBackground(getResources().getDrawable(R.drawable.message102));
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_103)) {
				messageIcon.setBackground(getResources().getDrawable(R.drawable.message103));
			}
			else if (messageCode.equalsIgnoreCase(Constant.MESSAGE_CODE_104)) {
				messageIcon.setBackground(getResources().getDrawable(R.drawable.message104));
			}

		}
		catch (NullPointerException e) {
			Log.e(TAG, "Error in setIcon:" + e);
		}
		catch (Exception e) {
			Log.e(TAG, "Error in setIcon:" + e);
		}
	}
}
