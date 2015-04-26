package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AgentListRoot {
	@SerializedName("AgentList")
	public List<AgentEntity> agentList;
	
	public AgentListRoot() {
		agentList=new ArrayList<AgentEntity>();
	}

}
