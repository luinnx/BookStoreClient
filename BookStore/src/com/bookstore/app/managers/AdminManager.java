package com.bookstore.app.managers;


import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.entities.TeacherListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class AdminManager implements IAdminManager{

	@Override
	public JobListRoot getJobList(int jobStatus) {
		JobListRoot jobListRoot=null;
		try{
			//jobListRoot=(JobListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getCompletedJobList, jobStatus), JobListRoot.class);
		
			/*if(jobListRoot!=null&&jobListRoot.jobList.size()>0){
				jobList=(ArrayList<JobEntity>) jobListRoot.jobList;
			}*/
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		return jobListRoot;
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
	public LoginEntity getAuthentication(String email, String password, String imei,
			int userType) {
		LoginEntity entity=null;
		entity=(LoginEntity) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getAuthentication, email,password,imei,userType), LoginEntity.class);
		return entity;
	}

}
