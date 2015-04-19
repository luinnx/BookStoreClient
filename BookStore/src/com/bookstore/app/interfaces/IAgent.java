package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentJobListRoot;

public interface IAgent {
	public AgentJobListRoot getJobList(int agentId, int job_status, int pageIndex); 
}
