package com.bookstore.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AgentLocationMapRoot {
	@SerializedName("AgentLocation")
	public ArrayList<AgentLocationEntity> agentLocationList = null;

	public AgentLocationMapRoot() {
		agentLocationList = new ArrayList<AgentLocationEntity>();
	}

}
