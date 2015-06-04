package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserListRoot {
	@SerializedName("UserList")
	public List<AgentEntity> agentList;
	
	public UserListRoot() {
		agentList=new ArrayList<AgentEntity>();
	}
}
