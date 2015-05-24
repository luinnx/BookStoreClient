package com.bookstore.app.utils;

public class CommonUrls {

	static CommonUrls commonUrls;

	// public String BASE_URL="http://192.168.0.103:8080/BookStoreService/api/";

	/*public String BASE_URL = "http://192.168.0.50:8084/BookStoreService/api/";
	public String IMAGE_BASE_URL = "http://192.168.0.50:8084/BookStoreService";*/
	
	/*public String BASE_URL = "http://182.48.75.85:8080/BookStoreService/api/";
	public String IMAGE_BASE_URL = "http://182.48.75.85:8080/BookStoreService";*/
	
	public String BASE_URL = "http://173.82.4.196:8080/BookStoreService/api/";
	public String IMAGE_BASE_URL = "http://173.82.4.196:8080/BookStoreService";
	
	//user Common pi
	public String getAuthentication = BASE_URL+"user/authentication?email=%s&password=%s&IMEI=%s";
	public String addGCMID = BASE_URL+"user/add_gcm";
	public String forgot_password = BASE_URL+"user/forgot_password?userID=%s";
	public String changePassword = BASE_URL + "user/change_password?userID=%s&oldPassword=%s&newPassword=%s";

	// agent api
	public String agentJobList = BASE_URL+ "agent/job_list?agentId=%s&job_status=%s&pageIndex=%s";
	public String agentJobDetails = BASE_URL + "agent/job_details?jobID=%s";
	public String jobSubimt = BASE_URL + "agent/job_submit";
	public String agentLocation = BASE_URL+"agent/add_location?agentid=%s&latitude=%s&longitude=%s&locationName=%s";
	public String getAgentInformation = BASE_URL + "agent/agent_info?agent_id=%s";
	public String agentDonation = BASE_URL + "agent/add_donetion?agent_id=%s&amount=%s&comment=%s";
	public String addTada = BASE_URL + "agent/addTada";
	public String agent_tada_list=BASE_URL+"agent/get_tada_list?agent_id=%s&pageindex=%s";
	public String agentGetIndividualDonationDetails = BASE_URL+"agent/get_individual_doantion_details?doantionID=%s";
	public String get_individual_tada_details = BASE_URL+"agent/get_individual_tada_details?tadaID=%s";
	
	//get_individual_tada_details
	

	// admin api	
	public String getCompletedJobList = BASE_URL+ "admin/job_list?job_status=%s&pageIndex=%s";
	public String individualJobDetails = BASE_URL+ "admin/job_details?job_id=%s";
	public String createAgent = BASE_URL + "admin/add_agent";
	public String createBook = BASE_URL + "admin/add_book";
	public String addTeacher=BASE_URL+"admin/add_teacher?full_name=%s&user_name=%s&password=%sinstitute=%s&mobile=%s";	
	public String getAllAgent = BASE_URL + "admin/all_agent_list";	
	public String getIndividualAgentDetails = BASE_URL+ "admin/agent_details?agentID=%s";	
	public String getAllTeacher=BASE_URL+"admin/all_teacher_list";
	public String createJob = BASE_URL + "admin/job_create?bookID=%s&no_of_book=%s&teacherID=%s&jobStatus=%s&agentID=%s&agentGCMID=%s&adminId=%s";
	public String getAllBooks=BASE_URL+"admin/get_book_list?pageIndex=%s";	
	public String getAllAgentList = BASE_URL+"admin/get_agent_list?pageIndex=%s";
	public String getAllTeacherList = BASE_URL+"admin/get_teacher_list?pageIndex=%s";
	public String searchSpeceficBooks=BASE_URL+"admin/book_search?catagory=%s&sub_catagory=%s&sub_sub_catagory=%s";
	public String getIndividualBookInfo=BASE_URL+"admin/get_book_info?bookId=%s";
	public String getDonationList=BASE_URL+"admin/donation_list?pageIndex=%s";
	public String getAgentsLocationList=BASE_URL+"admin/agent_location_list";
	public String getTadaList = BASE_URL+"admin/tadaList?pageIndex=%s";	
	public String getTadaDetails = BASE_URL+"admin/individula_tada?tadaid=%s";
	public String getIndividialDonation = BASE_URL+"admin/individula_donation?donationid=%s";
	public String setDonationACK = BASE_URL+"admin/donation_ack?donationID=%s&agentGcmID=%s&donationStatus=%s&adminID=%s&amount=%s";
	public String setTadaACK = BASE_URL+"admin/tada_ack?tadaID=%s&agentGcmID=%s&tadastatus=%s&adminID=%s";
	public String setJobSubmitACK = BASE_URL+"admin/job_ack?jobID=%s&adminID=%s&agentGcmID=%s&bookid=%s&no_of_book=%s&jobStatus=%s&remarks=%s";
	public String jobDetailsAcceptReject=BASE_URL+"admin/job_detais_ack?jobID=%s";
	
	public static CommonUrls getInstance() {
		return commonUrls;
	}

	public static void initialization() {
		if (commonUrls == null)
			commonUrls = new CommonUrls();
	}
}
