package com.bookstore.app.utils;

public class CommonConstraints {
	public static final int NO_EXCEPTION = -1;
	public static final int GENERAL_EXCEPTION = 0;
	public static final int CLIENT_PROTOCOL_EXCEPTION = 1;
	public static final int ILLEGAL_STATE_EXCEPTION = 2;
	public static final int UNSUPPORTED_ENCODING_EXCEPTION = 3;
	public static final int IO_EXCEPTION = 4;
	public final static String EncodingCode = "UTF8";

	// Use for http Header content-type
	public final static String CONTENT_TYPE_CSV = "text/csv";
	public final static String CONTENT_TYPE_APPLICATION_XML = "application/xml";
	public final static String CONTENT_TYPE_TEXT_XML = "text/xml";
	public final static String TAG = "bookStore";

	// Shared Prefs

	public final static String USER_LOGIN_STATUS = "login_status";
	public final static String USER_USERNAME = "registered_user_name";
	public final static String USER_PASSWORD = "registered_user_password";
	public final static String USER_MOBILE_NUMBER = "registered_user_mobilenumber";
	public final static String USER_TYPE="USER_TYPE";
	
	//USER INFO
	public final static int ADMIN_TYPE=1;
	public final static int AGENT_TYPE=2;
	public final static String USER_ID="user_id";
	//job status
	public final static int COMPLETED_JOB=1;
	public final static int PENDING_JOB=0;
	
	//GCM ID
	public final static String GCMID = "gcm_id";
	public final static String APPID = "434629589694";

}
