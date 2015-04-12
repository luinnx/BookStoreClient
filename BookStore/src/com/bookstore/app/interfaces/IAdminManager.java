package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.JobListRoot;

public interface IAdminManager {
	public JobListRoot getJobList(int jobStatus);
	public AgentListRoot getAgentList();

}
