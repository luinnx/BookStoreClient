package com.bookstore.app.utils;

public class CommonUrls {

	static CommonUrls commonUrls;
	
	//public String BASE_URL="http://192.168.0.103:8080/BookStoreService/api/";
	
	public String BASE_URL="http://192.168.0.50:8084/BookStoreService/api/";
	public String IMAGE_BASE_URL="";
	
	public String getAuthentication=BASE_URL+"user/authentication?email=%s&password=%s&IMEI=%s&type=%s";
	public String getCompletedJobList=BASE_URL+"admin/admin_joblist?job_status=%s";

	public static CommonUrls getInstance() {
		return commonUrls;
	}

	public static void initialization() {
		if (commonUrls == null)
			commonUrls = new CommonUrls();
	}
}
