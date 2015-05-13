package com.bookstore.app.interfaces;

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

public interface IAdminManager {
	public LoginEntity getAuthentication(String email, String password,
			String imei, int userType);

	public JobListRoot getJobList(int jobStatus);

	public JobEntity getJobDetails(String jobId);

	public boolean createAgent(String full_name, String password, String email,
			String mobile_no, String address, byte[] pic_url, String mpo_no,
			int isActive, int type);

	public AgentListRoot getAgentList(int pageIndex);

	public AgentEntity getIndividualAgentDetails(String agentID);

	public BookListRoot getBookList(int pageIndex);

	public BookEntity getIndividualBookDetails(String bookID);

	public Boolean addTeacher(String fullName, String userName,
			String password, String mobileNumber, String Institude);

	public TeacherListRoot getTeacherList();

	public Boolean addBook(String bookName, String writterName,
			String publisherName, String bookCondition, String bookPrice,
			String isbnNumber, String publishDate, byte[] pic_url,
			String bookQuantity, String catagoryId, String subCatagoryID,
			String subSubCatagoryID);
	public BookListRoot querySpeceficTypesBook(String catagory,String subCatagory,String subSubCatagory);
	public JobCreateEntity createJob(String bookName, String bookID, String no_of_book, 
            String teacherID, String teacher_institute, String jobStatus, 
            String agentID, String agentGCMID, String adminId);
	
	public DonationListRoot getAllDonationList(int index);
	public AgentLocationMapRoot getAgentsLocation();
	
	public IndividualTADA getTada(int id);
	public TaDaListRoot getAllTaDaList(int pageIndex);

}
