package com.MWC.chatapp;

public interface GCMConfig {

	
	// CONSTANTS
	static final String YOUR_SERVER_URL =  "http://millionairesorg.com/push/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "279531456357";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.MWC.chatapp.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
}
