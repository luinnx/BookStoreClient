package com.bookstore.app.managers;

import android.util.Base64;

import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.entities.TeacherListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminManager implements IAdminManager {

	@Override
	public JobListRoot getJobList(int jobStatus) {
		JobListRoot jobListRoot = null;
		try {
			jobListRoot = (JobListRoot) JSONfunctions.retrieveDataFromStream(
					String.format(CommonUrls.getInstance().getCompletedJobList,
							jobStatus), JobListRoot.class);

			if (jobListRoot.jobList.size() > 0) {
				return jobListRoot;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return null;
	}

	@Override
	public AgentListRoot getAgentList() {

		return null;
	}

	@Override
	public BookListRoot getBookList() {

		return null;
	}

	@Override
	public TeacherListRoot getTeacherList() {

		return null;
	}

	@Override
	public LoginEntity getAuthentication(String email, String password,
			String imei, int userType) {
		LoginEntity entity = null;
		entity = (LoginEntity) JSONfunctions.retrieveDataFromStream(String
				.format(CommonUrls.getInstance().getAuthentication, email,
						password, imei, userType), LoginEntity.class);
		return entity;
	}

	@Override
	public JobEntity getJobDetails(String jobId) {
		JobEntity entity = null;
		entity = (JobEntity) JSONfunctions.retrieveDataFromStream(String
				.format(CommonUrls.getInstance().individualJobDetails, jobId),
				JobEntity.class);
		return entity;
	}

	/*public String password;
	public String email;
	public String mobile_no;
	public String address;
	public String pic_url;
	public String gcm_id;
	public String mpo_no;
	public int isActive;
	public int type;
	public String rowPic;*/

	@Override
	public boolean createAgent(String full_name, String password, String email,
			String mobile_no, String address, byte[] pic_url, String mpo_no,
			int isActive, int type) {

		boolean result = false;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("full_name", full_name);
			jsonObject.put("email", email);
			jsonObject.put("mobile_no", mobile_no);
			jsonObject.put("address", address);
			jsonObject.put("password", password);
			jsonObject.put("mpo_no", mpo_no);
			jsonObject.put("isActive", isActive);
			jsonObject.put("type", type);
			jsonObject.put(
					"rowPic",
					pic_url != null ? Base64.encodeToString(pic_url,
							Base64.NO_WRAP) : "");
			result = (Boolean) JSONfunctions.retrieveDataFromJsonPost(
					CommonUrls.getInstance().createAgent, jsonObject,
					Boolean.class);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
