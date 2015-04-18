package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.entities.TeacherListRoot;

public interface IAdminManager {
	public LoginEntity getAuthentication(String email, String password,
			String imei, int userType);

	public JobListRoot getJobList(int jobStatus);

	public JobEntity getJobDetails(String jobId);

	public boolean createAgent(String full_name, String password, String email,
			String mobile_no, String address, byte[] pic_url, String mpo_no,
			int isActive, int type);

	public AgentListRoot getAgentList();

	public BookListRoot getBookList();

	public TeacherListRoot getTeacherList();

}
