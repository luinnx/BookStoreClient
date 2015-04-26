package com.bookstore.app.entities;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class AgentInfo {
	@SerializedName("location_name")
	public String AgentLocation;
	@SerializedName("address")
	public String AgentAddress;
	@SerializedName("email")
	public String AgentEmail;
	@SerializedName("mobilenumber")
	public String AgentMobileNumber;
	@SerializedName("createdate")
	public long AgentCreateDate;
	@SerializedName("agentname")
	public String AgentName;
	@SerializedName("mponumber")
	public String AgentMPONumber;
	@SerializedName("agentpicurl")
	public String AgentPicUrl;
	
}
