package com.bookstore.app.utils;

public class CommonUrls {

	static CommonUrls commonUrls;

	// public String BASE_URL="http://192.168.0.103:8080/BookStoreService/api/";

	public String BASE_URL = "http://192.168.0.101:8084/BookStoreService/api/";
	public String IMAGE_BASE_URL = "";

	public String getAuthentication = BASE_URL
			+ "user/authentication?email=%s&password=%s&IMEI=%s&type=%s";
	public String getCompletedJobList = BASE_URL
			+ "admin/job_list?job_status=%s";
	public String individualJobDetails = BASE_URL
			+ "admin/job_details?job_id=%s";
	public String createAgent = BASE_URL + "admin/add_agent";
	public String getAllAgent = BASE_URL + "admin/get_agent_list?pageIndex=%s";
	public String getIndividualAgentDetails = BASE_URL
			+ "admin/agent_details?agentID=%s";

	// agent service api
	public String agentJobList = BASE_URL
			+ "agent/job_list?agentId=%s&job_status=%s&pageIndex=%s";

	public static CommonUrls getInstance() {
		return commonUrls;
	}

	public static void initialization() {
		if (commonUrls == null)
			commonUrls = new CommonUrls();
	}
}
