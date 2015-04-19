package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AgentJobListRoot {
	@SerializedName("JobList")
	public ArrayList<AgentJobList> agentJobList = null;
	
	public AgentJobListRoot() {
		agentJobList = new ArrayList<AgentJobList>();
	}
}
