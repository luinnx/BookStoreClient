package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.JobDetails;

public interface IAgent {
	public AgentJobListRoot getJobList(int agentId, int job_status, int pageIndex); 
	public JobDetails getJobDetails(String jobID);
}
