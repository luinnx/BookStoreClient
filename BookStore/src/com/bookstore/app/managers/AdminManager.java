package com.bookstore.app.managers;

import android.util.Base64;

import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookEntity;
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
	public AgentListRoot getAgentList(int pageIndex) {
		AgentListRoot agentListRoot = null;
		try {
			agentListRoot = (AgentListRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonUrls.getInstance().getAllAgent, pageIndex),
							AgentListRoot.class);
			if (agentListRoot.agentList.size() > 0)
				return agentListRoot;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	@Override
	public TeacherListRoot getTeacherList() {
		TeacherListRoot listRoot=null;
		try{
			listRoot=(TeacherListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getAllTeacher), TeacherListRoot.class);
		}catch(Exception ex){
			ex.printStackTrace();
		}
			

		return listRoot;
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

	/*
	 * public String password; public String email; public String mobile_no;
	 * public String address; public String pic_url; public String gcm_id;
	 * public String mpo_no; public int isActive; public int type; public String
	 * rowPic;
	 */

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

	@Override
	public AgentEntity getIndividualAgentDetails(String agentID) {
		AgentEntity agentEntity = null;
		try {
			agentEntity = (AgentEntity) JSONfunctions.retrieveDataFromStream(
					String.format(
							CommonUrls.getInstance().getIndividualAgentDetails,
							agentID), AgentEntity.class);
			if (agentEntity != null)
				return agentEntity;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	@Override
	public BookListRoot getBookList(int pageIndex) {
		BookListRoot bookListRoot=null;
		bookListRoot=(BookListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getAllBooks,pageIndex), BookListRoot.class);
		return bookListRoot;
	}

	@Override
	public BookEntity getIndividualBookDetails(String bookID) {
		BookEntity bookEntity=null;
		bookEntity=(BookEntity) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getIndividualBookInfo, bookID), BookEntity.class);
		return bookEntity;
	}

}
