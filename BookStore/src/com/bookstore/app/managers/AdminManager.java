package com.bookstore.app.managers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Base64;
import android.util.Log;

import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.AgentLocationMapRoot;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.DonationListRoot;
import com.bookstore.app.entities.IndividualTADA;
import com.bookstore.app.entities.JobCreateEntity;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.entities.TaDaListRoot;
import com.bookstore.app.entities.TeacherListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.utils.CommonConstraints;
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
		TeacherListRoot listRoot = null;
		try {
			listRoot = (TeacherListRoot) JSONfunctions.retrieveDataFromStream(
					String.format(CommonUrls.getInstance().getAllTeacher),
					TeacherListRoot.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return listRoot;
	}

	@Override
	public LoginEntity getAuthentication(String email, String password,
			String imei) {
		LoginEntity entity = null;
		entity = (LoginEntity) JSONfunctions.retrieveDataFromStream(String
				.format(CommonUrls.getInstance().getAuthentication, email,
						password, imei), LoginEntity.class);
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
		BookListRoot bookListRoot = null;
		bookListRoot = (BookListRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonUrls.getInstance().getAllBooks, pageIndex),
				BookListRoot.class);
		return bookListRoot;
	}

	@Override
	public BookEntity getIndividualBookDetails(String bookID) {
		BookEntity bookEntity = null;
		bookEntity = (BookEntity) JSONfunctions.retrieveDataFromStream(
				String.format(CommonUrls.getInstance().getIndividualBookInfo,
						bookID), BookEntity.class);
		return bookEntity;
	}

	@Override
	public Boolean addTeacher(String fullName, String userName,
			String password, String mobileNumber, String Institude) {
		Boolean result = false;
		try {
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(
					CommonUrls.getInstance().addTeacher, URLEncoder.encode(fullName,
							CommonConstraints.EncodingCode), userName,
					password, URLEncoder.encode(Institude,
							CommonConstraints.EncodingCode), mobileNumber), Boolean.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * public int id; public int catagory_id; public int sub_catagoty; public
	 * String sub_sub_catagoty; public String fullname; public String
	 * auther_name; public String publisher_name; public String condition;
	 * public int quantity; public String isbn; public String puslish_date;
	 * public double price; public String pic_url; public String rowPic;
	 */

	@Override
	public Boolean addBook(String bookName, String writterName,
			String publisherName, String bookCondition, String bookPrice,
			String isbnNumber, String publishDate, byte[] pic_url,
			String bookQuantity, String catagoryId, String subCatagoryID,
			String subSubCatagoryID) {
		Boolean result = false;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("fullname", bookName);
			jsonObject.put("auther_name", writterName);
			jsonObject.put("publisher_name", publisherName);
			jsonObject.put("puslish_date", publishDate);
			jsonObject.put("condition", bookCondition);
			jsonObject.put("isbn", isbnNumber);
			jsonObject.put("quantity", bookQuantity);
			jsonObject.put("price", bookPrice);

			jsonObject.put("catagory_id", catagoryId);
			jsonObject.put("sub_catagoty", subCatagoryID);
			jsonObject.put("sub_sub_catagoty", subSubCatagoryID);

			jsonObject.put(
					"rowPic",
					pic_url != null ? Base64.encodeToString(pic_url,
							Base64.NO_WRAP) : "");
			result = (Boolean) JSONfunctions.retrieveDataFromJsonPost(
					CommonUrls.getInstance().createBook, jsonObject,
					Boolean.class);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public BookListRoot querySpeceficTypesBook(String catagory,
			String subCatagory, String subSubCatagory) {
		BookListRoot bookListRoot = null;
		bookListRoot = (BookListRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonUrls.getInstance().searchSpeceficBooks,
						catagory, subCatagory, subSubCatagory),
				BookListRoot.class);
		return bookListRoot;
	}

	@Override
	public JobCreateEntity createJob(String bookName, String bookID,
			String no_of_book, String teacherID, String teacher_institute,
			String jobStatus, String agentID, String agentGCMID, String adminId) {

		JobCreateEntity result = null;
		try {
			bookName = URLEncoder.encode(bookName,
					CommonConstraints.EncodingCode);
			teacher_institute = URLEncoder.encode(teacher_institute,
					CommonConstraints.EncodingCode);
			result = (JobCreateEntity) JSONfunctions.retrieveDataFromStream(
					String.format(CommonUrls.getInstance().createJob, bookName,
							bookID, no_of_book, teacherID, teacher_institute,
							jobStatus, agentID, URLEncoder.encode(agentGCMID,
									CommonConstraints.EncodingCode), adminId),
					JobCreateEntity.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public DonationListRoot getAllDonationList(int index) {
		DonationListRoot donationListRoot=null;
		donationListRoot=(DonationListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getDonationList, index), DonationListRoot.class);
		return donationListRoot;
	}

	@Override
	public AgentLocationMapRoot getAgentsLocation( ) {
		
		List<Object> complexList=new ArrayList<Object>();
		
		AgentLocationMapRoot agentLocationMapRoot=null;
		agentLocationMapRoot=(AgentLocationMapRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getAgentsLocationList), AgentLocationMapRoot.class);
		
		/*complexList.add(agentLocationMapRoot);
		
		AgentListRoot agentListRoot = null;
		try {
			agentListRoot = (AgentListRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonUrls.getInstance().getAllAgent, pageIndex),
							AgentListRoot.class);
			complexList.add(agentListRoot);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		*/
		
		return agentLocationMapRoot;
	}

	@Override
	public IndividualTADA getTada(int id) {
		IndividualTADA individualTADA = null;
		try{
			individualTADA = (IndividualTADA) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getTadaDetails, id), IndividualTADA.class);
		}catch(Exception ex){
			Log.e("BSA", ex.getMessage());
		}
		return individualTADA;
	}

	@Override
	public TaDaListRoot getAllTaDaList(int pageIndex) {
		TaDaListRoot listRoot=null;
		try{
			listRoot=(TaDaListRoot) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().getTadaList,
							pageIndex), TaDaListRoot.class);
		}catch(Exception exception){
			Log.e("BS", exception.getMessage());
		}
		
		return listRoot;
	}
}
