package com.bookstore.app.managers;

import java.net.URLEncoder;

import android.util.Log;

import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class AgentManager implements IAgent{

	@Override
	public AgentJobListRoot getJobList(int agentId, int job_status,
			int pageIndex) {
		AgentJobListRoot agentJobListRoot = null;
		try{
			agentJobListRoot = (AgentJobListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().agentJobList, agentId,job_status,pageIndex), AgentJobListRoot.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return agentJobListRoot;
	}

	@Override
	public JobDetails getJobDetails(String jobID) {
		JobDetails jobDetails = null;
		try{
			jobDetails = (JobDetails) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().agentJobDetails, jobID), JobDetails.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return jobDetails;
	}

	@Override
	public boolean jobSubmit(String teacherUserName, String teacherPassword,
			int bookID, int no_of_book, int jobid, int job_status) {
		Boolean result = false;
		try{
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().jobSubimt, 
					teacherUserName,
					teacherPassword,
					bookID,
					no_of_book,
					jobid,
					job_status), Boolean.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public boolean addLocation(int agentid, double latitude, double longitude, String locationName) {
		boolean result = false;
		try{
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().agentLocation, 
					agentid, 
					latitude, 
					longitude,
					URLEncoder.encode(locationName, CommonConstraints.EncodingCode)), Boolean.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public AgentInfo getAgentInformation(int agentID) {
		AgentInfo agentInfo = null;
		try{
			agentInfo = (AgentInfo) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().getAgentInformation, agentID), AgentInfo.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return agentInfo;
	}

}
