package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.JobDetails;

public interface IAgent {
	public AgentJobListRoot getJobList(int agentId, int job_status, int pageIndex); 
	public JobDetails getJobDetails(String jobID);
	public boolean jobSubmit(String teacherUserName, String teacherPassword, int bookID, int no_of_book, int jobid, int job_status);
	public boolean addLocation(int agentid, double latitude, double longitude, String locationName);
	public AgentInfo getAgentInformation(int agentID);
	public boolean addDonetion(int agentID, double amount, String comment);
}
