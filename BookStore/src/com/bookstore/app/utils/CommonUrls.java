package com.bookstore.app.utils;

public class CommonUrls {

	static CommonUrls commonUrls;

	// public String BASE_URL="http://192.168.0.103:8080/BookStoreService/api/";

	/*public String BASE_URL = "http://192.168.0.50:8084/BookStoreService/api/";
	public String IMAGE_BASE_URL = "http://192.168.0.50:8084/BookStoreService";*/
	
	public String BASE_URL = "http://182.48.75.85:8080/BookStoreService/api/";
	public String IMAGE_BASE_URL = "http://182.48.75.85:8080/BookStoreService";

	public String getAuthentication = BASE_URL
			+ "user/authentication?email=%s&password=%s&IMEI=%s&type=%s";
	public String getCompletedJobList = BASE_URL
			+ "admin/job_list?job_status=%s";
	public String individualJobDetails = BASE_URL
			+ "admin/job_details?job_id=%s";
	public String createAgent = BASE_URL + "admin/add_agent";
	public String createBook = BASE_URL + "admin/add_book";
	public String getAllAgent = BASE_URL + "admin/get_agent_list?pageIndex=%s";
	public String getIndividualAgentDetails = BASE_URL
			+ "admin/agent_details?agentID=%s";
	public String getAllTeacher=BASE_URL+"admin/all_teacher_list";
	public String getAllBooks=BASE_URL+"admin/get_book_list?pageIndex=%s";
	
	public String getIndividualBookInfo=BASE_URL+"admin/get_book_info?bookId=%s";
	public String addTeacher=BASE_URL+"admin/add_teacher?full_name=%s&user_name=%s&password=%s&institute=%s&" +
			"mobile=%s";
	public String searchSpeceficBooks=BASE_URL+"admin/book_search?catagory=%s&sub_catagory=%s&sub_sub_catagory=%s";

	// agent service api
	public String agentJobList = BASE_URL
			+ "agent/job_list?agentId=%s&job_status=%s&pageIndex=%s";
	public String agentJobDetails = BASE_URL + "agent/job_details?jobID=%s";
	public String jobSubimt = BASE_URL + "agent/job_submit?teacherUserName=%s&teacherPassword=%s&bookID=%s&no_of_book=%s&jobid=%s&job_status=%s";
	public String agentLocation = BASE_URL+"agent/add_location?agentid=%s&latitude=%s&longitude=%s&locationName=%s";
	public String getAgentInformation = BASE_URL + "agent/agent_info?agent_id=%s";
	public String agentDonation = BASE_URL + "agent/add_donetion?agent_id=%s&amount=%s&comment=%s";
	
	// User Common Service
	public String addGCMID = BASE_URL+"user/add_gcm?userID=%s&type=%s&gcmID=%s";
	public String changePassword = BASE_URL + "user/change_password?userID=%s&type=%s&oldPassword=%s&newPassword=%s";
	public String createJob = BASE_URL + "admin/job_create?bookName=%s&bookID=%s&no_of_book=%s&teacherID=%s&teacher_institute=%s&" +
			"jobStatus=%s&agentID=%s&agentGCMID=%s&adminId=%s";
	public String getDonationList=BASE_URL+"admin/donation_list?pageIndex=%s";
	public String getAgentsLocationList=BASE_URL+"admin/agent_location_list";
	
	public static CommonUrls getInstance() {
		return commonUrls;
	}

	public static void initialization() {
		if (commonUrls == null)
			commonUrls = new CommonUrls();
	}
}
