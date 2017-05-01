package com.lt.gpstracker.gcm;

public interface Config {

	// used to share GCM regId with application server - using php app server
//	static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

	// GCM server using java
	 static final String APP_SERVER_URL =
	 "http://192.168.1.101:8080/GCM_Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "583618093179";
	static final String MESSAGE_KEY = "message";

}
