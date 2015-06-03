package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentDonationEntity {
	@SerializedName("id")
	public int id;
	
	@SerializedName("totalamount")
	public double totalAmount;
	
	@SerializedName("donationstatus")
	public int status;
	
	@SerializedName("agentpic")
	public String agentpic;
	
	@SerializedName("agentname")
	public String agentName;
	
	@SerializedName("createdate")
	public long cretedate;
}
